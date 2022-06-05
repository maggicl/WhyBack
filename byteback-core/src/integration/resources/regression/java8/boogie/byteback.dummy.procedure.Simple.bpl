const unique $byteback.dummy.procedure.Simple : Type;

procedure byteback.dummy.procedure.Simple.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Simple)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Simple);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

procedure byteback.dummy.procedure.Simple.empty##() returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.doubleAssignment##() returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.emptyWhile##() returns ()
{
  var $a : bool;
  $a := false;
label2:
  if ((~int($a) == 0)) {
    goto label1;
  }
  goto label2;
label1:
  return;
}

procedure byteback.dummy.procedure.Simple.emptyDoWhile##() returns ()
{
  var $a : bool;
  $a := false;
label1:
  if ((~int($a) != 0)) {
    goto label1;
  }
  return;
}

procedure byteback.dummy.procedure.Simple.emptyIf##() returns ()
{
  if ((~int(false) == 0)) {
    goto label1;
  }
label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignIf##() returns ()
{
  var $a : bool;
  $a := false;
  if ((~int($a) != 0)) {
    goto label1;
  }
  $a := true;
label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignParameter#int#(?a : int) returns ()
{
  return;
}

procedure byteback.dummy.procedure.Simple.emptyFor##() returns ()
{
  var $i : int;
  $i := 0;
label2:
  if (($i >= 10)) {
    goto label1;
  }
  $i := ($i + 1);
  goto label2;
label1:
  return;
}

procedure byteback.dummy.procedure.Simple.emptyNestedFor##() returns ()
{
  var $i : int;
  var $j : int;
  $i := 0;
label4:
  if (($i >= 10)) {
    goto label1;
  }
  $j := 0;
label3:
  if (($j >= 10)) {
    goto label2;
  }
  $j := ($j + 1);
  goto label3;
label2:
  $i := ($i + 1);
  goto label4;
label1:
  return;
}

procedure byteback.dummy.procedure.Simple.returnsNull##() returns (~ret : Reference)
{
  ~ret := ~null;
  return;
}

procedure byteback.dummy.procedure.Simple.realCondition##() returns ()
{
  if ((~cmp(3.14, 2.72) >= 0)) {
    goto label1;
  }
label1:
  return;
}

procedure byteback.dummy.procedure.Simple.assignsProcedureResult##() returns ()
{
  var $a : Reference where (~typeof(~heap, $a) == $java.lang.Object);
  call $a := byteback.dummy.procedure.Simple.returnsNull##();
  return;
}

procedure byteback.dummy.procedure.Simple.assignsProcedureResultTwice##() returns ()
{
  var $a : Reference where (~typeof(~heap, $a) == $java.lang.Object);
  call $a := byteback.dummy.procedure.Simple.returnsNull##();
  call $a := byteback.dummy.procedure.Simple.returnsNull##();
  return;
}

procedure byteback.dummy.procedure.Simple.callsVoidProcedure##() returns ()
{
  call byteback.dummy.procedure.Simple.emptyWhile##();
  return;
}

procedure byteback.dummy.procedure.Simple.callsInForLoop##() returns ()
{
  var $i : int;
  $i := 0;
label2:
  if (($i >= 10)) {
    goto label1;
  }
  call byteback.dummy.procedure.Simple.emptyFor##();
  $i := ($i + 1);
  goto label2;
label1:
  return;
}