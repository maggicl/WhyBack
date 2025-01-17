define(`ELEM',`z')dnl
define(`ELEMTYPE',`jbool')dnl
include(`primitive.mlw.m4')dnl

define(`ELEM',`b')dnl
define(`ELEMTYPE',`jbyte')dnl
include(`primitive.mlw.m4')dnl

define(`ELEM',`s')dnl
define(`ELEMTYPE',`jshort')dnl
include(`primitive.mlw.m4')dnl

define(`ELEM',`c')dnl
define(`ELEMTYPE',`jchar')dnl
include(`primitive.mlw.m4')dnl

define(`ELEM',`i')dnl
define(`ELEMTYPE',`jint')dnl
include(`primitive.mlw.m4')dnl

define(`ELEM',`j')dnl
define(`ELEMTYPE',`jlong')dnl
include(`primitive.mlw.m4')dnl

define(`ELEM',`f')dnl
define(`ELEMTYPE',`jfloat')dnl
include(`primitive.mlw.m4')dnl

define(`ELEM',`d')dnl
define(`ELEMTYPE',`jdouble')dnl
include(`primitive.mlw.m4')dnl

module L
  use prelude.ptr.Ptr
  use prelude.ptr.Operators
  use prelude.typing.Type
  use set.Set

  type static_field = abstract { s_type: Type.t; }
  type instance_field = abstract { i_type: Type.t; }

  type t = {
    mutable pointers: set Ptr.t;
    typeof: Ptr.t -> Type.t;
    mutable l_static_fmap: static_field -> Ptr.t;
    mutable l_instance_fmap: (Ptr.t, instance_field) -> Ptr.t;
  } invariant {
    (not (mem Ptr.null pointers)) &&
    (forall s:static_field. l_static_fmap s = Ptr.null \/ typeof (l_static_fmap s) :> s.s_type) &&
    (forall p:Ptr.t, s:instance_field. compatible (l_instance_fmap (p,s)) typeof s.i_type)
  }

  predicate other_same_static (heap: t) (old_heap: t) (f: static_field) =
    forall ff. f <> ff ->
      heap.l_static_fmap ff = old_heap.l_static_fmap ff

  predicate other_same_instance (heap: t) (old_heap: t) (p: Ptr.t) (f: instance_field) =
    forall pp,ff. p <> pp || f <> ff ->
      heap.l_instance_fmap (pp,ff) = old_heap.l_instance_fmap (pp,ff)

  val function isf (heap: t) (p: Ptr.t) (f: instance_field) : Ptr.t
    requires { p <> Ptr.null }
    ensures { result = heap.l_instance_fmap (p,f) }
    ensures { compatible result heap.typeof f.i_type }

  val getf (heap: t) (p: Ptr.t) (f: instance_field) : Ptr.t
    (* raises { JException e -> npe_if_null e p } *)
    ensures { not_null p -> result = isf heap p f }
    ensures { not_null p -> compatible result heap.typeof f.i_type }

  val putf (heap: t) (p: Ptr.t) (f: instance_field) (v: Ptr.t) : unit
    writes { heap.l_instance_fmap }
    requires { compatible v heap.typeof f.i_type }
    (* raises { JException e -> npe_if_null e p } *)
    ensures { not_null p -> heap.l_instance_fmap (p,f) = v }
    ensures { other_same_instance heap (old heap) p f }

  val function iss (heap: t) (f: static_field) : Ptr.t
    ensures { result = heap.l_static_fmap f }
    ensures { compatible result heap.typeof f.s_type }

  let function gets [@inline:trivial] (heap: t) (f: static_field) : Ptr.t =
    iss heap f

  val puts (heap: t) (f: static_field) (v: Ptr.t) : unit
    writes { heap.l_static_fmap }
    requires { compatible v heap.typeof f.s_type }
    ensures { heap.l_static_fmap f = v }
    ensures { other_same_static heap (old heap) f }

  predicate is_new_ptr (result: Ptr.t) (of_type: Type.t) (heap: t) (old_heap: t) =
    result <> Ptr.null /\
    mem result heap.pointers /\
    not (mem result old_heap.pointers) /\
    heap.typeof result = of_type
end

define(`ELEM',`z')dnl
define(`ELEMTYPE',`jbool')dnl
include(`primitive_array.mlw.m4')dnl

define(`ELEM',`b')dnl
define(`ELEMTYPE',`jbyte')dnl
include(`primitive_array.mlw.m4')dnl

define(`ELEM',`s')dnl
define(`ELEMTYPE',`jshort')dnl
include(`primitive_array.mlw.m4')dnl

define(`ELEM',`c')dnl
define(`ELEMTYPE',`jchar')dnl
include(`primitive_array.mlw.m4')dnl

define(`ELEM',`i')dnl
define(`ELEMTYPE',`jint')dnl
include(`primitive_array.mlw.m4')dnl

define(`ELEM',`j')dnl
define(`ELEMTYPE',`jlong')dnl
include(`primitive_array.mlw.m4')dnl

define(`ELEM',`f')dnl
define(`ELEMTYPE',`jfloat')dnl
include(`primitive_array.mlw.m4')dnl

define(`ELEM',`d')dnl
define(`ELEMTYPE',`jdouble')dnl
include(`primitive_array.mlw.m4')dnl

module Heap
  use int.Int
  use prelude.ptr.Ptr
  use prelude.typing.Type
  use prelude.ptr.Operators
  use TYPES
  use array.Array
  use int.Int
  use set.Set
  use Z
  use B
  use S
  use C
  use I
  use J
  use F
  use D
  use L
  use RZ
  use RB
  use RS
  use RC
  use RI
  use RJ
  use RF
  use RD

  type t = {
    z: Z.t;
    b: B.t;
    s: S.t;
    c: C.t;
    i: I.t;
    j: J.t;
    f: F.t;
    d: D.t;
    l: L.t;
    rz: RZ.t;
    rb: RB.t;
    rs: RS.t;
    rc: RC.t;
    ri: RI.t;
    rj: RJ.t;
    rf: RF.t;
    rd: RD.t;
    mutable rl_arrays: set Ptr.t;
    rl_element_type: Ptr.t -> Type.t;
    mutable rl_elements: Ptr.t -> {array Ptr.t};
  } invariant {
     (not (mem Ptr.null rl_arrays)) &&
     (forall e:Ptr.t, i:int. (mem e rl_arrays && (0 <= i < (rl_elements e).length)) ->
       compatible (rl_elements e)[i] l.typeof (rl_element_type e))
  }

  type ht = t

  use L

  let predicate check_ptr_mem [@inline:trivial] (heap: ht) (p: Ptr.t) (typ: Type.t)
    requires { not_null p }
  =
    heap.l.pointers p && match (typ) with
      | BoolArray ->      heap.rz.rz_arrays p
      | ByteArray ->      heap.rb.rb_arrays p
      | ShortArray ->     heap.rs.rs_arrays p
      | CharArray ->      heap.rc.rc_arrays p
      | IntArray ->       heap.ri.ri_arrays p
      | LongArray ->      heap.rj.rj_arrays p
      | FloatArray ->     heap.rf.rf_arrays p
      | DoubleArray ->    heap.rd.rd_arrays p
      | Class _ ->        true
      | ArrayOf _ ->      heap.rl_arrays p
    end

  let predicate instanceof [@inline:trivial] (heap: ht) (p: Ptr.t) (t: Type.t) =
    not_null p && compatible p heap.l.typeof t && check_ptr_mem heap p t

  let predicate isinstance [@inline:trivial] (heap: ht) (p: Ptr.t) (t: Type.t) =
    is_null p || instanceof heap p t

  val checkcast (heap: ht) (p: Ptr.t) (t: Type.t) : Ptr.t
    ensures { compatible p heap.l.typeof t -> result = p }
    (* raises { JException e -> not (hastype heap p t) && e = Java.Lang.ClassCastException.class } *)

  val function iscast (heap: ht) (p: Ptr.t) (t: Type.t) : Ptr.t
    requires { compatible p heap.l.typeof t }
    ensures { result = p }

  val new (heap: ht) (id: Type.class) : Ptr.t
    writes { heap.l.pointers }
    ensures { L.is_new_ptr result (Type.Class id) heap.l (old heap.l) }

  (* TODO: when re-introducing PEI behaviour, here is the place for NegativeArraySize and ArrayStore *)

  val znewarray (heap: ht) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.rz.rz_arrays, heap.rz.rz_elements }
    ensures { len >= 0 -> L.is_new_ptr result Type.BoolArray heap.l (old heap.l) }
    ensures { len >= 0 -> RZ.is_new_array result len heap.rz (old heap.rz) }
    
  val bnewarray (heap: ht) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.rb.rb_arrays, heap.rb.rb_elements }
    ensures { len >= 0 -> L.is_new_ptr result Type.ByteArray heap.l (old heap.l) }
    ensures { len >= 0 -> RB.is_new_array result len heap.rb (old heap.rb) }
        
  val snewarray (heap: ht) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.rs.rs_arrays, heap.rs.rs_elements }
    ensures { len >= 0 -> L.is_new_ptr result Type.ShortArray heap.l (old heap.l) }
    ensures { len >= 0 -> RS.is_new_array result len heap.rs (old heap.rs) }
              
  val cnewarray (heap: ht) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.rc.rc_arrays, heap.rc.rc_elements }
    ensures { len >= 0 -> L.is_new_ptr result Type.CharArray heap.l (old heap.l) }
    ensures { len >= 0 -> RC.is_new_array result len heap.rc (old heap.rc) }

  val inewarray (heap: ht) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.ri.ri_arrays, heap.ri.ri_elements }
    ensures { len >= 0 -> L.is_new_ptr result Type.IntArray heap.l (old heap.l) }
    ensures { len >= 0 -> RI.is_new_array result len heap.ri (old heap.ri) }
    
  val jnewarray (heap: ht) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.rj.rj_arrays, heap.rj.rj_elements }
    ensures { len >= 0 -> L.is_new_ptr result Type.LongArray heap.l (old heap.l) }
    ensures { len >= 0 -> RJ.is_new_array result len heap.rj (old heap.rj) }
        
  val fnewarray (heap: ht) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.rf.rf_arrays, heap.rf.rf_elements }
    ensures { len >= 0 -> L.is_new_ptr result Type.FloatArray heap.l (old heap.l) }
    ensures { len >= 0 -> RF.is_new_array result len heap.rf (old heap.rf) }
    
  val dnewarray (heap: ht) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.rd.rd_arrays, heap.rd.rd_elements }
    ensures { len >= 0 -> L.is_new_ptr result Type.DoubleArray heap.l (old heap.l) }
    ensures { len >= 0 -> RD.is_new_array result len heap.rd (old heap.rd) }

  scope RL
    predicate other_same_array (heap: ht) (old_heap: ht) (p: Ptr.t) =
      forall pp. p <> pp ->
        (heap.rl_elements p).elts = (old_heap.rl_elements pp).elts

    predicate valid_access (heap: ht) (p: Ptr.t) (i: jint) =
      mem p heap.rl_arrays && 0 <= i < (heap.rl_elements p).length

    val function iselem (heap: ht) (p: Ptr.t) (i: jint) : Ptr.t
      requires { valid_access heap p i }
      ensures { result = (heap.rl_elements p)[i] }

    val load (heap: ht) (p: Ptr.t) (i: jint) : Ptr.t
      (* raises { JException e -> npe_if_null e p || out_of_bounds e heap p i } *)
      ensures { valid_access heap p i -> result = iselem heap p i }

    val store (heap: ht) (p: Ptr.t) (i: jint) (v: Ptr.t) : unit
      writes { heap.rl_elements }
      ensures { valid_access heap p i -> (heap.rl_elements p).elts = Map.set (old (heap.rl_elements p)).elts i v }
      ensures { valid_access heap p i -> (heap.rl_elements p) = (old (heap.rl_elements p))[i <- v] }
      (* raises { JException e -> npe_if_null e p || out_of_bounds e heap p i } *)
      ensures { other_same_array heap (old heap) p }

    val function arraylength (heap: ht) (p: Ptr.t) : jint
      requires { mem p heap.rl_arrays }
      ensures { result = int2i (heap.rl_elements p).length }

    predicate is_new_array (result: Ptr.t) (len: int) (of_elem_type: Type.t) (heap: ht) (old_heap: ht) =
      result <> Ptr.null /\
      mem result heap.rl_arrays /\
      not (mem result old_heap.rl_arrays) /\
      heap.rl_element_type result = of_elem_type /\
      (heap.rl_elements result).length = len /\
      forall i:int. 0 <= i < len -> (heap.rl_elements result)[i] = Ptr.null
  end

  val lnewarray (heap: ht) (elem_type: Type.t) (len: jint) : Ptr.t
    writes { heap.l.pointers, heap.rl_arrays }
    ensures { len >= 0 -> L.is_new_ptr result (ArrayOf elem_type) heap.l (old heap.l) }
    ensures { len >= 0 -> RL.is_new_array result len elem_type heap (old heap) }
end
