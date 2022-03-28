const byteback.dummy.procedure.Field.field: Field int;

procedure byteback.dummy.procedure.Field.assignsField##(this: Reference) returns ()
{
  ~heap := ~update(~heap, this, byteback.dummy.procedure.Field.field, 1);
  return;
}
