procedure byteback.dummy.procedure.Simple.$init$##(this: Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

procedure byteback.dummy.procedure.Simple.empty##() returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.emptyWhile##() returns ()
{

label2:
  if (~int(false) == 0) {
    goto label1;
  }

  goto label2;

label1:
  return;

}

procedure byteback.dummy.procedure.Simple.emptyDoWhile##() returns ()
{
label1:
  if (~int(false) != 0) {
    goto label1;
  }

  return;
}

procedure byteback.dummy.procedure.Simple.emptyIf##() returns ()
{
  if (~int(false) == 0) {
    goto label1;
  }

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignIf##() returns ()
{
  var a: bool;
  a := false;

  if (~int(a) != 0) {
    goto label1;
  }

  a := true;

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignParameter#int#(a: int) returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.emptyFor##() returns ()
{
  var i: int;
  i := 0;

label2:
  if (i >= 10) {
    goto label1;
  }

  i := i + 1;
  goto label2;

label1:
  return;
}


procedure byteback.dummy.procedure.Simple.returnsNull##() returns (~ret: Reference)  
{
  ~ret := ~null;
  return;
}

procedure byteback.dummy.procedure.Simple.realCondition##() returns ()
{
  if (~cmp(3.14, 2.72) >= 0) {
    goto label1;
  }

label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignsProcedureResult##() returns ()
{
  var a: Reference;

  call a := byteback.dummy.procedure.Simple.returnsNull##();

  return;
}

procedure byteback.dummy.procedure.Simple.assignsProcedureResultTwice##() returns ()
{
  var a: Reference;

  call a := byteback.dummy.procedure.Simple.returnsNull##();
  call a := byteback.dummy.procedure.Simple.returnsNull##();

  return;
}

procedure byteback.dummy.procedure.Simple.callsVoidProcedure##() returns ()
{
  call byteback.dummy.procedure.Simple.emptyWhile##();
  return;
}

procedure byteback.dummy.procedure.Simple.callsInForLoop##() returns ()
{
  var i: int;
  i := 0;

label2:
  if (i >= 10) {
    goto label1;
  }

  call byteback.dummy.procedure.Simple.emptyFor##();
  i := (i + 1);
  goto label2;

label1:
  return;
}
