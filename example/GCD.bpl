type Reference;

const unique ~null : Reference;

const unique ~void : Reference;

function ~isvoid(r : Reference) returns (bool) { (r == ~void) }

type Field a;

type Store  = [Reference]<a>[Field (a)]a;

function {:inline } ~heap.read<a>(h : Store, r : Reference, f : Field (a)) returns (a) { h[r][f] }

function {:inline } ~heap.update<a>(h : Store, r : Reference, f : Field (a), v : a) returns (Store) { h[r := h[r][f := v]] }

function ~heap.succ(h1 : Store, h2 : Store) returns (bool);

function ~heap.isgood(h : Store) returns (bool);

function ~heap.isanchor(h : Store) returns (bool);

var ~heap : Store where (~heap.isgood(~heap) && ~heap.isanchor(~heap));

axiom (forall h1 : Store, h2 : Store, h3 : Store :: {~heap.succ(h1, h2), ~heap.succ(h2, h3)} ((h1 != h3) ==> ((~heap.succ(h1, h2) && ~heap.succ(h2, h3)) ==> ~heap.succ(h1, h3))));

axiom (forall h1 : Store, h2 : Store :: (~heap.succ(h1, h2) && (forall r : Reference, t : Type :: (~typeof(h1, r) == ~typeof(h2, r)))));

axiom (forall h1 : Store, h2 : Store :: (~heap.succ(h1, h2) && (forall r : Reference :: {~allocated(h2, r)} (~allocated(h1, r) == ~allocated(h2, r)))));

function ~allocated(Store, Reference) returns (bool);

procedure ~new(t : Type) returns (~ret : Reference, ~exc : Reference);
  ensures (~ret != ~null);
  ensures (~typeof(~heap, ~ret) == t);
  ensures ~allocated(~heap, ~ret);
  ensures (~exc == ~void);

type Type;

const unique ~Object.Type : Field (Type);

const unique ~Primitive : Type;

function {:inline } ~typeof(h : Store, r : Reference) returns (Type) { ~heap.read(h, r, ~Object.Type) }

function ~instanceof(h : Store, r : Reference, t : Type) returns (bool) { (~heap.read(h, r, ~Object.Type) <: t) }

axiom (forall h : Store, t : Type :: !~instanceof(h, ~void, t));

function ~type.reference(Type) returns (Reference);

function ~type.reference_inverse(Reference) returns (Type);

axiom (forall t : Type :: {~type.reference(t)} (~type.reference_inverse(~type.reference(t)) == t));

type Box;

function ~box<a>(a) returns (Box);

function ~unbox<a>(Box) returns (a);

axiom (forall <a> x : a :: {~box(x)} (~unbox(~box(x)) == x));

function ~element(int) returns (Field (Box));

function ~element_inverse<a>(Field (a)) returns (int);

axiom (forall i : int :: {~element(i)} (~element_inverse(~element(i)) == i));

function ~lengthof(r : Reference) returns (int);

axiom (forall r : Reference :: (~lengthof(r) >= 0));

axiom (forall h1 : Store, h2 : Store, r : Reference, i : int :: ((~heap.succ(h1, h2) && ((0 <= i) && (i < ~lengthof(r)))) ==> (~heap.read(h1, r, ~element(i)) == ~heap.read(h2, r, ~element(i)))));

function ~array.type(Type) returns (Type);

function ~array.type_inverse(Type) returns (Type);

axiom (forall t : Type :: {~array.type(t)} (~array.type_inverse(~array.type(t)) == t));

function {:inline } ~array.read<b>(h : Store, a : Reference, i : int) returns (b) { (~unbox(~heap.read(h, a, ~element(i))) : b) }

function {:inline } ~array.update<b>(h : Store, a : Reference, i : int, v : b) returns (Store) { ~heap.update(h, a, ~element(i), ~box(v)) }

procedure ~array(t : Type, l : int) returns (~ret : Reference, ~exc : Reference);
  ensures (~ret != ~null);
  ensures (~typeof(~heap, ~ret) == ~array.type(t));
  ensures ~allocated(~heap, ~ret);
  ensures ((~lengthof(~ret) == l) && (~lengthof(~ret) >= 0));
  ensures (~exc == ~void);

function ~string.const(id : int) returns (~ret : Reference);

procedure ~string(chars : Reference) returns (~ret : Reference, ~exc : Reference);
  ensures (~ret != ~null);
  ensures ~allocated(~heap, ~ret);
  ensures (~exc == ~void);

function ~cmp<t>(a : t, b : t) returns (int);

axiom (forall i : real, j : real :: ((i < j) <==> (~cmp(i, j) == -1)));

axiom (forall i : real, j : real :: ((i > j) <==> (~cmp(i, j) == 1)));

axiom (forall i : real, j : real :: ((i == j) <==> (~cmp(i, j) == 0)));

