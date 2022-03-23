procedure byteback.dummy.Unit.voidMethod##(this: Reference) returns ()
{
  return;
}

procedure byteback.dummy.Unit.singleAssignmentMethod##(this: Reference) returns ()
{
  var a: int;
  a := 1;
}

procedure byteback.dummy.Unit.doubleAssignmentMethod##(this: Reference) returns ()
{
  var a: int;
  var a#2: int;
  a := 0;
  a#2 := a + 42;
  return;
}
