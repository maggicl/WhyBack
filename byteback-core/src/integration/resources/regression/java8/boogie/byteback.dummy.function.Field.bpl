const unique $byteback.dummy.function.Field : Type;

const unique $byteback.dummy.function.Field.staticField : Field (int);

const unique $byteback.dummy.function.Field.field : Field (int);

procedure byteback.dummy.function.Field.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.function.Field)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.function.Field);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

function byteback.dummy.function.Field.staticFieldReference##(~heap : Store, $this : Reference) returns (int) { ~heap.read(~heap, ~type.reference($byteback.dummy.function.Field), $byteback.dummy.function.Field.staticField) }

function byteback.dummy.function.Field.staticFieldSum##(~heap : Store, $this : Reference) returns (int) { (~heap.read(~heap, ~type.reference($byteback.dummy.function.Field), $byteback.dummy.function.Field.staticField) + 2) }

function byteback.dummy.function.Field.fieldReference##(~heap : Store, $this : Reference) returns (int) { ~heap.read(~heap, $this, $byteback.dummy.function.Field.field) }

function byteback.dummy.function.Field.fieldSum##(~heap : Store, $this : Reference) returns (int) { (~heap.read(~heap, $this, $byteback.dummy.function.Field.field) + 2) }