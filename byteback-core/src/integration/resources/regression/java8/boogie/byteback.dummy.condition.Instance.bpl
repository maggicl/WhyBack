procedure byteback.dummy.condition.Instance.isObject##()
{
	var $stack1 : Reference;
	call $stack1 := ~new(java.lang.Object);
	call java.lang.Object.$init$##($stack1);
	assert ~instanceof(~heap, $stack1, java.lang.Object);
	return;
}
