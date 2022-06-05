const unique $byteback.dummy.condition.Counter : Type;

const unique $byteback.dummy.condition.Counter.count : Field (int);

procedure byteback.dummy.condition.Counter.main##() returns ()
	modifies ~heap;
{
  var $$stack1 : Reference where (~typeof(~heap, $$stack1) == $byteback.dummy.condition.Counter);
  var $counter : Reference where (~typeof(~heap, $counter) == $byteback.dummy.condition.Counter);
  call $$stack1 := ~new($byteback.dummy.condition.Counter);
  call byteback.dummy.condition.Counter.$init$##($$stack1);
  $counter := $$stack1;
  assert ~eq(~heap.read(~heap, $counter, $byteback.dummy.condition.Counter.count), 0);
  call byteback.dummy.condition.Counter.increment##($counter);
  assert ~eq(~heap.read(~heap, $counter, $byteback.dummy.condition.Counter.count), 1);
  call byteback.dummy.condition.Counter.countTo10##($counter);
  assert ~eq(~heap.read(~heap, $counter, $byteback.dummy.condition.Counter.count), 11);
  call byteback.dummy.condition.Counter.countTo10Indirectly##($counter);
  assert ~eq(~heap.read(~heap, $counter, $byteback.dummy.condition.Counter.count), 21);
  return;
}

procedure byteback.dummy.condition.Counter.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.condition.Counter)) returns ()
  ensures ~eq(~heap.read(~heap, ?this, $byteback.dummy.condition.Counter.count), 0);
	modifies ~heap;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.condition.Counter);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.condition.Counter.count, 0);
  return;
}

procedure byteback.dummy.condition.Counter.increment##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.condition.Counter)) returns ()
  ensures ~eq(~heap.read(~heap, ?this, $byteback.dummy.condition.Counter.count), (old(~heap.read(~heap, ?this, $byteback.dummy.condition.Counter.count)) + 1));
	modifies ~heap;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.condition.Counter);
  $this := ?this;
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.condition.Counter.count, (~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count) + 1));
  return;
}

procedure byteback.dummy.condition.Counter.countTo10##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.condition.Counter)) returns ()
  ensures ~eq(~heap.read(~heap, ?this, $byteback.dummy.condition.Counter.count), (old(~heap.read(~heap, ?this, $byteback.dummy.condition.Counter.count)) + 10));
	modifies ~heap;
{
  var $old_count : int;
  var $i : int;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.condition.Counter);
  $this := ?this;
  $old_count := ~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count);
  $i := 0;
  assert (~int.lte(0, $i) && ~int.lte($i, 10));
  assert ~eq(~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count), ($old_count + $i));
label2:
  assume (~int.lte(0, $i) && ~int.lte($i, 10));
  assume ~eq(~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count), ($old_count + $i));
  if (($i >= 10)) {
    goto label1;
  }
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.condition.Counter.count, (~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count) + 1));
  $i := ($i + 1);
  assert (~int.lte(0, $i) && ~int.lte($i, 10));
  assert ~eq(~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count), ($old_count + $i));
  goto label2;
label1:
  assume (~int.lte(0, $i) && ~int.lte($i, 10));
  assume ~eq(~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count), ($old_count + $i));
  return;
}

procedure byteback.dummy.condition.Counter.countTo10Indirectly##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.condition.Counter)) returns ()
  ensures ~eq(~heap.read(~heap, ?this, $byteback.dummy.condition.Counter.count), (old(~heap.read(~heap, ?this, $byteback.dummy.condition.Counter.count)) + 10));
	modifies ~heap;
{
  var $old_count : int;
  var $i : int;
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.condition.Counter);
  $this := ?this;
  $old_count := ~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count);
  $i := 0;
  assert (~int.lte(0, $i) && ~int.lte($i, 10));
  assert ~eq(~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count), ($old_count + $i));
label2:
  assume (~int.lte(0, $i) && ~int.lte($i, 10));
  assume ~eq(~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count), ($old_count + $i));
  if (($i >= 10)) {
    goto label1;
  }
  call byteback.dummy.condition.Counter.increment##($this);
  $i := ($i + 1);
  assert (~int.lte(0, $i) && ~int.lte($i, 10));
  assert ~eq(~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count), ($old_count + $i));
  goto label2;
label1:
  assume (~int.lte(0, $i) && ~int.lte($i, 10));
  assume ~eq(~heap.read(~heap, $this, $byteback.dummy.condition.Counter.count), ($old_count + $i));
  return;
}
