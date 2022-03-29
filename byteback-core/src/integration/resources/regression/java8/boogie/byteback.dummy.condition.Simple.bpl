procedure byteback.dummy.condition.Simple.returnsOne##() returns (~ret: int)
  ensures ~ret == 1;
{
  ~ret := 1;
  return;
}

procedure byteback.dummy.condition.Simple.identity#int#(a: int) returns (~ret: int)
  ensures ~ret == a;
{
  ~ret := a;
  return;
}
