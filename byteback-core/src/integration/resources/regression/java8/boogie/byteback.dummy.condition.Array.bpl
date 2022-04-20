procedure byteback.dummy.condition.Array.$init$##(this: Reference) returns ()
{
  call java.lang.Object.$init$##(this);
  return;
}

procedure byteback.dummy.condition.Array.sum#int?#(as: Reference) returns (~ret: int)
  ensures ~implies(~heap,
    (forall index : int :: ~implies(~heap,
                        ~and(~heap,
                         ~lt(~heap, index, ~lengthof(~heap, as)),
                         ~gte(~heap, index, 0)),
                        ~gte(~heap, ~get(~heap, as, ~Array.int, index), 0))),
    ~gte(~heap, ~ret, 0));
{
  var c: int;
  var l4: int;
  c := 0;
  l4 := 0;
label2:

  if (l4 >= ~lengthof(~heap, as)) {
    goto label1;
  }

  c := (c + ~get(~heap, as, ~Array.int, l4));
  l4 := (l4 + 1);

  goto label2;

label1:

  ~ret := c;

  return;
}

procedure byteback.dummy.condition.Array.assignsLastElement#int?#(as: Reference) returns ()
  ensures ~eq(~heap, ~get(~heap, as, ~Array.int, ~lengthof(~heap, as) - 1), 1);
{
  ~heap := ~insert(~heap, as, ~Array.int, ~lengthof(~heap, as) - 1, 1);
  return;
}
