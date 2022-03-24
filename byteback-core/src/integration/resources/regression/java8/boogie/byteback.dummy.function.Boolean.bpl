function byteback.dummy.function.Boolean.or#boolean#boolean#(~heap: Store, a: bool, b: bool) returns (bool)
{
    a || b
}

function byteback.dummy.function.Boolean.and#boolean#boolean#(~heap: Store, a: bool, b: bool) returns (bool)
{
    a && b
}

function byteback.dummy.function.Boolean.xor#boolean#boolean#(~heap: Store, a: bool, b: bool) returns(bool)
{
    a != b
}

function byteback.dummy.function.Boolean.returnsTrue##(~heap: Store) returns(bool)
{
    true
}

function byteback.dummy.function.Boolean.returnsFalse##(~heap: Store) returns(bool)
{
    false
}
