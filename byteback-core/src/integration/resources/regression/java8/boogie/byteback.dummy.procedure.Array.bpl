procedure byteback.dummy.procedure.Array.sum#int$#(as: Reference) returns (~ret: int)
{
  var l3: int;
  var c: int;
  var l4: int;
  c := 0;
  l3 := ~lengthof(~heap, as);
  l4 := 0;

label2:

  if (l4 >= l3) {
    goto label1;
  }

  c := (c + ~get(~heap, as, ~Array_int, l4));
  l4 := (l4 + 1);

  goto label2;

label1:

  ~ret := c;

  return;
}
