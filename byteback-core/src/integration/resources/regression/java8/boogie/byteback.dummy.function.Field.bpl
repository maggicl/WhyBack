const byteback.dummy.function.Field.field: Field int;

procedure byteback.dummy.function.Field.$init$##(this: Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

function byteback.dummy.function.Field.fieldReference##(~heap: Store, this: Reference) returns (int)
{
   ~read(~heap, this, byteback.dummy.function.Field.field)
}

function byteback.dummy.function.Field.fieldSum##(~heap: Store, this: Reference) returns (int)
{
   ~read(~heap, this, byteback.dummy.function.Field.field) + 2
}
