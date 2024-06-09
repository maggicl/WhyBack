define(`MELEM', translit(ELEM, `a-z', `A-Z'))dnl
define(`STATICFMAP', `'ELEM`_static_fmap')dnl
define(`INSTANCEFMAP', `'ELEM`_instance_fmap')dnl
module MELEM
  use prelude.ptr.Ptr
  use prelude.typing.Type
  use prelude.boolean.Operators
  use prelude.machine.Operators

  type static_field = abstract { }
  type instance_field = abstract { }

  type t = {
    (* Static fields have only one ELEMTYPE per field declaration, as they are static *)
    mutable STATICFMAP: static_field -> ELEMTYPE;
    mutable INSTANCEFMAP: (Ptr.t, instance_field) -> ELEMTYPE;
  }

  predicate other_same_static (heap: t) (old_heap: t) (f: static_field) =
    forall ff. f <> ff ->
      heap.STATICFMAP ff = old_heap.STATICFMAP ff

  predicate other_same_instance (heap: t) (old_heap: t) (p: Ptr.t) (f: instance_field) =
    forall pp,ff. (p <> pp || f <> ff) ->
      heap.INSTANCEFMAP (pp,ff) = old_heap.INSTANCEFMAP (pp,ff)

  val function isf (heap: t) (p: Ptr.t) (f: instance_field) : ELEMTYPE
    requires { p <> Ptr.null }
    ensures { result = heap.INSTANCEFMAP (p, f) }

  val getf (heap: t) (p: Ptr.t) (f: instance_field) : ELEMTYPE
    (* raises { JException e -> npe_if_null e p } *)
    ensures { p <> Ptr.null -> result = isf heap p f }

  val putf (heap: t) (p: Ptr.t) (f: instance_field) (v: ELEMTYPE) : unit
    writes { heap.INSTANCEFMAP }
    (* raises { JException e -> npe_if_null e p } *)
    ensures { p <> Ptr.null -> heap.INSTANCEFMAP (p, f) = v }
    ensures { other_same_instance heap (old heap) p f }

  let function iss [@inline:trivial] (heap: t) (f: static_field) : ELEMTYPE =
    heap.STATICFMAP f

  let function gets [@inline:trivial] (heap: t) (f: static_field) : ELEMTYPE =
    iss heap f

  val puts (heap: t) (f: static_field) (c: ELEMTYPE) : unit
    writes { heap.STATICFMAP }
    ensures { heap.STATICFMAP f = c }
    ensures { other_same_static heap (old heap) f }
end
