const unique $byteback.dummy.function.Real : Type;

procedure byteback.dummy.function.Real.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.function.Real)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.function.Real);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

function byteback.dummy.function.Real.division#double#double#(~heap : Store, $a : real, $b : real) returns (real) { ($a / $b) }

function byteback.dummy.function.Real.division#float#float#(~heap : Store, $a : real, $b : real) returns (real) { ($a / $b) }

function byteback.dummy.function.Real.multiplication#double#double#(~heap : Store, $a : real, $b : real) returns (real) { ($a * $b) }

function byteback.dummy.function.Real.multiplication#float#float#(~heap : Store, $a : real, $b : real) returns (real) { ($a * $b) }

function byteback.dummy.function.Real.circleArea#double#(~heap : Store, $r : real) returns (real) { ((3.14 * $r) * $r) }