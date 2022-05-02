// -------------------------------------------------------------------
// Heap model
// -------------------------------------------------------------------
type Reference;

const unique ~null: Reference;

type Field a;

type Store = [Reference]<a>[Field a]a;

var ~heap: Store;

function {:inline} ~read<a>(h: Store, r: Reference, f: Field a) returns (a)
{
	h[r][f]
}

function {:inline} ~update<a>(h: Store, r: Reference, f: Field a, v: a) returns (Store)
{
	h[r := h[r][f := v]]
}

function ~succ(h1: Store, h2: Store) returns (bool);

axiom (forall <a> h: Store, r: Reference, f: Field a, v: a :: { ~update(h, r, f, v) } ~succ(h, ~update(h, r, f, v)));

axiom (forall h1: Store, h2: Store, h3: Store :: { ~succ(h1, h2), ~succ(h2, h3) } ~succ(h1, h2) && ~succ(h2, h3) ==> ~succ(h1, h3));

function ~allocated(Reference) returns (bool);

procedure ~new(t: Type) returns (~ret: Reference);
	ensures ~typeof(~heap, ~ret) == t;
	ensures ~allocated(~ret);

// -------------------------------------------------------------------
// Type model
// -------------------------------------------------------------------
type Type = Reference;

const ~Object.Type: Field Type;

function ~typeof(h: Store, r: Reference) returns (Type)
{
  ~read(h, r, ~Object.Type)
}

function ~instanceof(h: Store, r: Reference, t: Type) returns (bool)
{
	~typeof(h, r) == t
}

axiom (forall h: Store, t: Type :: ~typeof(h, ~null) <: t);

// -------------------------------------------------------------------
// Array model
// -------------------------------------------------------------------
type Box;

function ~box<a>(a) returns (Box);

function ~unbox<a>(Box) returns (a);

axiom (forall <a> x : a :: { ~box(x) } ~unbox(~box(x)) == x);

function ~element(int) returns (Field Box);

function ~element_inverse<a>(Field a) returns (int);

axiom (forall i: int :: { ~element(i) } ~element_inverse(~element(i)) == i);

function ~lengthof(r: Reference) returns (int);

axiom (forall r: Reference :: ~lengthof(r) >= 0);

axiom (forall <a> h1: Store, h2: Store, r: Reference, i: int :: 0 <= i && i < ~lengthof(r) ==> ~read(h1, r, ~element(i)) == ~read(h2, r, ~element(i)));

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
function ~and(a: bool, b: bool) returns (bool)
{
  a && b
}

function ~or(a: bool, b: bool) returns (bool)
{
  a || b
}

function ~implies(a: bool, b: bool) returns (bool)
{
  a ==> b
}

function ~iff(a: bool, b: bool) returns (bool)
{
  a <==> b
}

function ~eq<t>(a: t, b: t) returns (bool)
{
	a == b
}

function ~neq<t>(a: t, b: t) returns (bool)
{
	a != b
}

function ~int.lt(a: int, b: int) returns (bool)
{
	a < b
}

function ~real.lt(a: real, b: real) returns (bool)
{
	a < b
}

function ~int.lte(a: int, b: int) returns (bool)
{
	a <= b
}

function ~real.lte(a: real, b: real) returns (bool)
{
	a <= b
}

function ~int.gt(a: int, b: int) returns (bool)
{
	a > b
}

function ~real.gt(a: real, b: real) returns (bool)
{
	a > b
}

function ~int.gte(a: int, b: int) returns (bool)
{
	a >= b
}

function ~real.gte(a: real, b: real) returns (bool)
{
	a >= b
}

// -------------------------------------------------------------------
// Casting operators
// -------------------------------------------------------------------

// Casting between primitive types
function ~int<a>(a) returns (int);

// bool -> int
axiom ~int(false) == 0;
axiom ~int(true) == 1;

// -------------------------------------------------------------------
// Missing definitions
// -------------------------------------------------------------------
procedure java.lang.Object.$init$##(this: Reference) returns ();

procedure java.lang.Object.clone##(this: Reference) returns ();

const unique java.lang.Object: Type;
