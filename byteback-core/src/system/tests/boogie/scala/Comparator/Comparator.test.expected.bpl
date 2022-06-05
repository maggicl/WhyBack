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

const unique $byteback.dummy.complete.Comparator : Type;

procedure byteback.dummy.complete.Comparator.main##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.Comparator)) returns ()
{
  var $$stack5 : Reference where (~typeof(~heap, $$stack5) == $byteback.dummy.complete.LessThanComparator);
  var $ltComparator : Reference where (~typeof(~heap, $ltComparator) == $byteback.dummy.complete.LessThanComparator);
  var $$stack6 : Reference where (~typeof(~heap, $$stack6) == $byteback.dummy.complete.GreaterThanComparator);
  var $gtComparator : Reference where (~typeof(~heap, $gtComparator) == $byteback.dummy.complete.GreaterThanComparator);
  var $a : bool;
  var $b : bool;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.Comparator);
  $this := ?this;
  call $$stack5 := ~new($byteback.dummy.complete.LessThanComparator);
  call byteback.dummy.complete.LessThanComparator.$init$##($$stack5);
  $ltComparator := $$stack5;
  call $$stack6 := ~new($byteback.dummy.complete.GreaterThanComparator);
  call byteback.dummy.complete.GreaterThanComparator.$init$##($$stack6);
  $gtComparator := $$stack6;
  call $a := byteback.dummy.complete.LessThanComparator.compare#int#int#($$stack5, 2, 1);
  assert ~not($a);
  call $b := byteback.dummy.complete.GreaterThanComparator.compare#int#int#($$stack6, 1, 2);
  assert ~not($b);
  return;
}

procedure byteback.dummy.complete.Comparator.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.Comparator)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.Comparator);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

const unique $byteback.dummy.complete.LessThanComparator : Type;

procedure byteback.dummy.complete.LessThanComparator.compare_less_than#int#int#boolean#(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.LessThanComparator), ?a : int, ?b : int, ?returns : bool) returns (~ret : bool)
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.LessThanComparator);
  var $a : int;
  var $b : int;
  var $returns : bool;
  $returns := ?returns;
  $b := ?b;
  $a := ?a;
  $this := ?this;
  ~ret := ~implies($returns, ~int.lt($a, $b));
  return;
}

procedure byteback.dummy.complete.LessThanComparator.compare#int#int#(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.LessThanComparator), ?a : int, ?b : int) returns (~ret : bool)
  ensures ~implies(~ret, ~int.lt(?a, ?b));
{
  var $$stack3 : bool;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.LessThanComparator);
  var $a : int;
  var $b : int;
  $b := ?b;
  $a := ?a;
  $this := ?this;
  if (($a >= $b)) {
    goto label1;
  }
  $$stack3 := true;
  goto label2;
label1:
  $$stack3 := false;
label2:
  ~ret := $$stack3;
  return;
}

procedure byteback.dummy.complete.LessThanComparator.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.LessThanComparator)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.LessThanComparator);
  $this := ?this;
  call byteback.dummy.complete.DefaultComparator.$init$##($this);
  return;
}

const unique $byteback.dummy.complete.GreaterThanComparator : Type;

procedure byteback.dummy.complete.GreaterThanComparator.compare_greater_than#int#int#boolean#(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.GreaterThanComparator), ?a : int, ?b : int, ?returns : bool) returns (~ret : bool)
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.GreaterThanComparator);
  var $a : int;
  var $b : int;
  var $returns : bool;
  $returns := ?returns;
  $b := ?b;
  $a := ?a;
  $this := ?this;
  ~ret := ~implies($returns, ~int.gt($a, $b));
  return;
}

procedure byteback.dummy.complete.GreaterThanComparator.compare#int#int#(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.GreaterThanComparator), ?a : int, ?b : int) returns (~ret : bool)
  ensures ~implies(~ret, ~int.gt(?a, ?b));
{
  var $$stack3 : bool;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.GreaterThanComparator);
  var $a : int;
  var $b : int;
  $b := ?b;
  $a := ?a;
  $this := ?this;
  if (($a <= $b)) {
    goto label1;
  }
  $$stack3 := true;
  goto label2;
label1:
  $$stack3 := false;
label2:
  ~ret := $$stack3;
  return;
}

procedure byteback.dummy.complete.GreaterThanComparator.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.GreaterThanComparator)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.GreaterThanComparator);
  $this := ?this;
  call byteback.dummy.complete.DefaultComparator.$init$##($this);
  return;
}

const unique $byteback.dummy.complete.DefaultComparator : Type;

procedure byteback.dummy.complete.DefaultComparator.compare_default#int#int#boolean#(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.DefaultComparator), ?a : int, ?b : int, ?returns : bool) returns (~ret : bool)
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.DefaultComparator);
  var $returns : bool;
  $returns := ?returns;
  $this := ?this;
  ~ret := $returns;
  return;
}

procedure byteback.dummy.complete.DefaultComparator.compare#int#int#(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.DefaultComparator), ?a : int, ?b : int) returns (~ret : bool)
  ensures ~ret;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.DefaultComparator);
  $this := ?this;
  ~ret := true;
  return;
}

procedure byteback.dummy.complete.DefaultComparator.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.DefaultComparator)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.DefaultComparator);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}