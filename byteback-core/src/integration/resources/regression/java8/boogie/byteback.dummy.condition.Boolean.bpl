procedure byteback.dummy.condition.Boolean.shortCircuitingAnd#boolean#boolean#(a: bool, b: bool) returns (~ret: bool)
  ensures ~implies(~heap, a && b, ~ret);
{
  var $stack2: bool;

  if (~int(a) == 0) {
    goto label2;
  }

  if (~int(b) == 0) {
    goto label2;
  }

  $stack2 := true;
  goto label3;

label2:
  $stack2 := false;

label3:
  ~ret := $stack2;

  return;
}

procedure byteback.dummy.condition.Boolean.shortCircuitingOr#boolean#boolean#(a: bool, b: bool) returns (~ret: bool)
  ensures ~implies(~heap, a || b, ~ret);
{
  var $stack2: bool;

  if (~int(a) != 0) {
    goto label1;
  }

  if (~int(b) == 0) {
    goto label2;
  }

label1:
  $stack2 := true;
  goto label3;

label2:
  $stack2 := false;

label3:
  ~ret := $stack2;

  return;
}

procedure byteback.dummy.condition.Boolean.shortCircuitingNot#boolean#(a: bool) returns (~ret: bool)
  ensures ~implies(~heap, a, ~not(~heap, ~ret));
{
  var $stack1: bool;

  if (~int(a) != 0) {
    goto label1;
  }

  $stack1 := true;
  goto label2;

label1:
  $stack1 := false;

label2:
  ~ret := $stack1;

  return;
}
