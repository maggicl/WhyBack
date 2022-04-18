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
