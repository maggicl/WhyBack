function byteback.dummy.function.Integer.addition#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a + b
}

function byteback.dummy.function.Integer.subtraction#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a - b
}

function byteback.dummy.function.Integer.multiplication#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a * b
}

// TODO: Model integer division
function byteback.dummy.function.Integer.division#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a / b
}

function byteback.dummy.function.Integer.modulo#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a mod b
}

function byteback.dummy.function.Integer.square#int#(~heap: Store, a: int) returns (int)
{
    a * a
}

function byteback.dummy.function.Integer.squareArea#int#(~heap: Store, a: int) returns (int)
{
    byteback.dummy.function.Integer.square#int#(~heap, a)
}

function byteback.dummy.function.Integer.rectangleArea#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    byteback.dummy.function.Integer.multiplication#int#int#(~heap, a, b)
}

function byteback.dummy.function.Integer.even#int#(~heap: Store, a: int) returns (bool)
{
    eq(~heap, (a mod 2), 0)
}

function byteback.dummy.function.Integer.odd#int#(~heap: Store, a: int) returns (bool)
{
    not(~heap, byteback.dummy.function.Integer.even#int#(~heap, a))
}

function byteback.dummy.function.Integer.assignIndirect#int#(~heap: Store, a: int) returns (int)
{
    a
}

function byteback.dummy.function.Integer.assignPlus#int#(~heap: Store, a: int) returns (int)
{
    a + 1
}

function byteback.dummy.function.Integer.assignPlusIndirect#int#(~heap: Store, a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}


function byteback.dummy.function.Integer.nestedPlus#int#(~heap: Store, a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}

function byteback.dummy.function.Integer.assignPlusIndirectVariables#int#(~heap: Store, a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}

function byteback.dummy.function.Integer.commonSubExpressionPlus#int#(~heap: Store, a: int) returns (int)
{
    (a + 1) + (a + 1)
}

function byteback.dummy.function.Integer.minus#int#(~heap: Store, a: int) returns (int)
{
    -a
}

function byteback.dummy.function.Integer.returnsOne##(~heap: Store) returns(int)
{
    1
}

function byteback.dummy.function.Integer.returnsZero##(~heap: Store) returns(int)
{
    0
}
