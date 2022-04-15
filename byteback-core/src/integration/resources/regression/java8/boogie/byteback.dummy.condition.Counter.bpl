// -------------------------------------------------------------------
// Heap model
// -------------------------------------------------------------------
type Reference;

const ~null: Reference;

type Field a;
type Store = [Reference]<a>[Field a]a;

var ~heap: Store;

function ~read<a>(h: Store, r: Reference, f: Field a) returns (a)
{ h[r][f] }

function ~update<a>(h: Store, r: Reference, f: Field a, v: a) returns (Store)
{ h[r := h[r][f := v]] }

// -------------------------------------------------------------------
// Binary operators
// -------------------------------------------------------------------
function ~cmp<a>(a, a) returns (int);

function ifgt(a: int) returns (int) { if (a > 0) then 0 else 1 }

axiom (forall i: int, j: int :: i < j ==> ~cmp(i, j) == -1);
axiom (forall i: int, j: int :: i > j ==> ~cmp(i, j) == 1);
axiom (forall i: int, j: int :: i == j ==> ~cmp(i, j) == 0);

axiom (forall i: real, j: real :: i < j ==> ~cmp(i, j) == -1);
axiom (forall i: real, j: real :: i > j ==> ~cmp(i, j) == 1);
axiom (forall i: real, j: real :: i == j ==> ~cmp(i, j) == 0);

// -------------------------------------------------------------------
// Casting operators
// -------------------------------------------------------------------

// Casting between primitive types
function ~int<a>(a) returns (int);

// bool -> int
axiom ~int(false) == 0;
axiom ~int(true) == 1;

procedure ~new() returns (~ret: Reference);

function eq(~heap: Store, a: int, b: int) returns (bool) { a == b }

function implies(~heap: Store, a: bool, b: bool) returns (bool) { a ==> b }

function not(~heap: Store, a: bool) returns (bool) { !a }

// real -> int
// TODO
function ~real<a>(a) returns (real);

const byteback.dummy.condition.Counter.count: Field int;

procedure byteback.dummy.condition.Counter.main##() returns ()
  modifies ~heap;
{
  var $stack1: Reference;
  call $stack1 := ~new();
  call byteback.dummy.condition.Counter.$init$##($stack1);
  assert eq(~heap, ~read(~heap, $stack1, byteback.dummy.condition.Counter.count), 0);
  call byteback.dummy.condition.Counter.increment##($stack1);
  assert eq(~heap, ~read(~heap, $stack1, byteback.dummy.condition.Counter.count), 1);
  call byteback.dummy.condition.Counter.countTo10##($stack1);
  assert eq(~heap, ~read(~heap, $stack1, byteback.dummy.condition.Counter.count), 11);
  call byteback.dummy.condition.Counter.countTo10Indirectly##($stack1);
  assert eq(~heap, ~read(~heap, $stack1, byteback.dummy.condition.Counter.count), 21);
  return;
}

procedure byteback.dummy.condition.Counter.$init$##(this: Reference) returns ()
  ensures eq(~heap, ~read(~heap, this, byteback.dummy.condition.Counter.count), 0);
  modifies ~heap;
{
  call java.lang.Object.$init$##(this);
  ~heap := ~update(~heap, this, byteback.dummy.condition.Counter.count, 0);
  return;
} 

procedure byteback.dummy.condition.Counter.increment##(this : Reference) returns ()
  ensures eq(~heap, ~read(~heap, this, byteback.dummy.condition.Counter.count), old(~read(~heap, this, byteback.dummy.condition.Counter.count)) + 1);
  modifies ~heap;
{
  ~heap := ~update(~heap, this, byteback.dummy.condition.Counter.count,
    ~read(~heap, this, byteback.dummy.condition.Counter.count) + 1);
  return;
}

procedure byteback.dummy.condition.Counter.countTo10##(this : Reference) returns ()
  ensures eq(~heap, ~read(~heap, this, byteback.dummy.condition.Counter.count),
    old(~read(~heap, this, byteback.dummy.condition.Counter.count)) + 10);
  modifies ~heap;
{
  var i: int;
  i := 0;

label2:
  if (i >= 10) {
    goto label1;
  }

  ~heap := ~update(~heap, this, byteback.dummy.condition.Counter.count,
    ~read(~heap, this, byteback.dummy.condition.Counter.count) + 1);
  i := i + 1;
  goto label2;

label1:
  return;
}

procedure byteback.dummy.condition.Counter.countTo10Indirectly##(this : Reference) returns ()
  ensures eq(~heap, ~read(~heap, this, byteback.dummy.condition.Counter.count),
    old(~read(~heap, this, byteback.dummy.condition.Counter.count)) + 10);
  modifies ~heap;
{
  var i: int;
  i := 0;

label2:
  if (i >= 10) {
    goto label1;
  }

  call byteback.dummy.condition.Counter.increment##(this);
  i := i + 1;
  goto label2;

label1:
  return;
}
