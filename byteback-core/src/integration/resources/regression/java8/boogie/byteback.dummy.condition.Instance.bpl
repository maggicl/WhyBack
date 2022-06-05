const unique $byteback.dummy.condition.Instance : Type;

procedure byteback.dummy.condition.Instance.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.condition.Instance)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.condition.Instance);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

procedure byteback.dummy.condition.Instance.isObject##() returns ()
{
  var $$stack1 : Reference where (~typeof(~heap, $$stack1) == $java.lang.Object);
  var $object : Reference where (~typeof(~heap, $object) == $java.lang.Object);
  call $$stack1 := ~new($java.lang.Object);
  call java.lang.Object.$init$##($$stack1);
  $object := $$stack1;
  assert ~instanceof($$stack1, java.lang.Object);
  return;
}