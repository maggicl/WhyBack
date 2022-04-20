procedure byteback.dummy.condition.Simple.$init$##(this: Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

procedure byteback.dummy.condition.Simple.returnsOne##() returns (~ret: int)
  ensures ~eq(~heap, ~ret, 1);
{
  ~ret := 1;
  return;
}

procedure byteback.dummy.condition.Simple.identity#int#(a: int) returns (~ret: int)
  ensures ~eq(~heap, ~ret, a);
{
  ~ret := a;
  return;
}

procedure byteback.dummy.condition.Simple.singleAssertion##()
{
  assert true;
  return;
}

procedure byteback.dummy.condition.Simple.singleAssumption##()
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

function byteback.dummy.condition.Simple.recursive_fibonacci#int#(~heap: Store, m: int) returns (int)
{
  if ~lte(~heap, m, 0) then 0 else byteback.dummy.condition.Simple.recursive_fibonacci#int#(~heap, m)
}

procedure byteback.dummy.condition.Simple.fibonacci#int#(m: int) returns (~ret: int)
  requires ~gt(~heap, m, 0);
{
  var c: int;
  var a: int;
  var b: int;
  var i: int;
  a := 0;
  b := 1;
  i := 0;

  assert ~lte(~heap, i, m);
label2:
  assume ~lte(~heap, i, m);
  if (i >= m) {
    goto label1;
  }

  c := a + b;
  a := b;
  b := c;
  i := i + 1;
  assert ~lte(~heap, i, m);
  goto label2;
label1:
  assume ~lte(~heap, i, m);
  ~ret := a;

  return;
}

procedure byteback.dummy.condition.Simple.overloadedConditions##() returns (~ret: int)
  ensures true;
{
  ~ret := 0;
  return;
}

procedure byteback.dummy.condition.Simple.result##() returns (~ret: int)
  ensures ~eq(~heap, ~ret, 1);
{
  ~ret := 1;
  return;
}

procedure byteback.dummy.condition.Simple.returnsResult##() returns (~ret: int)
  ensures ~eq(~heap, ~ret, 1);
{
  var $stack0: int;
  call $stack0 := byteback.dummy.condition.Simple.result##();
  ~ret := $stack0;
  return;
}

procedure byteback.dummy.condition.Simple.loopAssertion##() returns ()
{
  var i: int;
  i := 0;
label2:
  if (i >= 10) {
    goto label1;
  }
  assert ~eq(~heap, 0, 0);
  i := (i + 1);
  goto label2;
label1:
  return;
}

procedure byteback.dummy.condition.Simple.loopInvariant##() returns ()
{
  var i: int;
  i := 0;
  assert ~eq(~heap, 0, 0);
label2:
  assume ~eq(~heap, 0, 0);
  if (i >= 10) {
    goto label1;
  }
  i := (i + 1);
  assert ~eq(~heap, 0, 0);
  goto label2;
label1:
  assume ~eq(~heap, 0, 0);

  return;
}

procedure byteback.dummy.condition.Simple.assignIf#int#(b: int) returns ()
{
  var a: int;
  a := 0;
  if (a >= b) {
    goto label1;
  }
  a := 1;
  goto label2;
label1:
  a := 2;
label2:
  assert ~implies(~heap, ~gt(~heap, b, a), ~eq(~heap, a, 1));
  assert ~gt(~heap, a, 0);
  return;
}

procedure byteback.dummy.condition.Simple.fizz##()
{
  return;
}

procedure byteback.dummy.condition.Simple.buzz##()
{
  return;
}

procedure byteback.dummy.condition.Simple.fizzBuzz#int#(n: int)
  requires ~gt(~heap, n, 0);
{
  var i: int;
  i := 0;
  assert ~lte(~heap, i, n);

label4:

  assume ~lte(~heap, i, n);
  if (i >= n) {
    goto label1;
  }

  if (i mod 3 != 0) {
    goto label2;
  }

  call byteback.dummy.condition.Simple.fizz##();

label2:

  if (i mod 5 != 0) {
    goto label3;
  }

  call byteback.dummy.condition.Simple.buzz##();

label3:

  i := i + 1;
  assert ~lte(~heap, i, n);

  goto label4;

label1:

  assume ~lte(~heap, i, n);
  return;
}
