define(`MELEM', translit(ELEM, `a-z', `A-Z'))dnl
define(`MELEM', `R'MELEM)dnl
define(`ARRAYS', `r'ELEM`_arrays')dnl
define(`ELEMS', `r'ELEM`_elements')dnl
module MELEM
  use prelude.ptr.Ptr
  use prelude.typing.Type
  use prelude.boolean.Operators
  use prelude.machine.Operators
  use array.Array
  use int.Int
  use set.Set

  type t = {
    mutable ARRAYS: set Ptr.t;
    mutable ELEMS: Ptr.t -> {array ELEMTYPE}
  }

  predicate other_same_array (heap: t) (old_heap: t) (p: Ptr.t) =
    forall p'. p <> p' ->
      (heap.ELEMS p).elts = (old_heap.ELEMS p').elts

  predicate valid_access (heap: t) (p: Ptr.t) (i: jint) =
    mem p heap.ARRAYS && 0 <= i < (heap.ELEMS p).length

  val function iselem (heap: t) (p: Ptr.t) (i: jint) : ELEMTYPE
    requires { valid_access heap p i }
    ensures { result = (heap.ELEMS p)[i] }

  val load (heap: t) (p: Ptr.t) (i: jint) : ELEMTYPE
    (* raises { JException e -> npe_if_null e p || out_of_bounds e heap p i } *)
    ensures { valid_access heap p i -> result = iselem heap p i }

  val store (heap: t) (p: Ptr.t) (i: jint) (v: ELEMTYPE) : unit
    writes { heap.ELEMS }
    ensures { valid_access heap p i -> (heap.ELEMS p).elts = Map.set (old (heap.ELEMS p)).elts i v }
    ensures { valid_access heap p i -> (heap.ELEMS p) = (old (heap.ELEMS p)[i <- v]) }
    (* raises { JException e -> npe_if_null e p || out_of_bounds e heap p i } *)
    ensures { other_same_array heap (old heap) p }

  val function arraylength (heap: t) (p: Ptr.t) : jint
    requires { mem p heap.ARRAYS }
    ensures { result = int2i (heap.ELEMS p).length }

  predicate is_new_array (result: Ptr.t) (len: int) (heap: t) (old_heap: t) =
    result <> Ptr.null /\
    mem result heap.ARRAYS /\
    not (mem result old_heap.ARRAYS) /\
    (heap.ELEMS result).length = len /\
    forall i:int. 0 <= i < len -> (heap.ELEMS result)[i] = Default.ELEM
end
