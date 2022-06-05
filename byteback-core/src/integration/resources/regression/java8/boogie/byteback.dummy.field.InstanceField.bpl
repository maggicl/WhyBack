const unique $byteback.dummy.field.InstanceField : Type;

const unique $byteback.dummy.field.InstanceField.booleanField : Field (bool);

const unique $byteback.dummy.field.InstanceField.byteField : Field (int);

const unique $byteback.dummy.field.InstanceField.shortField : Field (int);

const unique $byteback.dummy.field.InstanceField.intField : Field (int);

const unique $byteback.dummy.field.InstanceField.longField : Field (int);

const unique $byteback.dummy.field.InstanceField.floatField : Field (real);

const unique $byteback.dummy.field.InstanceField.doubleField : Field (real);

const unique $byteback.dummy.field.InstanceField.referenceField : Field (Reference);

procedure byteback.dummy.field.InstanceField.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.field.InstanceField)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.field.InstanceField);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}