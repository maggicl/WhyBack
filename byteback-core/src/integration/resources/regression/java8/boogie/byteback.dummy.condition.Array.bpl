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

procedure ~new() returns (~ret: Reference);

// -------------------------------------------------------------------
// Array model
// -------------------------------------------------------------------
type Array a = Field [int]a;

const unique ~Array.bool: Array bool;

const unique ~Array.int: Array int;

const unique ~Array.real: Array real;

const unique ~Array.Reference: Array Reference;

const unique ~Array.length: Field int;

function ~lengthof(h: Store, r: Reference) returns (int)
{
  ~read(h, r, ~Array.length)
}

function ~get<a>(h: Store, r: Reference, f: Array a, i: int) returns (a)
{
  ~read(h, r, f)[i]
}

function ~insert<a>(h: Store, r: Reference, f: Array a, i: int, e: a) returns (Store)
{
  ~update(h, r, f, ~read(h, r, f)[i := e])
}

procedure ~array(l: int) returns (~ret: Reference);
  modifies ~heap;
  ensures ~read(~heap, ~ret, ~Array.length) == l;

// -------------------------------------------------------------------
// Binary operators
// -------------------------------------------------------------------
function ~cmp<t>(a: t, b: t) returns (int);

axiom (forall i: real, j: real :: i < j <==> ~cmp(i, j) == -1);
axiom (forall i: real, j: real :: i > j <==> ~cmp(i, j) == 1);
axiom (forall i: real, j: real :: i == j <==> ~cmp(i, j) == 0);

axiom (forall i: int, j: int :: i < j <==> ~cmp(i, j) == -1);
axiom (forall i: int, j: int :: i > j <==> ~cmp(i, j) == 1);
axiom (forall i: int, j: int :: i == j <==> ~cmp(i, j) == 0);

// -------------------------------------------------------------------
// Prelude definitions
// -------------------------------------------------------------------
function ~and(~heap: Store, a: bool, b: bool) returns (bool)
{ a && b }

function ~or(~heap: Store, a: bool, b: bool) returns (bool)
{ a || b }

function ~implies(~heap: Store, a: bool, b: bool) returns (bool)
{ a ==> b }

function ~iff(~heap: Store, a: bool, b: bool) returns (bool)
{ a <==> b }

function ~lt<t>(~heap: Store, a: t, b:  t) returns (bool);
axiom (forall ~heap: Store, a: real, b: real :: ~lt(~heap, a, b) <==> a < b);
axiom (forall ~heap: Store, a: int, b: int :: ~lt(~heap, a, b) <==> a < b);

function ~lte<t>(~heap: Store, a: t, b: t) returns (bool);
axiom (forall ~heap: Store, a: real, b: real :: ~lte(~heap, a, b) <==> a <= b);
axiom (forall ~heap: Store, a: int, b: int :: ~lte(~heap, a, b) <==> a <= b);

function ~gt<t>(~heap: Store, a: t, b: t) returns (bool);
axiom (forall ~heap: Store, a: real, b: real :: ~gt(~heap, a, b) <==> a > b);
axiom (forall ~heap: Store, a: int, b: int :: ~gt(~heap, a, b) <==> a > b);

function ~gte<t>(~heap: Store, a: t, b: t) returns (bool);
axiom (forall ~heap: Store, a: real, b: real :: ~gte(~heap, a, b) <==> a >= b);
axiom (forall ~heap: Store, a: int, b: int :: ~gte(~heap, a, b) <==> a >= b);

function ~eq<t>(~heap: Store, a: t, b: t) returns (bool)
{ a == b }

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

procedure byteback.dummy.condition.Array.sum#int$#(as: Reference) returns (~ret: int)
  ensures ~implies(~heap,
    (forall index : int :: ~implies(~heap,
                        ~and(~heap,
                         ~lt(~heap, index, ~lengthof(~heap, as)),
                         ~gte(~heap, index, 0)),
                        ~gte(~heap, ~get(~heap, as, ~Array.int, index), 0))),
    ~gte(~heap, ~ret, 0));
{
  var c: int;
  var l4: int;
  c := 0;
  l4 := 0;
label2:

  if (l4 >= ~lengthof(~heap, as)) {
    goto label1;
  }

  c := (c + ~get(~heap, as, ~Array.int, l4));
  l4 := (l4 + 1);

  goto label2;

label1:

  ~ret := c;

  return;
}

procedure byteback.dummy.condition.Array.assignsLastElement#int$#(as: Reference) returns ()
  ensures ~eq(~heap, ~get(~heap, as, ~Array.int, ~lengthof(~heap, as) - 1), 1);
{
  ~heap := ~insert(~heap, as, ~Array.int, ~lengthof(~heap, as) - 1, 1);
  return;
}
