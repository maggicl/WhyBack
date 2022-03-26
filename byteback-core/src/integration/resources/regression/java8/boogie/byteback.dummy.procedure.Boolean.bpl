function ~int<a>(a) returns (int);

// bool -> int
axiom ~int(false) == 0;
axiom ~int(true) == 1;

procedure byteback.dummy.procedure.Simple.shortCircuitingAnd##(a: bool, b: bool) returns (~ret: bool)
  ensures a && b ==> ~ret;
{
  var $stack4: bool;

  if (~int(a) == 0) {
    goto label2;
  }

  if (~int(b) == 0) {
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
  ensures a || b ==> ~ret;
{
  var $stack4: bool;

  if (~int(a) != 0) {
    goto label1;
  }

  if (~int(b) == 0) {
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
  ensures a ==> !~ret;
{
  var $stack3: bool;

  if (~int(a) != 0) {
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
