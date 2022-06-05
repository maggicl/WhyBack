const unique $byteback.dummy.condition.Simple : Type;

procedure byteback.dummy.condition.Simple.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.condition.Simple)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.condition.Simple);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

procedure byteback.dummy.condition.Simple.returnsOne##() returns (~ret : int)
  ensures ~eq(~ret, 1);
{
  ~ret := 1;
  return;
}

procedure byteback.dummy.condition.Simple.identity#int#(?a : int) returns (~ret : int)
  ensures ~eq(~ret, ?a);
{
  var $a : int;
  $a := ?a;
  ~ret := $a;
  return;
}

procedure byteback.dummy.condition.Simple.singleAssertion##() returns ()
{
  assert true;
  return;
}

procedure byteback.dummy.condition.Simple.singleAssumption##() returns ()
{
  assume true;
  return;
}

procedure byteback.dummy.condition.Simple.wrongAssumption1##() returns ()
{
  assume false;
  return;
}

procedure byteback.dummy.condition.Simple.wrongAssumption2##() returns ()
{
  assume false;
  return;
}

function byteback.dummy.condition.Simple.recursive_fibonacci#int#(~heap : Store, $m : int) returns (int) { if ~int.lte($m, 0) then 0 else byteback.dummy.condition.Simple.recursive_fibonacci#int#(~heap, $m) }

procedure byteback.dummy.condition.Simple.fibonacci#int#(?m : int) returns (~ret : int)
  requires ~int.gt(?m, 0);
{
  var $a : int;
  var $b : int;
  var $i : int;
  var $m : int;
  $m := ?m;
  $a := 0;
  $b := 1;
  $i := 0;
  assert ~int.lte($i, $m);
label2:
  assume ~int.lte($i, $m);
  if (($i >= $m)) {
    goto label1;
  }
  $a := $b;
  $b := ($a + $b);
  $i := ($i + 1);
  assert ~int.lte($i, $m);
  goto label2;
label1:
  assume ~int.lte($i, $m);
  ~ret := $a;
  return;
}

procedure byteback.dummy.condition.Simple.overloadedConditions##() returns (~ret : int)
  ensures true;
{
  ~ret := 0;
  return;
}

procedure byteback.dummy.condition.Simple.result##() returns (~ret : int)
  ensures ~eq(~ret, 1);
{
  ~ret := 1;
  return;
}

procedure byteback.dummy.condition.Simple.returnsResult##() returns (~ret : int)
  ensures ~eq(~ret, 1);
{
  var $$stack0 : int;
  call $$stack0 := byteback.dummy.condition.Simple.result##();
  ~ret := $$stack0;
  return;
}

procedure byteback.dummy.condition.Simple.loopAssertion##() returns ()
{
  var $c : int;
  var $i : int;
  $c := 0;
  $i := 0;
label2:
  if (($i >= 10)) {
    goto label1;
  }
  assert ~eq($c, 0);
  $i := ($i + 1);
  goto label2;
label1:
  return;
}

procedure byteback.dummy.condition.Simple.loopInvariant##() returns ()
{
  var $c : int;
  var $i : int;
  $c := 0;
  $i := 0;
  assert ~eq(0, $c);
label2:
  assume ~eq(0, $c);
  if (($i >= 10)) {
    goto label1;
  }
  $i := ($i + 1);
  assert ~eq(0, $c);
  goto label2;
label1:
  assume ~eq(0, $c);
  return;
}

procedure byteback.dummy.condition.Simple.assignIf#int#(?b : int) returns ()
{
  var $a : int;
  var $b : int;
  $b := ?b;
  $a := 0;
  if (($a >= $b)) {
    goto label1;
  }
  $a := 1;
  goto label2;
label1:
  $a := 2;
label2:
  assert ~implies(~int.gt($b, $a), ~eq($a, 1));
  assert ~int.gt($a, 0);
  return;
}

procedure byteback.dummy.condition.Simple.fizz##() returns ()
{
  return;
}

procedure byteback.dummy.condition.Simple.buzz##() returns ()
{
  return;
}

procedure byteback.dummy.condition.Simple.fizzBuzz#int#(?n : int) returns ()
  requires ~int.gt(?n, 0);
{
  var $i : int;
  var $n : int;
  $n := ?n;
  $i := 0;
  assert ~int.lte($i, $n);
label4:
  assume ~int.lte($i, $n);
  if (($i >= $n)) {
    goto label1;
  }
  if ((($i mod 3) != 0)) {
    goto label2;
  }
  call byteback.dummy.condition.Simple.fizz##();
label2:
  if ((($i mod 5) != 0)) {
    goto label3;
  }
  call byteback.dummy.condition.Simple.buzz##();
label3:
  $i := ($i + 1);
  assert ~int.lte($i, $n);
  goto label4;
label1:
  assume ~int.lte($i, $n);
  return;
}