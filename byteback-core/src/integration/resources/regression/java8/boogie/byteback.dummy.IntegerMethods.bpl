function byteback.dummy.IntegerMethods.addition#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a + b
}

function byteback.dummy.IntegerMethods.subtraction#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a - b
}

function byteback.dummy.IntegerMethods.multiplication#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a * b
}

// TODO: Model integer division
function byteback.dummy.IntegerMethods.division#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a / b
}

function byteback.dummy.IntegerMethods.modulo#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    a % b
}

function byteback.dummy.IntegerMethods.square#int#(~heap: Store, a: int) returns (int)
{
    a * a
}

function byteback.dummy.IntegerMethods.squareArea#int#(~heap: Store, a: int) returns (int)
{
    byteback.dummy.IntegerMethods.square#int#(~heap, a)
}

function byteback.dummy.IntegerMethods.rectangleArea#int#int#(~heap: Store, a: int, b: int) returns (int)
{
    byteback.dummy.IntegerMethods.multiplication#int#int#(~heap, a, b)
}

function byteback.dummy.IntegerMethods.even#int#(~heap: Store, a: int) returns (bool)
{
    eq(~heap, (a % 2), 0)
}

function byteback.dummy.IntegerMethods.odd#int#(~heap: Store, a: int) returns (bool)
{
    not(~heap, byteback.dummy.IntegerMethods.even#int#(~heap, a))
}

function byteback.dummy.IntegerMethods.assignIndirect#int#(~heap: Store, a: int) returns (int)
{
    a
}

function byteback.dummy.IntegerMethods.assignPlus#int#(~heap: Store, a: int) returns (int)
{
    a + 1
}

function byteback.dummy.IntegerMethods.assignPlusIndirect#int#(~heap: Store, a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}


function byteback.dummy.IntegerMethods.nestedPlus#int#(~heap: Store, a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}

function byteback.dummy.IntegerMethods.assignPlusIndirectVariables#int#(~heap: Store, a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}

function byteback.dummy.IntegerMethods.commonSubExpressionPlus#int#(~heap: Store, a: int) returns (int)
{
    (a + 1) + (a + 1)
}