axiom (forall i : int, j : int :: ((i < j) <==> (~cmp(i, j) == -1)));

axiom (forall i : int, j : int :: ((i > j) <==> (~cmp(i, j) == 1)));

axiom (forall i : int, j : int :: ((i == j) <==> (~cmp(i, j) == 0)));

function ~shl(a : int, p : int) returns (int);

function ~shr(a : int, p : int) returns (int);

function ~and(a : bool, b : bool) returns (bool) { (a && b) }

function ~or(a : bool, b : bool) returns (bool) { (a || b) }

function ~implies(a : bool, b : bool) returns (bool) { (a ==> b) }

function ~iff(a : bool, b : bool) returns (bool) { (a <==> b) }

function ~eq<t>(a : t, b : t) returns (bool) { (a == b) }

function ~neq<t>(a : t, b : t) returns (bool) { (a != b) }

function ~int.lt(a : int, b : int) returns (bool) { (a < b) }

function ~real.lt(a : real, b : real) returns (bool) { (a < b) }

function ~int.lte(a : int, b : int) returns (bool) { (a <= b) }

function ~real.lte(a : real, b : real) returns (bool) { (a <= b) }

function ~int.gt(a : int, b : int) returns (bool) { (a > b) }

function ~real.gt(a : real, b : real) returns (bool) { (a > b) }

function ~int.gte(a : int, b : int) returns (bool) { (a >= b) }

function ~real.gte(a : real, b : real) returns (bool) { (a >= b) }

function ~not(a : bool) returns (bool) { !a }

function ~int<a>(a) returns (int);

axiom (~int(false) == 0);

axiom (~int(true) == 1);

function ~int_to_real(a : int) returns (real) { real(a) }

function ~real_to_int(a : real) returns (int) { int(a) }

const unique $java.lang.Exception : Type;

axiom ($java.lang.Exception <: $java.lang.Throwable);

const unique $java.lang.Throwable : Type;

axiom ($java.lang.Throwable <: $java.lang.Object);

axiom ($java.lang.Throwable <: $java.io.Serializable);

const unique $java.lang.Comparable : Type;

axiom ($java.lang.Comparable <: $java.lang.Object);

const unique $java.io.Serializable : Type;

axiom ($java.io.Serializable <: $java.lang.Object);

const unique $java.lang.Object : Type;

axiom (forall t1 : Type, t2 : Type :: {(t1 <: $GCD), (t2 <: $java.lang.Throwable)} (((t1 <: $GCD) && (t2 <: $java.lang.Throwable)) ==> (!(t1 <: t2) && !(t2 <: t1))));

axiom (forall t1 : Type, t2 : Type :: {(t1 <: $java.lang.Throwable), (t2 <: $java.lang.String)} (((t1 <: $java.lang.Throwable) && (t2 <: $java.lang.String)) ==> (!(t1 <: t2) && !(t2 <: t1))));

axiom (forall t1 : Type, t2 : Type :: {(t1 <: $GCD), (t2 <: $java.lang.String)} (((t1 <: $GCD) && (t2 <: $java.lang.String)) ==> (!(t1 <: t2) && !(t2 <: t1))));

const unique $java.lang.RuntimeException : Type;

axiom ($java.lang.RuntimeException <: $java.lang.Exception);

const unique $java.lang.constant.Constable : Type;

axiom ($java.lang.constant.Constable <: $java.lang.Object);

const unique $GCD : Type;

axiom ($GCD <: $java.lang.Object);

const unique $java.lang.String : Type;

axiom ($java.lang.String <: $java.lang.Object);

axiom ($java.lang.String <: $java.io.Serializable);

axiom ($java.lang.String <: $java.lang.Comparable);

axiom ($java.lang.String <: $java.lang.CharSequence);

axiom ($java.lang.String <: $java.lang.constant.Constable);

axiom ($java.lang.String <: $java.lang.constant.ConstantDesc);

const unique $java.lang.IllegalArgumentException : Type;

axiom ($java.lang.IllegalArgumentException <: $java.lang.RuntimeException);

const unique $java.lang.constant.ConstantDesc : Type;

axiom ($java.lang.constant.ConstantDesc <: $java.lang.Object);

const unique $java.lang.CharSequence : Type;

axiom ($java.lang.CharSequence <: $java.lang.Object);

function GCD.arguments_are_negative#boolean#int#int##(~heap : Store, _l0 : int, _l1 : int) returns (bool) { (~int.lte(_l0, 0) || ~int.lte(_l1, 0)) }

function GCD.arguments_are_positive#boolean#int#int##(~heap : Store, _l0 : int, _l1 : int) returns (bool) { (~int.lte(_l0, 0) && ~int.lte(_l1, 0)) }

