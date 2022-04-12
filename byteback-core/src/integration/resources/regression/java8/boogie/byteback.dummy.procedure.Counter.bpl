const byteback.dummy.procedure.Counter.count: Field int;

procedure byteback.dummy.procedure.Counter.increment##(this : Reference) returns ()
  modifies ~heap;
{
  ~heap := ~update(~heap, this, byteback.dummy.procedure.Counter.count,
    ~read(~heap, this, byteback.dummy.procedure.Counter.count) + 1);
  return;
}

procedure byteback.dummy.procedure.Counter.countTo10##(this : Reference) returns ()
  modifies ~heap;
{
  var i: int;
  i := 0;

label2:
  if ((i >= 10)) {
    goto label1;
  }
  ~heap := ~update(~heap, this, byteback.dummy.procedure.Counter.count,
    ~read(~heap, this, byteback.dummy.procedure.Counter.count) + 1);
  i := i + 1;
  goto label2;

label1:
  return;
}

procedure byteback.dummy.procedure.Counter.countTo10Indirectly##(this : Reference) returns ()
{
  var i: int;
  i := 0;

label2:
  if ((i >= 10)) {
    goto label1;
  }
  call byteback.dummy.procedure.Counter.increment##(this);
  i := i + 1;
  goto label2;

label1:
  return;
}

