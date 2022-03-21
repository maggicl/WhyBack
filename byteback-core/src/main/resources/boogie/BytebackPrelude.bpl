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
// Additional operators
// -------------------------------------------------------------------
function ~cmp_bool(bool) returns (int);
function ~cmp_int(int) returns (int);
function ~cmp_real(real) returns (int);

function ~or_int(int, int) returns (int);
function ~and_int(int, int) returns (int);
function ~xor_int(int, int) returns (int);
