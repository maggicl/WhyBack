const unique $byteback.dummy.function.Simple : Type;

procedure byteback.dummy.function.Simple.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.function.Simple)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.function.Simple);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

function byteback.dummy.function.Simple.universalQuantifier##(~heap : Store) returns (bool) { (forall $i : int :: true) }

function byteback.dummy.function.Simple.existentialQuantifier##(~heap : Store) returns (bool) { (exists $i : int :: true) }