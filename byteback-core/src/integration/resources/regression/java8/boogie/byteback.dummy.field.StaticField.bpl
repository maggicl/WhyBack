const unique $byteback.dummy.field.StaticField : Type;

const unique $byteback.dummy.field.StaticField.booleanField : Field (bool);

const unique $byteback.dummy.field.StaticField.byteField : Field (int);

const unique $byteback.dummy.field.StaticField.shortField : Field (int);

const unique $byteback.dummy.field.StaticField.intField : Field (int);

const unique $byteback.dummy.field.StaticField.longField : Field (int);

const unique $byteback.dummy.field.StaticField.floatField : Field (real);

const unique $byteback.dummy.field.StaticField.doubleField : Field (real);

const unique $byteback.dummy.field.StaticField.referenceField : Field (Reference);

procedure byteback.dummy.field.StaticField.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.field.StaticField)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.field.StaticField);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

procedure byteback.dummy.field.StaticField.initialized##() returns (~ret : bool)
{
  var $$stack11 : Reference where (~typeof(~heap, $$stack11) == $java.lang.Object);
  var $$stack2 : bool;
  if ((~int(~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.booleanField)) != 0)) {
    goto label8;
  }
  if ((~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.byteField) != 1)) {
    goto label8;
  }
  if ((~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.shortField) != 1)) {
    goto label8;
  }
  if ((~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.intField) != 1)) {
    goto label8;
  }
  if ((~cmp(~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.longField), 1) != 0)) {
    goto label8;
  }
  if ((~cmp(~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.floatField), 1.0) != 0)) {
    goto label8;
  }
  if ((~cmp(~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.doubleField), 1.0) != 0)) {
    goto label8;
  }
  $$stack11 := ~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.referenceField);
  if ((~heap.read(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.referenceField) != ~null)) {
    goto label8;
  }
  $$stack2 := true;
  goto label9;
label8:
  $$stack2 := false;
label9:
  ~ret := $$stack2;
  return;
}

procedure byteback.dummy.field.StaticField.initialize##() returns ()
	modifies ~heap;
{
  ~heap := ~heap.update(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.booleanField, false);
  ~heap := ~heap.update(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.byteField, 1);
  ~heap := ~heap.update(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.shortField, 1);
  ~heap := ~heap.update(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.intField, 1);
  ~heap := ~heap.update(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.longField, 1);
  ~heap := ~heap.update(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.floatField, 1.0);
  ~heap := ~heap.update(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.doubleField, 1.0);
  ~heap := ~heap.update(~heap, ~type.reference($byteback.dummy.field.StaticField), $byteback.dummy.field.StaticField.referenceField, ~null);
  return;
}
