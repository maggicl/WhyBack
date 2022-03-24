const byteback.dummy.function.Field.field: Field int;

function byteback.dummy.function.Field.fieldReference##(~heap: Store, this: Reference) returns (int)
{
   ~read(~heap, this, byteback.dummy.function.Field.field)
}

function byteback.dummy.function.Field.fieldSum##(~heap: Store, this: Reference) returns (int)
{
   ~read(~heap, this, byteback.dummy.function.Field.field) + 2
}
