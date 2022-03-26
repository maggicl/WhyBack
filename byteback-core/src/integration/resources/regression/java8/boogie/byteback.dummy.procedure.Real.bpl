procedure byteback.dummy.procedure.Real.sumIfSmaller#double#double#(a: real, b: real) returns (~ret: real)
{
  var $stack4: int;
  var $stack5: real;
  $stack4 := ~cmp(b, a);

  if ($stack4 >= 0) {
    goto label1;
  }

  $stack5 := a + b;
  ~ret := $stack5;

  return;

label1:
  ~ret := 0.0;
  return;
}
