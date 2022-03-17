// -------------------------------------------------------------------
// Heap model
// -------------------------------------------------------------------
type Reference;

const `null`: Reference;

type Field a;
type Store = <a>[Reference, Field a]a;

var `heap`: Store;

function `read`<a>(h: Store, r: Reference, f: Field a) returns (a)
{ h[r, f] }

function `update`<a>(h: Store, r: Reference, f: Field a, v: a) returns (Store)
{ h[r, f := v] }
