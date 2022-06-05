const unique $byteback.dummy.procedure.Boolean : Type;

procedure byteback.dummy.procedure.Boolean.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Boolean)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Boolean);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

procedure byteback.dummy.procedure.Boolean.shortCircuitingAnd#boolean#boolean#(?a : bool, ?b : bool) returns (~ret : bool)
{
  var $$stack2 : bool;
  var $a : bool;
  var $b : bool;
  $b := ?b;
  $a := ?a;
  if ((~int($a) == 0)) {
    goto label2;
  }
  if ((~int($b) == 0)) {
    goto label2;
  }
  $$stack2 := true;
  goto label3;
label2:
  $$stack2 := false;
label3:
  ~ret := $$stack2;
  return;
}

procedure byteback.dummy.procedure.Boolean.shortCircuitingOr#boolean#boolean#(?a : bool, ?b : bool) returns (~ret : bool)
{
  var $$stack2 : bool;
  var $a : bool;
  var $b : bool;
  $b := ?b;
  $a := ?a;
  if ((~int($a) != 0)) {
    goto label1;
  }
  if ((~int($b) == 0)) {
    goto label2;
  }
label1:
  $$stack2 := true;
  goto label3;
label2:
  $$stack2 := false;
label3:
  ~ret := $$stack2;
  return;
}

procedure byteback.dummy.procedure.Boolean.shortCircuitingNot#boolean#(?a : bool) returns (~ret : bool)
{
  var $$stack1 : bool;
  var $a : bool;
  $a := ?a;
  if ((~int($a) != 0)) {
    goto label1;
  }
  $$stack1 := true;
  goto label2;
label1:
  $$stack1 := false;
label2:
  ~ret := $$stack1;
  return;
}