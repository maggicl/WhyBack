ByteBack is a tool for specifying bytecode programs and for converting
them into the Boogie intermediate verification language.

# Manual Setup

## Building

To build the project use the following command:

``` bash
./gradlew install
```

This command will build the .jar archive of the command line application
`byteback-cli` in
[./byteback-cli/build/libs/byteback-cli.jar](./byteback-cli/build/libs/byteback-cli.jar),
and for `byteback-annotations` (BBLib) in
[./byteback-annotations/build/libs/byteback-annotations.jar](./byteback-annotations/build/libs/byteback-annotations.jar).
It is possible to call `byteback-cli` by using the execution script,
which will be located in
[./byteback-cli/build/install/byteback-cli/bin/byteback-cli](./byteback-cli/build/install/byteback-cli/bin/byteback-cli).

## Dependencies <span id="subsec:dependencies"></span>

In order to verify programs using ByteBack the following dependencies
need to be installed separately:

-   [Z3](https://github.com/Z3Prover/z3) solver 4.11.2+
-   [boogie](https://github.com/boogie-org/boogie) 2.15.8+

Z3 can be installed with pypi using the
[./requirements.txt](./requirements.txt) file, while Boogie can be
installed by using the [.NET SDK
6](https://dotnet.microsoft.com/en-us/download/dotnet/6.0), issuing the
following command:

``` bash
dotnet tool install -g boogie
```

### Testing

Most of the tests performed by ByteBack are system tests that use
`byteback-cli` on the classes located in the
[byteback-test](./byteback-test) subprojects. These tests requires the
following additional pypi dependencies (also listed at
[./byteback-test/scripts/requirements.txt](./byteback-test/scripts/requirements.txt)):

-   [lit](https://llvm.org/docs/CommandGuide/lit.html) 15.0.0
-   [filecheck](https://llvm.org/docs/CommandGuide/FileCheck.html)
    0.0.22

### Experiments

Experiments are performed using the scripts located in
[./byteback-test/scripts](./byteback-test/scripts). To run the
experiments the following Python dependencies are required:

-   [pandas](https://pandas.pydata.org/) 1.4.3
-   [click](https://click.palletsprojects.com/en/8.1.x/) 8.1.3

These dependencies are listed in
[./byteback-test/scripts/requirements.txt](./byteback-test/scripts/requirements.txt)

# Usage

## Using ByteBack's CLI

`byteback-cli` is a command line interface for the converter from Java
bytecode into the Boogie intermediate verification language. The
`byteback-cli` executable can be invoked with the following options:

``` bash
byteback-cli -cp {classpath...} -c {target class...}
   -o {output Boogie file...} [--nct] [--act]
```

classpath  
The `-cp` option declares a classpath to be analyzed by ByteBack. This
option can be repeated to specify multiple classpaths.

starting class  
The `-c` option declares the fully qualified name of the class from
which the analysis will start (i.e. the classes from which ByteBack will
start the conversion). This option can be repeated to declare multiple
starting classes. By including a single class, ByteBack will also
recursively perform the conversion of any eventual class referenced by
that class, excluding standard library classes.

output file  
The `-o` option will correspond to the path to the output Boogie file.
If this option is absent the Boogie program will be printed to stdout.

prelude  
The `-p` option allows the user to specify an alternative prelude to the
default one located in
[./byteback-cli/src/main/resources/boogie/BytebackPrelude.bpl](./byteback-cli/src/main/resources/boogie/BytebackPrelude.bpl).

implicit exceptions  
Optionally it is possible to add the `--nct` option to model implicit
runtime null pointer exceptions and the `--act` option to model implicit
runtime index out-of-bounds exceptions.

After generating the output Boogie program it can be verified using the
`boogie` verification tool.

## Using ByteBack annotations

The ByteBack annotations library (BBLib) contains the necessary
annotations and static methods used to specify bytecode programs.

In order to convert the annotated code, `byteback-annotations` must be
included in the classpath passed to `byteback-cli`.

## Running the Tests <span id="sec:tests"></span>

To run the system tests execute the `system` gradle task as follows:

``` bash
./gradlew system
```

It is also possible to specify a single test by using the `TEST_TARGET`
property and the following command from within the test project
directory (e.g.
[./byteback-test/test-java-8](./byteback-test/test-java-8)):

``` bash
./gradlew system -PTARGET={Path to the test file (.java, .scala, .kt)}
```

An additional property `TEST_JOBS` can be specified to run the tests in
parallel jobs. The default value is one.

``` bash
./gradlew system -PTEST_JOBS=4 # runs the tests on 4 parallel jobs
```

## Running the Experiments <span id="sec:experiments"></span>

For a detailed overview on how to run and interpret the experiments
refer to [./REPLICATION.md](./REPLICATION.md).

In short, to run the experiments and format the results run the
following gradle task:

``` bash
./gradlew results
```

This command will produce output in
[./byteback-test/build/experiments/](./byteback-test/build/experiments/).
The results of the experiments can be found in the file `results.csv`.
The CSV reports the statistics of each experiments computed over 5 runs
of `byteback-cli` and `boogie`. To change the number of runs it is
possible to pass the number of repetitions with the property
`EXP_REPETITIONS`.

``` bash
./gradlew results -PEXP_REPETITIONS=1 # Repeat experiment only once
```

The experiments are ran sequentially, hence lower values for the
`EXP_REPETITIONS` parameter will decrease the computation time of the
experimental results considerably.

# Example of usage

ByteBack can be used through a command-line tool `byteback-cli`.
Instructions on how to build and use this tool can be found in
[sec:manual-setup](sec:manual-setup). If the tests or the experiments
have already been ran it will be possible to find the tool in.
[./byteback-cli/build/install/byteback-cli/bin/byteback-cli](./byteback-cli/build/install/byteback-cli/bin/byteback-cli).

For general users, the simplest way to use ByteBack is through a
command-line tool `byteback-cli`. Running the system tests ([sec:tests])
and/or replicating the paper's experiments ([sec:experiments]) 
also builds `byteback-cli`. Otherwise, Section 2 of this
documents explains in greater detail how to build and use
`byteback-cli`.

Here we show how to use `byteback-cli` to verify a Java program
`Main.java` located in [./example](./example).

`Main.java` contains a single method `positive_sum` with pre- and
postcondition:

``` java
@Predicate
public boolean positive_arguments(int a, int b) {
    return gte(a, 0) & gte(b, 0);
}

@Predicate
public boolean positive_return(int a, int b, int returns) {
    return gte(returns, 0);
}

@Require("positive_arguments")   // a >= 0 && b >= 0
@Ensure("positive_return")       // returns >= 0
public int positive_sum(int a, int b) {
    return a + b;
}
```

The class must be compiled with `BBlib`, which can be found in
[./byteback-annotations/build/libs/byteback-annotations.jar](./byteback-annotations/build/libs/byteback-annotations.jar).
From the [./examples](./examples) directory the command to compile
`Main` is therefore:

First, compile `Main.java` with the standard Java compiler, including
ByteBack's specification library `BBlib` at
[./byteback-annotations/build/libs/byteback-annotations.jar](./byteback-annotations/build/libs/byteback-annotations.jar).
From the [./examples](./examples) directory:

``` bash
javac Main.java
  -cp ../byteback-annotations/build/libs/byteback-annotations.jar
```

Then, run `byteback-cli` on the generated bytecode:

``` bash
../byteback-cli/build/install/byteback-cli/bin/byteback-cli
  -cp ../byteback-annotations/build/libs/byteback-annotations.jar
    -cp .
    -c Main
    -o Main.bpl
```

This generated Boogie program `Main.bpl`, which encodes the Java
program's semantics and its specification in Boogie. To finally perform
verification, run the Boogie verification tool on `Main.bpl`:

``` bash
boogie Main.bpl
```

# Citing

To cite ByteBack refer to the paper:

> Verifying Functional Correctness Properties At the Level of Java Bytecode
> by Marco Paganoni and Carlo A. Furia

```bibtex
@InProceedings{PF-FM23-ByteBack,
  author = {Marco Paganoni and Carlo A. Furia},
  title = {Verifying Functional Correctness Properties At the Level of {J}ava Bytecode},
  booktitle = {Proceedings of the 25th International Symposium on Formal Methods (FM)},
  year = {2023},
  editor = {Marsha Chechik and Joost-Pieter Katoen and Martin Leucker},
  series = {Lecture Notes in Computer Science},
  pages = {343--363},
  volume = {14000},
  month = {March},
  publisher = {Springer},
  acceptancerate = {29\%}
}
```

Additional he following paper dives more closely into the verification
of bytecode programs involving exceptions:

> Reasoning About Exceptional Behavior At the Level of Java Bytecode
> by Marco Paganoni and Carlo A. Furia

```bibtex
@InProceedings{PF-FM23-ByteBack,
  author = {Marco Paganoni and Carlo A. Furia},
  title = {Reasoning About Exceptional Behavior At the Level of {J}ava Bytecode},
  booktitle = {Proceedings of the 18th International Conference on integrated Formal Methods (iFM)},
  year = {2023},
  editor = {Paula Herber and Anton Wijs and Marcello M. Bonsangue},
  series = {Lecture Notes in Computer Science},
  pages = {113--133},
  volume = {14300},
  month = {November},
  publisher = {Springer},
  acceptancerate = {33\%}
}
```
