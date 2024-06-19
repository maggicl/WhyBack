# WhyBack test suite

To run the tests run the commands:

```shell
gradle :byteback-test:test-java-8:system
gradle :byteback-test:test-java-17:system
gradle :byteback-test:test-kotlin-1.8.0:system
gradle :byteback-test:test-scala-2.13.8:system
```

To run a specific test run the command:

```shell
gradle -P TEST_TARGET="$(scripts/list.sh | grep $filename)" :byteback-test:$project:system
```

where `$filename` is a pattern to find the test filename and `$project` is name of the Gradle subproject where the test 
is located (i.e. `test-...`).

To collect the Why3 output in a single csv file run the command:

```shell
scripts/results.sh
```

cvc4 cvc5 alt-ergo gappa? z3 (different versions 2-3) 

alt-ergo polynomial
floating point/machine integer examples (gappa)
vampire abstract method with complex quantifier in the spec. Other method calls that method
    (forall x. x > a -> result > x) (maybe with types ? partial order)
method that returns the minimum element in an array and postcondition (exists k. 0 <= k < length && a[k] = result)