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

// real -> int
// TODO

function eq(~heap: Store, a: int, b: int) returns (bool) { a == b }

function gt(~heap: Store, a: int, b: int) returns (bool) { a > b }

function ~real<a>(a) returns (real);

// int -> real
// TODO

procedure byteback.dummy.condition.Simple.returnsOne##() returns (~ret: int)
  ensures eq(~heap, ~ret, 1);
{
  ~ret := 1;
  return;
}

procedure byteback.dummy.condition.Simple.identity#int#(a: int) returns (~ret: int)
  ensures eq(~heap, ~ret, a);
{
  ~ret := a;
  return;
}

procedure byteback.dummy.condition.Simple.singleAssertion##()
{
  assert true;
  return;
}

procedure byteback.dummy.condition.Simple.singleAssumption##()
{
  assume true;
  return;
}

procedure byteback.dummy.condition.Simple.wrongAssumption1##() returns ()
{
  assume false;
  return;
}

procedure byteback.dummy.condition.Simple.wrongAssumption2##() returns ()
{
  var a: bool;
  a := false;
  assume a;
  return;
}

procedure byteback.dummy.condition.Simple.fibonacci#int#(m: int) returns (~ret: int)
  requires gt(~heap, m, 0);
{
  var c: int;
  var a: int;
  var b: int;
  var i: int;
  a := 0;
  b := 1;
  i := 0;

label2:
  if (i >= m) {
    goto label1;
  }

  c := a + b;
  a := b;
  b := c;
  i := i + 1;
  goto label2;

label1:
  ~ret := a;

  return;
}

procedure byteback.dummy.condition.Simple.overloadedConditions##() returns (~ret: int)
  ensures true;
{
  ~ret := 0;
  return;
}

procedure byteback.dummy.condition.Simple.result##() returns (~ret: int)
  ensures eq(~heap, ~ret, 1);
{
  ~ret := 1;
  return;
}

procedure byteback.dummy.condition.Simple.returnsResult##() returns (~ret: int)
  ensures eq(~heap, ~ret, 1);
{
  var $stack0: int;
  call $stack0 := byteback.dummy.condition.Simple.result##();
  ~ret := $stack0;
  return;
}

procedure byteback.dummy.condition.Simple.loopAssertion##() returns ()
{
  var c: bool;
  var i: int;
  var $stack4: bool;

  c := false;
  i := 0;

label4:
  if (i >= 10) {
    goto label1;
  }

  if (~int(c) != 0) {
    goto label2;
  }

  $stack4 := true;

  goto label3;

label2:
  $stack4 := false;

label3:
  assert $stack4;

  i := (i + 1);

  goto label4;

label1:
  return;
}

procedure byteback.dummy.condition.Simple.loopInvariant##() returns ()
{
  return;
}

procedure byteback.dummy.condition.Simple.assignIf#int#(b: int) returns ()
{
  return;
}
