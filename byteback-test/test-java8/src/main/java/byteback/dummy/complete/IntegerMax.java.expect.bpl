type Reference;

const unique ~null : Reference;

type Field a;

type Store  = [Reference]<a>[Field (a)]a;

function {:inline } ~heap.read<a>(h : Store, r : Reference, f : Field (a)) returns (a) { h[r][f] }

function {:inline } ~heap.update<a>(h : Store, r : Reference, f : Field (a), v : a) returns (Store) { h[r := h[r][f := v]] }

function ~heap.succ(h1 : Store, h2 : Store) returns (bool);

axiom (forall <a> h : Store, r : Reference, f : Field (a), v : a :: {~heap.update(h, r, f, v)} (~heap.isgood(~heap.update(h, r, f, v)) ==> ~heap.succ(h, ~heap.update(h, r, f, v))));

axiom (forall h1 : Store, h2 : Store, h3 : Store :: {~heap.succ(h1, h2), ~heap.succ(h2, h3)} ((h1 != h3) ==> ((~heap.succ(h1, h2) && ~heap.succ(h2, h3)) ==> ~heap.succ(h1, h3))));

axiom (forall h1 : Store, h2 : Store :: (~heap.succ(h1, h2) && (forall <a> r : Reference, f : Field (a) :: {~heap.read(h2, r, f)} (~heap.read(h1, r, f) == ~heap.read(h2, r, f)))));

function ~heap.isgood(h : Store) returns (bool);

function ~heap.isanchor(h : Store) returns (bool);

var ~heap : Store where (~heap.isgood(~heap) && ~heap.isanchor(~heap));

function ~allocated(Reference) returns (bool);

procedure ~new(t : Type) returns (~ret : Reference);
  ensures (~typeof(~heap, ~ret) == t);
  ensures ~allocated(~ret);

type Type;

const unique ~Object.Type : Field (Type);

const unique ~Primitive : Type;

function ~typeof(h : Store, r : Reference) returns (Type) { ~heap.read(h, r, ~Object.Type) }

function ~instanceof(h : Store, r : Reference, t : Type) returns (bool) { (~typeof(h, r) == t) }

axiom (forall h : Store, t : Type :: (~typeof(h, ~null) <: t));

function ~type.reference(Type) returns (Reference);

function ~type.reference_inverse(Reference) returns (Type);

axiom (forall t : Type :: {~type.reference(t)} (~type.reference_inverse(~type.reference(t)) == t));

type Box;

function ~box<a>(a) returns (Box);

function ~unbox<a>(Box) returns (a);

axiom (forall <a> x : a :: {~box(x)} (~unbox(~box(x)) == x));

function ~element(int) returns (Field (Box));

function ~element_inverse<a>(Field (a)) returns (int);

axiom (forall i : int :: {~element(i)} (~element_inverse(~element(i)) == i));

function ~lengthof(r : Reference) returns (int);

axiom (forall r : Reference :: (~lengthof(r) >= 0));

axiom (forall h1 : Store, h2 : Store, r : Reference, i : int :: ((~heap.succ(h1, h2) && ((0 <= i) && (i < ~lengthof(r)))) ==> (~heap.read(h1, r, ~element(i)) == ~heap.read(h2, r, ~element(i)))));

function ~array.type(Type) returns (Type);

function ~array.type_inverse(Type) returns (Type);

axiom (forall t : Type :: {~array.type(t)} (~array.type_inverse(~array.type(t)) == t));

function {:inline } ~array.read<b>(h : Store, a : Reference, i : int) returns (b) { (~unbox(~heap.read(h, a, ~element(i))) : b) }

function {:inline } ~array.update<b>(h : Store, a : Reference, i : int, v : b) returns (Store) { ~heap.update(h, a, ~element(i), ~box(v)) }

procedure ~array(t : Type, l : int) returns (~ret : Reference);
  ensures (~typeof(~heap, ~ret) == ~array.type(t));
  ensures ~allocated(~ret);
  ensures (~lengthof(~ret) == l);

function ~cmp<t>(a : t, b : t) returns (int);

axiom (forall i : real, j : real :: ((i < j) <==> (~cmp(i, j) == -1)));

axiom (forall i : real, j : real :: ((i > j) <==> (~cmp(i, j) == 1)));

axiom (forall i : real, j : real :: ((i == j) <==> (~cmp(i, j) == 0)));

axiom (forall i : int, j : int :: ((i < j) <==> (~cmp(i, j) == -1)));

axiom (forall i : int, j : int :: ((i > j) <==> (~cmp(i, j) == 1)));

axiom (forall i : int, j : int :: ((i == j) <==> (~cmp(i, j) == 0)));

function ~and(a : bool, b : bool) returns (bool) { (a && b) }

