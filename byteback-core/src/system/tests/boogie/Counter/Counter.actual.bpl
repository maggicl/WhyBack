type Reference;

const ~null : Reference;

type Field a;

type Store  = [Reference]<a>[Field a]a;

var ~heap : Store;

function ~read<a>(h : Store, r : Reference, f : Field a) returns (a) { h[r][f] }

function ~update<a>(h : Store, r : Reference, f : Field a, v : a) returns (Store) { h[r := h[r][f := v]] }

procedure ~new() returns (~ret : Reference);

type Type;

const ~Object.Type : Field Type;

axiom (forall h : Store, t : Type :: (h[~null][~Object.Type] <: t));

type Array a = Field [int]a;

const unique ~Array.bool : Array bool;

const unique ~Array.int : Array int;

const unique ~Array.real : Array real;

const unique ~Array.Reference : Array Reference;

const unique ~Array.length : Field int;

function ~lengthof(h : Store, r : Reference) returns (int) { ~read(h, r, ~Array.length) }

function ~get<a>(h : Store, r : Reference, f : Array a, i : int) returns (a) { ~read(h, r, f)[i] }

function ~insert<a>(h : Store, r : Reference, f : Array a, i : int, e : a) returns (Store) { ~update(h, r, f, ~read(h, r, f)[i := e]) }

procedure ~array(l : int) returns (~ret : Reference);

function ~cmp<t>(a : t, b : t) returns (int);

axiom (forall i : real, j : real :: ((i < j) <==> (~cmp(i, j) == -1)));

axiom (forall i : real, j : real :: ((i > j) <==> (~cmp(i, j) == 1)));

axiom (forall i : real, j : real :: ((i == j) <==> (~cmp(i, j) == 0)));

axiom (forall i : int, j : int :: ((i < j) <==> (~cmp(i, j) == -1)));

axiom (forall i : int, j : int :: ((i > j) <==> (~cmp(i, j) == 1)));

axiom (forall i : int, j : int :: ((i == j) <==> (~cmp(i, j) == 0)));

function ~and(~heap : Store, a : bool, b : bool) returns (bool) { (a && b) }

function ~or(~heap : Store, a : bool, b : bool) returns (bool) { (a || b) }

function ~implies(~heap : Store, a : bool, b : bool) returns (bool) { (a ==> b) }

function ~iff(~heap : Store, a : bool, b : bool) returns (bool) { (a <==> b) }

function ~lt<t>(~heap : Store, a : t, b : t) returns (bool);

axiom (forall ~heap : Store, a : real, b : real :: (~lt(~heap, a, b) <==> (a < b)));

axiom (forall ~heap : Store, a : int, b : int :: (~lt(~heap, a, b) <==> (a < b)));

function ~lte<t>(~heap : Store, a : t, b : t) returns (bool);

axiom (forall ~heap : Store, a : real, b : real :: (~lte(~heap, a, b) <==> (a <= b)));

axiom (forall ~heap : Store, a : int, b : int :: (~lte(~heap, a, b) <==> (a <= b)));

function ~gt<t>(~heap : Store, a : t, b : t) returns (bool);

axiom (forall ~heap : Store, a : real, b : real :: (~gt(~heap, a, b) <==> (a > b)));

axiom (forall ~heap : Store, a : int, b : int :: (~gt(~heap, a, b) <==> (a > b)));

function ~gte<t>(~heap : Store, a : t, b : t) returns (bool);

axiom (forall ~heap : Store, a : real, b : real :: (~gte(~heap, a, b) <==> (a >= b)));

axiom (forall ~heap : Store, a : int, b : int :: (~gte(~heap, a, b) <==> (a >= b)));

function ~eq<t>(~heap : Store, a : t, b : t) returns (bool) { (a == b) }

function ~int<a>(a) returns (int);

axiom (~int(false) == 0);

axiom (~int(true) == 1);

function ~real<a>(a) returns (real);

const byteback.dummy.condition.Counter.count : Field int;

procedure byteback.dummy.condition.Counter.main##() returns ()
  modifies ~heap;
{
  var $stack1 : Reference;
  call $stack1 := ~new();
  call byteback.dummy.condition.Counter.$init$##($stack1);
  assert ~eq(~heap, ~read(~heap, $stack1, byteback.dummy.condition.Counter.count), 0);
  call byteback.dummy.condition.Counter.increment##($stack1);
  assert ~eq(~heap, ~read(~heap, $stack1, byteback.dummy.condition.Counter.count), 1);
  call byteback.dummy.condition.Counter.countTo10##($stack1);
  assert ~eq(~heap, ~read(~heap, $stack1, byteback.dummy.condition.Counter.count), 11);
  call byteback.dummy.condition.Counter.countTo10Indirectly##($stack1);
  assert ~eq(~heap, ~read(~heap, $stack1, byteback.dummy.condition.Counter.count), 21);
  return;
}

procedure byteback.dummy.condition.Counter.$init$##(this : Reference) returns ()
  ensures ~eq(~heap, ~read(~heap, this, byteback.dummy.condition.Counter.count), 0);
  modifies ~heap;
{
  call java.lang.Object.$init$##(this);
  ~heap := ~update(~heap, this, byteback.dummy.condition.Counter.count, 0);
  return;
}

procedure byteback.dummy.condition.Counter.increment##(this : Reference) returns ()
  ensures ~eq(~heap, ~read(~heap, this, byteback.dummy.condition.Counter.count), (old(~read(~heap, this, byteback.dummy.condition.Counter.count)) + 1));
  modifies ~heap;
{
  ~heap := ~update(~heap, this, byteback.dummy.condition.Counter.count, (~read(~heap, this, byteback.dummy.condition.Counter.count) + 1));
  return;
}

procedure byteback.dummy.condition.Counter.countTo10##(this : Reference) returns ()
  ensures ~eq(~heap, ~read(~heap, this, byteback.dummy.condition.Counter.count), (old(~read(~heap, this, byteback.dummy.condition.Counter.count)) + 10));
  modifies ~heap;
{
  var i : int;
  i := 0;
label2:
  if ((i >= 10)) {
    goto label1;
  }
  ~heap := ~update(~heap, this, byteback.dummy.condition.Counter.count, (~read(~heap, this, byteback.dummy.condition.Counter.count) + 1));
  i := (i + 1);
  goto label2;
label1:
  return;
}

procedure byteback.dummy.condition.Counter.countTo10Indirectly##(this : Reference) returns ()
  ensures ~eq(~heap, ~read(~heap, this, byteback.dummy.condition.Counter.count), (old(~read(~heap, this, byteback.dummy.condition.Counter.count)) + 10));
  modifies ~heap;
{
  var i : int;
  i := 0;
label2:
  if ((i >= 10)) {
    goto label1;
  }
  call byteback.dummy.condition.Counter.increment##(this);
  i := (i + 1);
  goto label2;
label1:
  return;
}