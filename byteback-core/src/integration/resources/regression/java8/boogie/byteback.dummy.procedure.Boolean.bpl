procedure byteback.dummy.procedure.Simple.shortCircuitingAnd##(a: bool, b: bool) returns (~ret: bool)
{
  var $stack4: bool;

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
  ~ret := $stack4;

  return;
}

procedure byteback.dummy.procedure.Simple.shortCircuitingOr##(a: bool, b: bool) returns (~ret: bool)
{
  var $stack4: bool;

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
  ~ret := $stack4;

  return;
}

procedure byteback.dummy.procedure.Simple.shortCircuitingNot##(a: bool) returns (~ret: bool)
  ensures a == true ==> ~ret == false;
{
  var $stack3: bool;

  if (a != false) {
    goto label1;
  }

  $stack3 := true;
  goto label2;

label1:
  $stack3 := false;

label2:
  ~ret := $stack3;

  return;
}
