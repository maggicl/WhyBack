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

const unique $byteback.dummy.complete.IntegerSelectionSort : Type;

procedure byteback.dummy.complete.IntegerSelectionSort.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.IntegerSelectionSort)) returns ()
{
  var _this : Reference where (~typeof(~heap, _this) == $byteback.dummy.complete.IntegerSelectionSort);
  _this := ?this;
  call java.lang.Object.$init$##(_this);
  return;
}

function byteback.dummy.complete.IntegerSelectionSort.bounded_index#int?#int#(~heap : Store, _a : Reference, _i : int) returns (bool) { (~int.lte(0, _i) && ~int.lt(_i, ~lengthof(_a))) }

function byteback.dummy.complete.IntegerSelectionSort.is_minimum#int?#int#int#int#(~heap : Store, _a : Reference, _i : int, _j : int, _m : int) returns (bool) { (forall _$stack5 : int :: ~implies((~int.lte(_i, _$stack5) && ~int.lt(_$stack5, _j)), ~int.gte((~array.read(~heap, _a, _$stack5) : int), (~array.read(~heap, _a, _m) : int)))) }

function byteback.dummy.complete.IntegerSelectionSort.is_minimum#int?#int#int#(~heap : Store, _a : Reference, _i : int, _m : int) returns (bool) { byteback.dummy.complete.IntegerSelectionSort.is_minimum#int?#int#int#int#(~heap, _a, _i, ~lengthof(_a), _m) }

procedure byteback.dummy.complete.IntegerSelectionSort.minimum#int?#int#(?a : Reference where (~typeof(~heap, ?a) == ~array.type(~Primitive)), ?i : int) returns (~ret : int)
  requires (~int.lte(0, ?i) && ~int.lt(?i, ~lengthof(?a)));
  ensures byteback.dummy.complete.IntegerSelectionSort.is_minimum#int?#int#int#int#(~heap, ?a, ?i, ~lengthof(?a), ~ret);
  ensures byteback.dummy.complete.IntegerSelectionSort.bounded_index#int?#int#(~heap, ?a, ~ret);
{
  var _m : int;
  var _j : int;
  var _a : Reference where (~typeof(~heap, _a) == ~array.type(~Primitive));
  var _i : int;
  _a := ?a;
  _i := ?i;
  _m := _i;
  _j := _i;
  assert (~int.lte(_i, _j) && ~int.lte(_j, ~lengthof(_a)));
  assert (~int.lte(_i, _m) && ~int.lt(_m, ~lengthof(_a)));
  assert byteback.dummy.complete.IntegerSelectionSort.is_minimum#int?#int#int#int#(~heap, _a, _i, _j, _m);
label3:
  assume (~int.lte(_i, _j) && ~int.lte(_j, ~lengthof(_a)));
  assume (~int.lte(_i, _m) && ~int.lt(_m, ~lengthof(_a)));
  assume byteback.dummy.complete.IntegerSelectionSort.is_minimum#int?#int#int#int#(~heap, _a, _i, _j, _m);
  if ((_j >= ~lengthof(_a))) {
    goto label1;
  }
  if (((~array.read(~heap, _a, _j) : int) >= (~array.read(~heap, _a, _m) : int))) {
    goto label2;
  }
  _m := _j;
label2:
  _j := (_j + 1);
  assert (~int.lte(_i, _j) && ~int.lte(_j, ~lengthof(_a)));
  assert (~int.lte(_i, _m) && ~int.lt(_m, ~lengthof(_a)));
  assert byteback.dummy.complete.IntegerSelectionSort.is_minimum#int?#int#int#int#(~heap, _a, _i, _j, _m);
  goto label3;
label1:
  assume (~int.lte(_i, _j) && ~int.lte(_j, ~lengthof(_a)));
  assume (~int.lte(_i, _m) && ~int.lt(_m, ~lengthof(_a)));
  assume byteback.dummy.complete.IntegerSelectionSort.is_minimum#int?#int#int#int#(~heap, _a, _i, _j, _m);
  ~ret := _m;
  return;
}

function byteback.dummy.complete.IntegerSelectionSort.sorted#int?#int#int#(~heap : Store, _a : Reference, _i : int, _j : int) returns (bool) { (forall _$stack4 : int :: ~implies((~int.lt(_i, _$stack4) && ~int.lt(_$stack4, _j)), ~int.lte((~array.read(~heap, _a, (_$stack4 - 1)) : int), (~array.read(~heap, _a, _$stack4) : int)))) }

function byteback.dummy.complete.IntegerSelectionSort.partitioned#int?#int#(~heap : Store, _a : Reference, _c : int) returns (bool) { (forall _$stack4 : int :: (forall _$stack5 : int :: ~implies((((~int.lte(0, _$stack4) && ~int.lt(_$stack4, _c)) && ~int.lte(_c, _$stack5)) && ~int.lt(_$stack5, ~lengthof(_a))), ~int.lte((~array.read(~heap, _a, _$stack4) : int), (~array.read(~heap, _a, _$stack5) : int))))) }

procedure byteback.dummy.complete.IntegerSelectionSort.sort#int?#(?a : Reference where (~typeof(~heap, ?a) == ~array.type(~Primitive))) returns ()
  requires ~neq(?a, ~null);
  requires ~int.gt(~lengthof(?a), 1);
  ensures byteback.dummy.complete.IntegerSelectionSort.sorted#int?#int#int#(~heap, ?a, 0, ~lengthof(?a));
  modifies ~heap;
{
  var ~sym1 : int;
  var _$stack11 : int;
  var _y : int;
  var _c : int;
  var _a : Reference where (~typeof(~heap, _a) == ~array.type(~Primitive));
  _a := ?a;
  _c := 0;
  assert (~int.lte(0, _c) && ~int.lte(_c, ~lengthof(_a)));
  assert byteback.dummy.complete.IntegerSelectionSort.partitioned#int?#int#(~heap, _a, _c);
  assert byteback.dummy.complete.IntegerSelectionSort.sorted#int?#int#int#(~heap, _a, 0, _c);
label2:
  assume (~int.lte(0, _c) && ~int.lte(_c, ~lengthof(_a)));
  assume byteback.dummy.complete.IntegerSelectionSort.partitioned#int?#int#(~heap, _a, _c);
  assume byteback.dummy.complete.IntegerSelectionSort.sorted#int?#int#int#(~heap, _a, 0, _c);
  if ((_c >= ~lengthof(_a))) {
    goto label1;
  }
  call ~sym1 := byteback.dummy.complete.IntegerSelectionSort.minimum#int?#int#(_a, _c);
  _$stack11 := ~sym1;
  _y := (~array.read(~heap, _a, _c) : int);
  ~heap := ~array.update(~heap, _a, _$stack11, (~array.read(~heap, _a, _c) : int));
  ~heap := ~array.update(~heap, _a, _c, _y);
  _c := (_c + 1);
  assert (~int.lte(0, _c) && ~int.lte(_c, ~lengthof(_a)));
  assert byteback.dummy.complete.IntegerSelectionSort.partitioned#int?#int#(~heap, _a, _c);
  assert byteback.dummy.complete.IntegerSelectionSort.sorted#int?#int#int#(~heap, _a, 0, _c);
  goto label2;
label1:
  assume (~int.lte(0, _c) && ~int.lte(_c, ~lengthof(_a)));
  assume byteback.dummy.complete.IntegerSelectionSort.partitioned#int?#int#(~heap, _a, _c);
  assume byteback.dummy.complete.IntegerSelectionSort.sorted#int?#int#int#(~heap, _a, 0, _c);
  return;
}