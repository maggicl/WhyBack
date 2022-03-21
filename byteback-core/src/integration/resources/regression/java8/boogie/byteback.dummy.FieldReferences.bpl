function byteback.dummy.FieldReferences.fieldReference##(~heap: Store, this: Reference) returns(int)
{
   ~read(~heap, this, byteback.dummy.FieldReferences.field)
}

function byteback.dummy.FieldReferences.fieldSum##(~heap: Store, this: Reference) returns(int)
{
   ~read(~heap, this, byteback.dummy.FieldReferences.field) + 2
}
