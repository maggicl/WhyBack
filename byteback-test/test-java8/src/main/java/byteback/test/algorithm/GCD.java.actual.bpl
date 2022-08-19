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
  ensures (~ret != ~null);
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
  ensures (~ret != ~null);
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

const unique $byteback.test.algorithm.GCD : Type;

procedure byteback.test.algorithm.GCD.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.test.algorithm.GCD)) returns ()
{
  var _this : Reference where (~typeof(~heap, _this) == $byteback.test.algorithm.GCD);
  _this := ?this;
  call java.lang.Object.$init$##(_this);
  return;
}

function byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap : Store, _a : int, _b : int) returns (int) { if ~eq(_a, _b) then _a else if ~int.gt(_a, _b) then byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, (_a - _b), _b) else byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _a, (_b - _a)) }

procedure byteback.test.algorithm.GCD.gcd#int#int#(?a : int, ?b : int) returns (~ret : int)
  requires (~int.gt(?a, 0) && ~int.gt(?b, 0));
  ensures ~eq(~ret, byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, ?a, ?b));
{
  var _r : int;
  var _x : int;
  var _a : int;
  var _b : int;
  _a := ?a;
  _b := ?b;
  _r := _a;
  _x := _b;
  assert (~int.gt(_r, 0) && ~int.gt(_x, 0));
  assert ~eq(byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _r, _x), byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _a, _b));
label4:
  assume (~int.gt(_r, 0) && ~int.gt(_x, 0));
  assume ~eq(byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _r, _x), byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _a, _b));
  if ((_r == _x)) {
    goto label1;
  }
  if ((_r <= _x)) {
    goto label2;
  }
  _r := (_r - _x);
  goto label4;
label2:
  _x := (_x - _r);
  assert (~int.gt(_r, 0) && ~int.gt(_x, 0));
  assert ~eq(byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _r, _x), byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _a, _b));
  goto label4;
label1:
  assume (~int.gt(_r, 0) && ~int.gt(_x, 0));
  assume ~eq(byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _r, _x), byteback.test.algorithm.GCD.gcd_recursive#int#int#(~heap, _a, _b));
  ~ret := _r;
  return;
}