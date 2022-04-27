procedure byteback.dummy.function.Virtual.$init$##(this: Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

function byteback.dummy.function.Virtual.getThis##(this: Reference) returns (Reference)
{
    this
}

function byteback.dummy.function.Virtual.getThat#byteback.dummy.function.Virtual#(this: Reference, that: Reference) returns (Reference)
{
    byteback.dummy.function.Virtual.getThis##(that)
}
