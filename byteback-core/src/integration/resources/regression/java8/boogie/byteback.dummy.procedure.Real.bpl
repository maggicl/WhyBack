procedure byteback.dummy.procedure.Real.sumIfSmaller#double#double#(a: real, b: real) returns (~ret: real)
{
  if (~cmp(b, a) >= 0) {
    goto label1;
  }

  ~ret := a + b;

  return;

label1:
  ~ret := 0.0;

  return;
}
