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

const unique $byteback.dummy.complete.Counter : Type;

const unique $byteback.dummy.complete.Counter.count : Field (int);

procedure byteback.dummy.complete.Counter.count##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.Counter)) returns (~ret : int)
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.Counter);
  $this := ?this;
  ~ret := ~heap.read(~heap, $this, $byteback.dummy.complete.Counter.count);
  return;
}

procedure byteback.dummy.complete.Counter.count_$eq#int#(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.Counter), ?x$1 : int) returns ()
  modifies ~heap;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.Counter);
  var $x$1 : int;
  $x$1 := ?x$1;
  $this := ?this;
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.complete.Counter.count, $x$1);
  return;
}

procedure byteback.dummy.complete.Counter.increment##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.Counter)) returns ()
  ensures ~eq(byteback.dummy.complete.Counter.count##(~heap, ?this), (old(byteback.dummy.complete.Counter.count##(~heap, ?this)) + 1));
  modifies ~heap;
{
  var $$stack1 : int;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.Counter);
  $this := ?this;
  call $$stack1 := byteback.dummy.complete.Counter.count##($this);
  call byteback.dummy.complete.Counter.count_$eq#int#($this, ($$stack1 + 1));
  return;
}

procedure byteback.dummy.complete.Counter.incrementTo10##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.Counter)) returns ()
  ensures ~eq(byteback.dummy.complete.Counter.count##(~heap, ?this), (old(byteback.dummy.complete.Counter.count##(~heap, ?this)) + 10));
  modifies ~heap;
{
  var $old_count : int;
  var $$stack7 : Reference where (~typeof(~heap, $$stack7) == $scala.Tuple2$mcII$sp);
  var $$stack8 : int;
  var $i : int;
  var $$stack12 : bool;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.Counter);
  $this := ?this;
  $i := 0;
  call $old_count := byteback.dummy.complete.Counter.count##($this);
  assert (~int.lte(0, $i) && ~int.lte($i, 10));
  assert $$stack12;
label4:
  assume (~int.lte(0, $i) && ~int.lte($i, 10));
  assume $$stack12;
  if (($i >= 10)) {
    goto label1;
  }
  call $$stack7 := ~new($scala.Tuple2$mcII$sp);
  call $$stack8 := byteback.dummy.complete.Counter.count##($this);
  call scala.Tuple2$mcII$sp.$init$#int#int#($$stack7, $$stack8, ($old_count + $i));
  if (($this != $$stack7)) {
    goto label2;
  }
  $$stack12 := true;
  goto label3;
label2:
  $$stack12 := false;
label3:
  call byteback.dummy.complete.Counter.increment##($this);
  $i := ($i + 1);
  assert (~int.lte(0, $i) && ~int.lte($i, 10));
  assert $$stack12;
  goto label4;
label1:
  assume (~int.lte(0, $i) && ~int.lte($i, 10));
  assume $$stack12;
  return;
}

procedure byteback.dummy.complete.Counter.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.complete.Counter)) returns ()
  modifies ~heap;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.complete.Counter);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.complete.Counter.count, 0);
  return;
}