function GCD.gcd_recursive#int#int#int##(~heap : Store, _l0 : int, _l1 : int) returns (int) { if ~eq(_l0, _l1) then _l0 else if ~int.gt(_l0, _l1) then GCD.gcd_recursive#int#int#int##(~heap, (_l0 - _l1), _l1) else GCD.gcd_recursive#int#int#int##(~heap, _l0, (_l1 - _l0)) }

procedure GCD.$init$#void##(?l0 : Reference where ~instanceof(~heap, ?l0, $GCD)) returns (~exc : Reference)
  free ensures ~heap.succ(old(~heap), ~heap);
  modifies ~heap;
{
  var _l0 : Reference where ~instanceof(~heap, _l0, $GCD);
  ~exc := ~void;
  _l0 := ?l0;
  call ~exc := java.lang.Object.$init$#void##(_l0);
  if ((~exc == ~void)) {
    goto label1;
  }
  return;
label1:
  return;
}

procedure java.lang.Object.$init$#void##(?this : Reference where ~instanceof(~heap, ?this, $java.lang.Object)) returns (~exc : Reference);
  ensures (true ==> (~exc == ~void));
  free ensures ~heap.succ(old(~heap), ~heap);
  modifies ~heap;

procedure GCD.gcd#int#int#int##(?l0 : int, ?l1 : int) returns (~ret : int, ~exc : Reference)
  ensures {:msg "GCD.java: (line 36): Error: The exceptional-precondition arguments_are_negative might not hold."}(old((~int.lte(?l0, 0) || ~int.lte(?l1, 0))) ==> ~instanceof(~heap, ~exc, $java.lang.IllegalArgumentException));
  ensures {:msg "GCD.java: (line 36): Error: The postcondition result_is_gcd might not hold."}~implies(~not(GCD.arguments_are_negative#boolean#int#int##(~heap, ?l0, ?l1)), ~eq(~ret, GCD.gcd_recursive#int#int#int##(~heap, ?l0, ?l1)));
  free ensures ~heap.succ(old(~heap), ~heap);
  modifies ~heap;
{
  var _l0 : int;
  var _l1 : int;
  var _$stack4 : Reference where ~instanceof(~heap, _$stack4, $java.lang.IllegalArgumentException);
  var _l2 : int;
  var _l3 : int;
  ~exc := ~void;
  _l0 := ?l0;
  _l1 := ?l1;
  if ((_l0 <= 0)) {
    goto label1;
  }
  if ((_l1 > 0)) {
    goto label2;
  }
label1:
  call _$stack4, ~exc := ~new($java.lang.IllegalArgumentException);
  call ~exc := java.lang.IllegalArgumentException.$init$#void#java.lang.String##(_$stack4, ~string.const(1364445960));
  if ((~exc == ~void)) {
    goto label3;
  }
  return;
label3:
  ~exc := _$stack4;
  return;
label2:
  _l2 := _l0;
  _l3 := _l1;
label7:
  assert {:msg "GCD.java: (line 45): Error: This assertion might not hold."}(~int.gt(_l2, 0) && ~int.gt(_l3, 0));
  assert {:msg "GCD.java: (line 46): Error: This assertion might not hold."}~eq(GCD.gcd_recursive#int#int#int##(~heap, _l2, _l3), GCD.gcd_recursive#int#int#int##(~heap, _l0, _l1));
  assert (~exc == ~void);
  assert (~exc == ~void);
  if ((_l2 == _l3)) {
    goto label4;
  }
  if ((_l2 <= _l3)) {
    goto label5;
  }
  _l2 := (_l2 - _l3);
  goto label7;
label5:
  _l3 := (_l3 - _l2);
  assert (~exc == ~void);
  assert {:msg "GCD.java: (line 45): Error: This assertion might not hold."}(~int.gt(_l2, 0) && ~int.gt(_l3, 0));
  assert {:msg "GCD.java: (line 46): Error: This assertion might not hold."}~eq(GCD.gcd_recursive#int#int#int##(~heap, _l2, _l3), GCD.gcd_recursive#int#int#int##(~heap, _l0, _l1));
  goto label7;
label4:
  assert {:msg "GCD.java: (line 45): Error: This assertion might not hold."}(~int.gt(_l2, 0) && ~int.gt(_l3, 0));
  assert {:msg "GCD.java: (line 46): Error: This assertion might not hold."}~eq(GCD.gcd_recursive#int#int#int##(~heap, _l2, _l3), GCD.gcd_recursive#int#int#int##(~heap, _l0, _l1));
  assert (~exc == ~void);
  ~ret := _l2;
  return;
}

procedure java.lang.IllegalArgumentException.$init$#void#java.lang.String##(?this : Reference where ~instanceof(~heap, ?this, $java.lang.IllegalArgumentException), ?p0 : Reference where ~instanceof(~heap, ?p0, $java.lang.String)) returns (~exc : Reference);
  ensures (true ==> (~exc == ~void));
  free ensures ~heap.succ(old(~heap), ~heap);
  modifies ~heap;