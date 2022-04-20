procedure byteback.dummy.function.Simple.$init$##(this: Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

function byteback.dummy.function.Simple.universalQuantifier##(~heap: Store) returns (bool)
{
  (forall i: int :: true)
}

function byteback.dummy.function.Simple.existentialQuantifier##(~heap: Store) returns (bool)
{
  (exists i: int :: true)
}
