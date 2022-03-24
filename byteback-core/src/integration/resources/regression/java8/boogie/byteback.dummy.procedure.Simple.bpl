procedure byteback.dummy.procedure.Simple.empty##() returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.singleAssignment##() returns ()
{
  var a: int;
  a := 42;
  return;
}

procedure byteback.dummy.procedure.Simple.doubleAssignment##() returns ()
{
  var a: int;
  var a#2: int;
  a := 0;
  a#2 := a + 42;
  return;
}

procedure byteback.dummy.procedure.Simple.emptyDoWhile##() returns ()
{
  var a: bool;
  a := false;

label1:
  if (a != false) {
    goto label1;
  }

  return;
}

procedure byteback.dummy.procedure.Simple.emptyIf##() returns ()
{
  var a: bool;
  a := false;

  if (a == false) {
    goto label1;
  }

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignIf##() returns ()
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
