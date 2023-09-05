ByteBack is a tool for specifying bytecode programs and for converting
them into the Boogie intermediate verification language.

# Replication

This is the ByteBack's replication package for the paper:

> Reasoning About Exceptional Behavior At the Level of Java Bytecode
>
> by Marco Paganoni and Carlo A. Furia

This section contains the instruction on how to setup and run the
version of the ByteBack tool described in the paper, and how to re-run
the experiments described in the paper.

## Summary

ByteBack's experiments consist of a set of specified programs written in
Java 8, Java 17, Scala 2.3, and Kotlin 1.8, which are located in the
subproject at [./byteback-test](./byteback-test). Each program is
specified using BBLib, which is located in the subproject at
[./byteback-annotations](./byteback-annotations).

### Citing

To cite this artifact refer to the DOI.

``` bib
@misc{ByteBack-Replication-Package-iFM2023,
    author       = {Marco Paganoni and
                                    Carlo A. Furia},
    title        = {ByteBack iFM 2023 Replication Package},
    month        = aug,
    year         = 2023,
    publisher    = {Zenodo},
    doi          = {10.5281/zenodo.8279413},
    url          = {https://doi.org/10.5281/zenodo.8279413}
}
```

## Setup

The recommended way to use ByteBack (in particular, installing it in
the [iFM 2023 virtual machine](https://doi.org/10.5281/zenodo.7782241))
is through a Docker image.  The image is available in this replication
package (directory <./image/>), as well as on DockerHub
(<`paganma/byteback:ifm23`>).

The following two subsections describe how to install and run in a
container ByteBack's Docker image: [using this replication package's
offline copy](#offline-setup) or [pulling from
DockerHub](#setup-with-docker-and-dockerhub).

### Offline setup

See <./image/README.md> for instructions on how to install and run the
local copy of ByteBack's Docker image. Then, follow the instructions
in the rest of this document to test the installation and replicate
the results.

### Setup with Docker and DockerHub

Assuming you have [Docker](https://www.docker.com) installed and are
connected to the internet, download and run ByteBack's Docker image
with:

```bash
docker run -it paganma/byteback:ifm23
```

This command runs a shell in a container with ByteBack and this
replication package's content installed. Follow the instructions in
the rest of this document to test the installation and replicate the
results.

## Resource requirements

This replication package does not have specific hardware requirements.

For smoothly running the experiments we suggest a configuration of at
least 2 virtual CPU cores and 8GB of RAM.

We ran this replication package in iFM 2023's virtual machine on a
laptop with a CPU i7-7600U 4x 3.9Ghz and 8GB of RAM.  With the default
settings the replication of the paper's experiments (task `results`)
completed in 43 minutes.

For reference, running the same experiments in the same virtual
machine on better hardware (CPU i9-12950HX 24x 4.9Ghz, 64 GB of RAM)
took 24 minutes.

## Test instructions

ByteBack comes with an extensive collection of system tests, which run
some simple verification tasks in [./byteback-test](./byteback-test).
The system tests check that the verification tasks are correctly
translated into Boogie, and that the generated Boogie programs verify.

To run the system tests, trigger Gradle task `system`:

``` bash
./gradlew system
```

These tests should terminate executing without reporting any failures,
printing the message: `BUILD SUCCESSFUL`.

## Replication instructions

This replication package allows users to replicate the results of the
experiments described in Section 4 of the paper. More precisely, the
paper's Table 4 summarizes the experimental results; in the following,
we describe how to reproduce that table's content.

The experiments run the annotated programs in
[./byteback-test](./byteback-test). Experiments are subdivided in
groups, corresponding to Table 4's column `LANG`: `j8` for Java 8
programs; `j17` for Java 17 programs; `s2` for Scala 2.13.8 programs;
`k18` for Kotlin 1.8 programs.

To compute the experiments execute Gradle task `results`:

``` bash
./gradlew results
```

Execution should terminate without reporting any failures, printing the
message: `BUILD SUCCESSFUL`.

Upon successful execution, file
[./byteback-test/build/experiments/results.csv](./byteback-test/build/experiments/results.csv)
will store the data corresponding to Table 4, as we detail next.

The CSV file `results.csv` includes one row for each verified program,
in the same order as the paper's Table 4. Each row has the columns
described below. In the following list of column names, we put between
square brackets the name of the corresponding column in the paper's
Table 4 if one exists (several of the CSV columns are not reported in
Table 4).

### Metrics

For each of the experiments the results table shows the following
metrics:

SourceLinesOfCode [SOURCE SIZE]
The SLOCs of the experiment's source code.

BytecodeLinesOfCode   
The SLOCs of the experiment's bytecode (as given by `javap`).

BoogieLinesOfCode [BOOGIE SIZE]  
The SLOCs of the experiment's generated Boogie code.

MethodCount [MET]  
Number of methods in the experiment.

SpecRequireCount   
The number of `@Require` annotations used.

SpecEnsureCount   
Number of `@Ensure` annotations used.

SpecRaiseCount   
Number of `@Raise` annotations used.

SpecReturnCount   
Number of `@Return` annotations used.

SpecPredicateCount [ANNOTATIONS P]  
Number of `@Predicate` annotations used.

SpecPureCount   
Number of `@Pure` annotations used.

SpecAssertionCount   
Number of assertions specified.

SpecAssumptionCount   
Number of assumptionsn specified.

SpecInvariantCount   
Number of loop invariants specified.

UsesExceptionFeatures   
Whether the experiment uses exception-related features.

SpecExceptionCount [ANNOTATIONS E]  
Total number of exception-related annotations used.

SpecFunctionalCount [ANNOTATIONS S]  
Total number of functional (`@Require` and `Ensure`) annotations used.

SpecIntermediateCount   
Total number of intermediate annotations (`assertion` and loop
`invariant`) used.

ConversionTime [ENCODING TIME]  
Average time taken by ByteBack to convert the bytecode of the experiment
to Boogie.

VerificationTime [VERIFICATION TIME]  
Average time taken by Boogie to verify the Boogie code produced by
ByteBack.

ConversionOverhead   
Percentage of the overhead introduced by ByteBack for the conversion,
without accounting for Soot's initialization time.

The metrics `ConversionTime`, `VerificationTime`, `ConversionOverhead`
are computed as averages by running ByteBack and Boogie 5 times for each
of the experiments.

The paper's Table 4 also includes two bottom rows with **total** and
**average** of the quantitative results. The CSV file does not include
these, but they can simply be computed by summing or averaging the CSV's
columns.

### Reproducibility

With the exception of the performance metrics `ConversionTime`,
`VerificationTime`, and `ConversionOverhead`, all of the other metrics
should be consistent with the results reported in the paper's Table 4.

The performance metrics obviously depend on the hardware on which the
experiments were executed, as well as on any possible overhead due to
virtualization. Replicating the results should still reveal a
qualitative distribution of running times that is comparable to the one
shown in the paper's Table 4.

The performance metrics are expected to fluctuate in relation to the
performance of the hardware on which the experiments are being executed.

### Replication with Limited Resources

As mentioned above, to compute the performance metrics ByteBack and
Boogie are executed 5 times for each experiment. This can make the
execution of the experiments slow.

To change the number of runs it is possible to pass the property
`EXP_REPETITIONS` to the `results` task:

``` bash
./gradlew results -PEXP_REPETITIONS={Number of repetitions}
```

The `results` task depends on the `system` task used for running test.
By default the tests run on a single thread. To change this it is
possible to pass the parameter `TEST_JOBS`.

``` bash
./gradlew results -PTEST_JOBS={Number of jobs}
```

Finally, if the hardware running the experiments is much slower, some
experiments may fail because the Boogie verification tool hits its
default timeout. To increase the timeout time it is possible to pass the
parameter `BOOGIE_TIME_LIMIT` to `gradlew`.

``` bash
./gradlew results -PBOOGIE_TIME_LIMIT={Time in seconds}
```

## Example of usage

ByteBack can be used through a command-line tool `byteback-cli`.
Instructions on how to build and use this tool can be found in
[sec:manual-setup](sec:manual-setup). If the tests or the experiments
have already been ran it will be possible to find the tool in.
[./byteback-cli/build/install/byteback-cli/bin/byteback-cli](./byteback-cli/build/install/byteback-cli/bin/byteback-cli).

For general users, the simplest way to use ByteBack is through a
command-line tool `byteback-cli`. Running the system tests (Section 1.4
of this document) and/or replicating the paper's experiments (Section
1.5 of this document) also builds `byteback-cli`. Otherwise, Section 2
of this documents explains in greater detail how to build and use
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

# Manual Setup

<span id="sec:manual-setup"></span>

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

## Running the Tests

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

## Running the Experiments

To run the experiments and format the results run the following gradle
task:

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
