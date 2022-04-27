// -------------------------------------------------------------------
// Heap model
// -------------------------------------------------------------------
type Reference;

const unique ~null: Reference;

type Field a;

type Store;

var ~heap: Store;

function ~read<a>(r: Reference, f: Field a) returns (a);

procedure ~update<a>(r: Reference, f: Field a, v: a) returns ();
	ensures ~read(r, f) == v;
	modifies ~heap;

function ~allocated(r: Reference) returns (bool);

procedure ~new(t: Type) returns (~ret: Reference);
	ensures ~typeof(~ret) == t;
	ensures ~allocated(~ret);
	modifies ~heap;

// -------------------------------------------------------------------
// Type model
// -------------------------------------------------------------------
type Type = Reference;

const ~Object.Type: Field Type;

function ~typeof(r: Reference) returns (Type)
{
  ~read(r, ~Object.Type)
}

function ~instanceof(r: Reference, t: Type) returns (bool)
{
	~typeof(r) == t
}

axiom (forall t: Type :: ~typeof(~null) <: t);

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
	modifies ~heap;

procedure ~array(l: int) returns (~ret: Reference);
  ensures ~read(~ret, ~Array.length) == l;
	modifies ~heap;

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

function ~lt<t>(a: t, b:  t) returns (bool);
axiom (forall a: real, b: real :: ~lt(a, b) <==> a < b);
axiom (forall a: int, b: int :: ~lt(a, b) <==> a < b);

function ~lte<t>(a: t, b: t) returns (bool);
axiom (forall a: real, b: real :: ~lte(a, b) <==> a <= b);
axiom (forall a: int, b: int :: ~lte(a, b) <==> a <= b);

function ~gt<t>(a: t, b: t) returns (bool);
axiom (forall a: real, b: real :: ~gt(a, b) <==> a > b);
axiom (forall a: int, b: int :: ~gt(a, b) <==> a > b);

function ~gte<t>(a: t, b: t) returns (bool);
axiom (forall a: real, b: real :: ~gte(a, b) <==> a >= b);
axiom (forall a: int, b: int :: ~gte(a, b) <==> a >= b);

function ~eq<t>(a: t, b: t) returns (bool)
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

// -------------------------------------------------------------------
// Missing definitions
// -------------------------------------------------------------------
procedure java.lang.Object.$init$##(this: Reference) returns ();

const unique java.lang.Object: Type;
