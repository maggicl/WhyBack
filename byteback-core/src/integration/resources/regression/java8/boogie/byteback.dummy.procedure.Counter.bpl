const byteback.dummy.procedure.Counter.count: Field int;

procedure byteback.dummy.procedure.Counter.increment##(this : Reference) returns ()
{
  var $stack1: int;
  var $stack2: int;
  $stack1 := ~read(~heap, this, byteback.dummy.procedure.Counter.count);
  $stack2 := $stack1 + 1;
  ~heap := ~update(~heap, this, byteback.dummy.procedure.Counter.count, $stack2);
  return;
}

procedure byteback.dummy.procedure.Counter.countTo10##(this : Reference) returns ()
{
  var $stack2: int;
  var $stack3: int;
  var i: int;
  i := 0;

label2:
  if ((i >= 10)) {
    goto label1;
  }
  $stack2 := ~read(~heap, this, byteback.dummy.procedure.Counter.count);
  $stack3 := $stack2 + 1;
  ~heap := ~update(~heap, this, byteback.dummy.procedure.Counter.count, $stack3);
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

