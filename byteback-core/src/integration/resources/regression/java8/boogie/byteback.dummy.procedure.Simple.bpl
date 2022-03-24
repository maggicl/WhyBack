procedure byteback.dummy.procedure.Simple.empty##(this: Reference) returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.singleAssignment##(this: Reference) returns ()
{
  var a: int;
  a := 1;
}

procedure byteback.dummy.procedure.Simple.doubleAssignment##(this: Reference) returns ()
{
  var a: int;
  var a#2: int;
  a := 0;
  a#2 := a + 42;
  return;
}

procedure byteback.dummy.procedure.Simple.emptyDoWhile##(this: Reference) returns ()
{
  var a: bool;
  a := false;

label1:
  if (a != false) {
    goto label1;
  }

  return;
}

procedure byteback.dummy.procedure.Simple.emptyIf##(this: Reference) returns ()
{
  var a: bool;
  a := false;

  if (a == false) {
    goto label1;
  }

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignIf##(this: Reference) returns ()
{
  var a: bool;
  a := false;

  if (a != false) {
    goto label1;
  }

  a := true;
label1:
  return;
}

procedure byteback.dummy.procedure.Simple.shortCircuitingAnd##(this: Reference) returns ()
{
  var a: bool;
  var b: bool;
  var $stack4: bool;
  var c: bool;

  a := true;
  b := true;

  if (a == false) {
    goto label2;
  }

  if (b == false) {
    goto label2;
  }

  $stack4 := true;
  goto label3;

label2:
  $stack4 := false;

label3:
  c := $stack4;

  return;
}

procedure byteback.dummy.procedure.Simple.shortCircuitingOr##(this: Reference) returns ()
{
  var a: bool;
  var b: bool;
  var $stack4: bool;
  var c: bool;

  a := true;
  b := true;

  if (a != false) {
    goto label1;
  }

  if (b == false) {
    goto label2;
  }

label1:
  $stack4 := true;
  goto label3;

label2:
  $stack4 := false;

label3:
  c := $stack4;

  return;
}

procedure byteback.dummy.procedure.Simple.shortCircuitingNot##(this: Reference) returns ()
{
  var a: bool;
  var $stack3: bool;
  var c: bool;
  a := true;

  if (a != false) {
    goto label1;
  }

  $stack3 := true;
  goto label2;

label1:
  $stack3 := false;

label2:
  c := $stack3;

  return;
}
