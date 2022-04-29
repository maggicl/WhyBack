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

function ~allocated(r: Reference) returns (bool);

procedure ~new(t: Type) returns (~ret: Reference);
	ensures ~typeof(~heap, ~ret) == t;
	ensures ~allocated(~ret);
	modifies ~heap;

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
type Array a = Field [int]a;

const unique ~Array.bool: Array bool;

const unique ~Array.int: Array int;

const unique ~Array.real: Array real;

const unique ~Array.Reference: Array Reference;

const unique ~Array.length: Field int;

function ~lengthof(r: Reference) returns (int);

axiom (forall r: Reference :: ~lengthof(r) >= 0);

function ~get<a>(r: Reference, f: Array a, i: int) returns (a);

procedure ~insert<a>(r: Reference, f: Array a, i: int, e: a) returns ();
	ensures ~get(r, f, i) == e;

procedure ~array(l: int) returns (~ret: Reference);
  ensures ~lengthof(~ret) == l;

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
function ~and(h: Store, a: bool, b: bool) returns (bool)
{
  a && b
}

function ~or(h: Store, a: bool, b: bool) returns (bool)
{
  a || b
}

function ~implies(h: Store, a: bool, b: bool) returns (bool)
{
  a ==> b
}

function ~iff(h: Store, a: bool, b: bool) returns (bool)
{
  a <==> b
}

function ~eq<t>(h: Store, a: t, b: t) returns (bool)
{
	a == b
}

function ~neq<t>(h: Store, a: t, b: t) returns (bool)
{
	a != b
}

function ~lt<t>(h: Store, a: t, b:  t) returns (bool);
axiom (forall h: Store, a: real, b: real :: ~lt(h, a, b) <==> a < b);
axiom (forall h: Store, a: int, b: int :: ~lt(h, a, b) <==> a < b);

function ~lte<t>(h: Store, a: t, b: t) returns (bool);
axiom (forall h: Store, a: real, b: real :: ~lte(h, a, b) <==> a <= b);
axiom (forall h: Store, a: int, b: int :: ~lte(h, a, b) <==> a <= b);

function ~gt<t>(h: Store, a: t, b: t) returns (bool);
axiom (forall h: Store, a: real, b: real :: ~gt(h, a, b) <==> a > b);
axiom (forall h: Store, a: int, b: int :: ~gt(h, a, b) <==> a > b);

function ~gte<t>(h: Store, a: t, b: t) returns (bool);
axiom (forall h: Store, a: real, b: real :: ~gte(h, a, b) <==> a >= b);
axiom (forall h: Store, a: int, b: int :: ~gte(h, a, b) <==> a >= b);

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

const unique java.lang.Object: Type;