function ~or(a : bool, b : bool) returns (bool) { (a || b) }

function ~implies(a : bool, b : bool) returns (bool) { (a ==> b) }

function ~iff(a : bool, b : bool) returns (bool) { (a <==> b) }

function ~eq<t>(a : t, b : t) returns (bool) { (a == b) }

function ~neq<t>(a : t, b : t) returns (bool) { (a != b) }

function ~int.lt(a : int, b : int) returns (bool) { (a < b) }

function ~real.lt(a : real, b : real) returns (bool) { (a < b) }

function ~int.lte(a : int, b : int) returns (bool) { (a <= b) }

function ~real.lte(a : real, b : real) returns (bool) { (a <= b) }

function ~int.gt(a : int, b : int) returns (bool) { (a > b) }

function ~real.gt(a : real, b : real) returns (bool) { (a > b) }

function ~int.gte(a : int, b : int) returns (bool) { (a >= b) }

function ~real.gte(a : real, b : real) returns (bool) { (a >= b) }

function ~not(a : bool) returns (bool) { !a }

function ~int<a>(a) returns (int);

axiom (~int(false) == 0);

axiom (~int(true) == 1);

const $java.lang.Object : Type;

procedure java.lang.Object.$init$##(this : Reference) returns ();

procedure java.lang.Object.clone##(this : Reference) returns (~ret : Reference);
  requires (this != ~null);
  ensures (this != ~ret);
  ensures (forall <a> h : Store, f : Field (Box) :: {(~unbox(~heap.read(h, ~ret, f)) : a)} ((~heap.read(h, this, f) != ~heap.read(h, ~ret, f)) && ((~unbox(~heap.read(h, this, f)) : a) == (~unbox(~heap.read(h, ~ret, f)) : a))));
  ensures (~lengthof(this) == ~lengthof(~ret));

const unique java.lang.Object : Type;

const unique $byteback.dummy.complete.IntegerMax : Type;

procedure byteback.dummy.complete.IntegerMax.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.IntegerMax)) returns ()
{
  var _this : Reference where (~typeof(~heap, _this) == $byteback.dummy.complete.IntegerMax);
  _this := ?this;
  call java.lang.Object.$init$##(_this);
  return;
}

function byteback.dummy.complete.IntegerMax.array_is_not_null#int?#(~heap : Store, _a : Reference) returns (bool) { ~neq(_a, ~null) }

function byteback.dummy.complete.IntegerMax.max_in_range#int?#int#int#int#(~heap : Store, _a : Reference, _n : int, _start : int, _end : int) returns (bool) { (forall _$stack5 : int :: ~implies((~int.lte(_start, _$stack5) && ~int.lt(_$stack5, _end)), ~int.gte(_n, (~array.read(~heap, _a, _$stack5) : int)))) }

procedure byteback.dummy.complete.IntegerMax.max#int?#(?a : Reference where (~typeof(~heap, ?a) == ~array.type(~Primitive))) returns (~ret : int)
  requires ~neq(?a, ~null);
  requires ~int.gt(~lengthof(?a), 0);
  ensures byteback.dummy.complete.IntegerMax.max_in_range#int?#int#int#int#(~heap, ?a, ~ret, 0, ~lengthof(?a));
{
  var _t : int;
  var _i : int;
  var _a : Reference where (~typeof(~heap, _a) == ~array.type(~Primitive));
  _a := ?a;
  _t := (~array.read(~heap, _a, 0) : int);
  _i := 1;
  assert (~int.lte(0, _i) && ~int.lte(_i, ~lengthof(_a)));
  assert byteback.dummy.complete.IntegerMax.max_in_range#int?#int#int#int#(~heap, _a, _t, 0, _i);
label3:
  assume (~int.lte(0, _i) && ~int.lte(_i, ~lengthof(_a)));
  assume byteback.dummy.complete.IntegerMax.max_in_range#int?#int#int#int#(~heap, _a, _t, 0, _i);
  if ((_i >= ~lengthof(_a))) {
    goto label1;
  }
  if (((~array.read(~heap, _a, _i) : int) <= _t)) {
    goto label2;
  }
  _t := (~array.read(~heap, _a, _i) : int);
label2:
  _i := (_i + 1);
  assert (~int.lte(0, _i) && ~int.lte(_i, ~lengthof(_a)));
  assert byteback.dummy.complete.IntegerMax.max_in_range#int?#int#int#int#(~heap, _a, _t, 0, _i);
  goto label3;
label1:
  assume (~int.lte(0, _i) && ~int.lte(_i, ~lengthof(_a)));
  assume byteback.dummy.complete.IntegerMax.max_in_range#int?#int#int#int#(~heap, _a, _t, 0, _i);
  ~ret := _t;
  return;
}