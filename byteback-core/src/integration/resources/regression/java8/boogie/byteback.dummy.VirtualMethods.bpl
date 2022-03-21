function byteback.dummy.VirtualMethods.getThis##(~heap: Store, this: Reference) returns (Reference)
{
    this
}

function byteback.dummy.VirtualMethods.getThat#byteback.dummy.VirtualMethods#(~heap: Store, this: Reference, that: Reference) returns (Reference)
{
    byteback.dummy.VirtualMethods.getThis##(~heap, that)
}
