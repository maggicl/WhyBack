const unique $byteback.dummy.procedure.Real : Type;

procedure byteback.dummy.procedure.Real.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Real)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Real);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

procedure byteback.dummy.procedure.Real.sumIfSmaller#double#double#(?a : real, ?b : real) returns (~ret : real)
{
  var $a : real;
  var $b : real;
  $b := ?b;
  $a := ?a;
  if ((~cmp($b, $a) >= 0)) {
    goto label1;
  }
  ~ret := ($a + $b);
  return;
label1:
  ~ret := 0.0;
  return;
}