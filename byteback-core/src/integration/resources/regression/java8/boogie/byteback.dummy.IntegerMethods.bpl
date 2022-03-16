function byteback.dummy.IntegerMethods.addition#int#int#(a: int, b: int) returns (int)
{
    a + b
}

function byteback.dummy.IntegerMethods.subtraction#int#int#(a: int, b: int) returns (int)
{
    a - b
}

function byteback.dummy.IntegerMethods.multiplication#int#int#(a: int, b: int) returns (int)
{
    a * b
}

// TODO: Model integer division
function byteback.dummy.IntegerMethods.division#int#int#(a: int, b: int) returns (int)
{
    a / b
}

function byteback.dummy.IntegerMethods.modulo#int#int#(a: int, b: int) returns (int)
{
    a % b
}

function byteback.dummy.IntegerMethods.square#int#(a: int) returns (int)
{
    a * a
}

function byteback.dummy.IntegerMethods.squareArea#int#(a: int) returns (int)
{
    byteback.dummy.IntegerMethods.square#int#(a)
}

function byteback.dummy.IntegerMethods.rectangleArea#int#int#(a: int, b: int) returns (int)
{
    byteback.dummy.IntegerMethods.multiplication#int#int#(a, b)
}

function byteback.dummy.IntegerMethods.even#int#(a: int) returns (bool)
{
    eq((a % 2), 0)
}

function byteback.dummy.IntegerMethods.odd#int#(a: int) returns (bool)
{
    not(byteback.dummy.IntegerMethods.even#int#(a))
}

function byteback.dummy.IntegerMethods.assignIndirect#int#(a: int) returns (int)
{
    a
}

function byteback.dummy.IntegerMethods.assignPlus#int#(a: int) returns (int)
{
    a + 1
}

function byteback.dummy.IntegerMethods.assignPlusIndirect#int#(a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}


function byteback.dummy.IntegerMethods.nestedPlus#int#(a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}

function byteback.dummy.IntegerMethods.assignPlusIndirectVariables#int#(a: int) returns (int)
{
    a + 1 + 2 + 3 + 4 + 5
}

function byteback.dummy.IntegerMethods.commonSubExpressionPlus#int#(a: int) returns (int)
{
    (a + 1) + (a + 1)
}
