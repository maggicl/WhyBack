const unique $byteback.dummy.function.Virtual : Type;

procedure byteback.dummy.function.Virtual.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.function.Virtual)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.function.Virtual);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

function byteback.dummy.function.Virtual.getThis##(~heap : Store, $this : Reference) returns (Reference) { $this }

function byteback.dummy.function.Virtual.getThat#byteback.dummy.function.Virtual#(~heap : Store, $this : Reference, $that : Reference) returns (Reference) { byteback.dummy.function.Virtual.getThis##(~heap, $that) }