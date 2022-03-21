function byteback.dummy.BooleanMethods.or#boolean#boolean#(~heap: Store, a: bool, b: bool) returns (bool)
{
    a || b
}

function byteback.dummy.BooleanMethods.and#boolean#boolean#(~heap: Store, a: bool, b: bool) returns (bool)
{
    a && b
}

function byteback.dummy.BooleanMethods.xor#boolean#boolean#(~heap: Store, a: bool, b: bool) returns(bool)
{
    a != b
}
