const unique $byteback.dummy.condition.Array : Type;

procedure byteback.dummy.condition.Array.$init$##(?this : Reference where (~typeof(~heap, ?this) == $byteback.dummy.condition.Array)) returns ()
{
  var $this : Reference where (~typeof(~heap, $this) == $byteback.dummy.condition.Array);
  $this := ?this;
  call java.lang.Object.$init$##($this);
  return;
}

procedure byteback.dummy.condition.Array.sum#int?#(?as : Reference where (~typeof(~heap, ?as) == ~array.type(~Primitive))) returns (~ret : int)
  ensures ~implies((forall $index : int :: ~implies(~and(~int.lt($index, ~lengthof(?as)), ~int.gte($index, 0)), ~int.gte((~unbox(~heap.read(~heap, ?as, ~element($index))) : int), 0))), ~int.gte(~ret, 0));
{
  var $l2 : Reference where (~typeof(~heap, $l2) == ~array.type(~Primitive));
  var $l3 : int;
  var $c : int;
  var $l4 : int;
  var $as : Reference where (~typeof(~heap, $as) == ~array.type(~Primitive));
  $as := ?as;
  $c := 0;
  $l2 := $as;
  $l3 := ~lengthof($l2);
  $l4 := 0;
label2:
  if (($l4 >= $l3)) {
    goto label1;
  }
  $c := ($c + (~unbox(~heap.read(~heap, $l2, ~element($l4))) : int));
  $l4 := ($l4 + 1);
  goto label2;
label1:
  ~ret := $c;
  return;
}

procedure byteback.dummy.condition.Array.assignsLastElement#int?#(?as : Reference where (~typeof(~heap, ?as) == ~array.type(~Primitive))) returns ()
  ensures ~eq((~unbox(~heap.read(~heap, ?as, ~element((~lengthof(?as) - 1)))) : int), 1);
	modifies ~heap;
{
  var $as : Reference where (~typeof(~heap, $as) == ~array.type(~Primitive));
  $as := ?as;
  ~heap := ~heap.update(~heap, $as, ~element((~lengthof($as) - 1)), ~box(1));
  return;
}
