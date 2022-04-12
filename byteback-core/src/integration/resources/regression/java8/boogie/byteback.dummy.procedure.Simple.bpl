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

function ~real<a>(a) returns (real);

// int -> real
// TODO

procedure byteback.dummy.procedure.Simple.empty##() returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.singleAssignment##() returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.doubleAssignment##() returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.emptyWhile##() returns ()
{
  var a: bool;
  a := false;

label2:
  if (~int(a) == 0) {
    goto label1;
  }

  goto label2;

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.emptyDoWhile##() returns ()
{
  var a: bool;
  a := false;

label1:
  if (~int(a) != 0) {
    goto label1;
  }

  return;
}

procedure byteback.dummy.procedure.Simple.emptyIf##() returns ()
{
  var a: bool;
  a := false;

  if (~int(a) == 0) {
    goto label1;
  }

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignIf##() returns ()
{
  var a: bool;
  a := false;

  if (~int(a) != 0) {
    goto label1;
  }

  a := true;
label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignParameter#int#(a: int) returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.emptyFor##() returns ()
{
  var i: int;
  i := 0;

label2:
  if (i >= 10) {
    goto label1;
  }

  i := i + 1;
  goto label2;

label1:
  return;
}


procedure byteback.dummy.procedure.Simple.returnsNull##() returns (~ret: Reference)  
{
  ~ret := ~null;
  return;
}

procedure byteback.dummy.procedure.Simple.realCondition##() returns ()
{
  var $stack2 : int;
  $stack2 := ~cmp(3.14, 2.72);

  if ($stack2 >= 0) {
    goto label1;
  }

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignsProcedureResult##() returns ()
{
  var a: Reference;

  call a := byteback.dummy.procedure.Simple.returnsNull##();

  return;
}

procedure byteback.dummy.procedure.Simple.assignsProcedureResultTwice##() returns ()
{
  var a: Reference;

  call a := byteback.dummy.procedure.Simple.returnsNull##();
  call a := byteback.dummy.procedure.Simple.returnsNull##();

  return;
}

procedure byteback.dummy.procedure.Simple.callsVoidProcedure##() returns ()
{
  call byteback.dummy.procedure.Simple.emptyWhile##();
  return;
}

procedure byteback.dummy.procedure.Simple.callsInForLoop##() returns ()
{
  var i: int;
  i := 0;

label2:
  if (i >= 10) {
    goto label1;
  }

  call byteback.dummy.procedure.Simple.emptyFor##();
  i := (i + 1);
  goto label2;

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.callsPureEq##() returns ()
{
  return;
}
