type Type;

const ~Object.Type : Field Type;

axiom (forall h : Store, t : Type :: (h[~null][~Object.Type] <: t));

type Reference;

const unique ~null : Reference;

type Field a;

type Store  = [Reference]<a>[Field a]a;

var ~heap : Store;

function ~read<a>(h : Store, r : Reference, f : Field a) returns (a) { h[r][f] }

function ~update<a>(h : Store, r : Reference, f : Field a, v : a) returns (Store) { h[r := h[r][f := v]] }

procedure ~new() returns (~ret : Reference);

type Global a;

type Table  = [Type]<a>[Global a]a;

function ~fetch<a>(t : Table, r : Type, g : Global a) returns (a) { t[r][g] }

function ~put<a>(t : Table, r : Type, g : Global a, v : a) returns (a) { t[r := t[r][g := v]] }

type Array a = Field [int]a;

const unique ~Array.bool : Array bool;

const unique ~Array.int : Array int;

const unique ~Array.real : Array real;

const unique ~Array.Reference : Array Reference;

const unique ~Array.length : Field int;

function ~lengthof(h : Store, r : Reference) returns (int) { ~read(h, r, ~Array.length) }

axiom (forall h : Store, r : Reference :: (~lengthof(h, r) >= 0));

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

procedure java.lang.Object.$init$##(this : Reference) returns ();

const unique byteback.dummy.complete.GCD : Type;

procedure byteback.dummy.complete.GCD.$init$##(this : Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

procedure byteback.dummy.complete.GCD.gcd#int#int#(a : int, b : int) returns (~ret : int)
  requires (~gt(~heap, a, 0) && ~gt(~heap, b, 0));
  ensures ~gte(~heap, ~ret, 0);
{
  ~ret := 1;
  return;
}