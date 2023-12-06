# Replication

These are the instructions on how to run ByteBack's replication
package for the paper:

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
