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

function eq(~heap: Store, a: int, b: int) returns (bool) { a == b }

function implies(~heap: Store, a: bool, b: bool) returns (bool) { a ==> b }

function not(~heap: Store, a: bool) returns (bool) { !a }

// real -> int
// TODO

function ~real<a>(a) returns (real);

// int -> real
// TODO

procedure byteback.dummy.condition.Boolean.shortCircuitingAnd#boolean#boolean#(a: bool, b: bool) returns (~ret: bool)
  ensures implies(~heap, a && b, ~ret);
{
  var $stack2: bool;

  if (~int(a) == 0) {
    goto label2;
  }

  if (~int(b) == 0) {
    goto label2;
  }

  $stack2 := true;
  goto label3;

label2:
  $stack2 := false;

label3:
  ~ret := $stack2;

  return;
}

procedure byteback.dummy.condition.Boolean.shortCircuitingOr#boolean#boolean#(a: bool, b: bool) returns (~ret: bool)
  ensures implies(~heap, a || b, ~ret);
{
  var $stack2: bool;

  if (~int(a) != 0) {
    goto label1;
  }

  if (~int(b) == 0) {
    goto label2;
  }

label1:
  $stack2 := true;
  goto label3;

label2:
  $stack2 := false;

label3:
  ~ret := $stack2;

  return;
}

procedure byteback.dummy.condition.Boolean.shortCircuitingNot#boolean#(a: bool) returns (~ret: bool)
  ensures implies(~heap, a, not(~heap, ~ret));
{
  var $stack1: bool;

  if (~int(a) != 0) {
    goto label1;
  }

  $stack1 := true;
  goto label2;

label1:
  $stack1 := false;

label2:
  ~ret := $stack1;

  return;
}
