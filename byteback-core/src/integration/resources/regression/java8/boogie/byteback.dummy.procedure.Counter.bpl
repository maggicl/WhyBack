const unique $byteback.dummy.procedure.Counter : Type;

const unique $byteback.dummy.procedure.Counter.count : Field (int);

procedure byteback.dummy.procedure.Counter.main##() returns ()
	modifies ~heap;
{
  var $$stack1 : Reference where (~typeof(~heap, $$stack1) == $byteback.dummy.procedure.Counter);
  var $counter : Reference where (~typeof(~heap, $counter) == $byteback.dummy.procedure.Counter);
  call $$stack1 := ~new($byteback.dummy.procedure.Counter);
  call byteback.dummy.procedure.Counter.$init$##($$stack1);
  $counter := $$stack1;
  call byteback.dummy.procedure.Counter.increment##($counter);
  call byteback.dummy.procedure.Counter.countTo10##($counter);
  call byteback.dummy.procedure.Counter.countTo10Indirectly##($counter);
  return;
}

procedure byteback.dummy.procedure.Counter.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Counter)) returns ()
	modifies ~heap;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Counter);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.procedure.Counter.count, 0);
  return;
}

procedure byteback.dummy.procedure.Counter.increment##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Counter)) returns ()
	modifies ~heap;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Counter);
  $this := ?this;
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.procedure.Counter.count, (~heap.read(~heap, $this, $byteback.dummy.procedure.Counter.count) + 1));
  return;
}

procedure byteback.dummy.procedure.Counter.countTo10##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Counter)) returns ()
	modifies ~heap;
{
  var $i : int;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Counter);
  $this := ?this;
  $i := 0;
label2:
  if (($i >= 10)) {
    goto label1;
  }
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.procedure.Counter.count, (~heap.read(~heap, $this, $byteback.dummy.procedure.Counter.count) + 1));
  $i := ($i + 1);
  goto label2;
label1:
  return;
}

procedure byteback.dummy.procedure.Counter.countTo10Indirectly##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Counter)) returns ()
	modifies ~heap;
{
  var $i : int;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Counter);
  $this := ?this;
  $i := 0;
label2:
  if (($i >= 10)) {
    goto label1;
  }
  call byteback.dummy.procedure.Counter.increment##($this);
  $i := ($i + 1);
  goto label2;
label1:
  return;
}
