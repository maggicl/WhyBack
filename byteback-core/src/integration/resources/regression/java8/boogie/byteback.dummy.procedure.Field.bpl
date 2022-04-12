const byteback.dummy.procedure.Field.field: Field int;

const byteback.dummy.procedure.Field.object: Field Reference;

procedure byteback.dummy.procedure.Field.assignsField##(this: Reference) returns ()
  modifies ~heap;
{
  ~heap := ~update(~heap, this, byteback.dummy.procedure.Field.field, 1);
  return;
}

procedure byteback.dummy.procedure.Field.assignsObject##(this: Reference) returns ()
  modifies ~heap;
{
  ~heap := ~update(~heap, this, byteback.dummy.procedure.Field.object, ~null);
  return;
}
