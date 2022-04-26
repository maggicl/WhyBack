const unique byteback.dummy.function.Field.field: Field int;

procedure byteback.dummy.function.Field.$init$##(this: Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

function byteback.dummy.function.Field.staticFieldReference##(~heap: Store, this: Reference) returns (int)
{
   ~read(~heap, byteback.dummy.function.Field, byteback.dummy.function.Field.staticField)
}

function byteback.dummy.function.Field.staticFieldSum##(~heap: Store, this: Reference) returns (int)
{
   ~read(~heap, byteback.dummy.function.Field, byteback.dummy.function.Field.staticField) + 2
}

function byteback.dummy.function.Field.fieldReference##(~heap: Store, this: Reference) returns (int)
{
   ~read(~heap, this, byteback.dummy.function.Field.field)
}

function byteback.dummy.function.Field.fieldSum##(~heap: Store, this: Reference) returns (int)
{
   ~read(~heap, this, byteback.dummy.function.Field.field) + 2
}
