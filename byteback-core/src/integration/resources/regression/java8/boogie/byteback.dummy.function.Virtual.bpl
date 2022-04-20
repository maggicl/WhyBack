procedure byteback.dummy.function.Virtual.$init$##(this: Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

function byteback.dummy.function.Virtual.getThis##(~heap: Store, this: Reference) returns (Reference)
{
    this
}

function byteback.dummy.function.Virtual.getThat#byteback.dummy.function.Virtual#(~heap: Store, this: Reference, that: Reference) returns (Reference)
{
    byteback.dummy.function.Virtual.getThis##(~heap, that)
}
