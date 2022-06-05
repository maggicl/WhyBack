const unique $byteback.dummy.procedure.Field : Type;

const unique $byteback.dummy.procedure.Field.field : Field (int);

const unique $byteback.dummy.procedure.Field.object : Field (Reference);

procedure byteback.dummy.procedure.Field.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Field)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Field);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

procedure byteback.dummy.procedure.Field.assignsField##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Field)) returns ()
	modifies ~heap;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Field);
  $this := ?this;
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.procedure.Field.field, 1);
  return;
}

procedure byteback.dummy.procedure.Field.assignsObject##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.procedure.Field)) returns ()
	modifies ~heap;
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.procedure.Field);
  $this := ?this;
  ~heap := ~heap.update(~heap, $this, $byteback.dummy.procedure.Field.object, ~null);
  return;
}
