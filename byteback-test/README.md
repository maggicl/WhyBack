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