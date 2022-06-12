procedure byteback.dummy.procedure.Switch.intSwitch#int#(?a: int) returns (~ret: int)
{
	var $b#2 : int;
	var $a : int;
	$a := ?a;
	if ((1 == $a)) {
		goto label1;
	}
	if ((2 == $a)) {
		goto label2;
	}
label1:
label2:
	$b#2 := 2;
	goto label3;
	$b#2 := 0;
label3:
	~ret := $b#2;
	return;
}

procedure byteback.dummy.procedure.Switch.enumSwitch#byteback.dummy.procedure.Switch$Fruit#(?a : Reference where (~typeof(~heap, ?a) == $byteback.dummy.procedure.Switch$Fruit)) returns (~ret : int)
{
	var $$stack2 : Reference where (~typeof(~heap, $$stack2) == ~array.type(~Primitive));
	var $$stack3 : int;
	var $b#2 : int;
	var $a : Reference where (~typeof(~heap, $a) == $byteback.dummy.procedure.Switch$Fruit);
	$a := ?a;
	$$stack2 := ~heap.read(~heap, ~type.reference($byteback.dummy.procedure.Switch$1), $byteback.dummy.procedure.Switch$1.$SwitchMap$byteback$dummy$procedure$Switch$Fruit);
	call $$stack3 := java.lang.Enum.ordinal##($a);
	if ((1 == (~unbox(~heap.read(~heap, ~heap.read(~heap, ~type.reference($byteback.dummy.procedure.Switch$1), $byteback.dummy.procedure.Switch$1.$SwitchMap$byteback$dummy$procedure$Switch$Fruit), ~element($$stack3))) : int))) {

	goto label2;
	}
	if ((2 == (~unbox(~heap.read(~heap, ~heap.read(~heap, ~type.reference($byteback.dummy.procedure.Switch$1), $byteback.dummy.procedure.Switch$1.$SwitchMap$byteback$dummy$procedure$Switch$Fruit), ~element($$stack3))) : int))) {
	  goto label3;
	}
label1:
label2:
	$b#2 := 2;
	goto label5;
label3:
	$b#2 := 3;
	goto label5;
	$b#2 := 0;
label5:
	~ret := $b#2;
   return;
}
