**ByteBack** is a deductive verifier for Java that works at the level
of JVM bytecode.  It provides an annotation library to specify
programs at the source code level in a way that "survives" compilation
to bytecode.  Then, ByteBack encodes bytecode programs into
[Boogie](https://github.com/boogie-org/boogie), which performs the
actual verification.

By working on bytecode while supporting specification at the source
code level, ByteBack can accommodate a lot of Java features in a
uniform way.  In fact, it can verify programs written in recent Java
versions, and even supports some features of other JVM languages such
as [Kotlin](https://kotlinlang.org/) and
[Scala](https://www.scala-lang.org/).


# Installing ByteBack

## Docker image

The simplest way of using ByteBack is through its Docker image.

CAF: ADD INSTRUCTIONS HERE

## Manual setup

### Building

Build ByteBack using [Gradle](https://gradle.org/):

``` bash
./gradlew install
```

This command builds:

- The command line executable `byteback-cli` in 
  [./byteback-cli/build/install/byteback-cli/bin/byteback-cli](./byteback-cli/build/install/byteback-cli/bin/byteback-cli).
- The `.jar` archive with `byteback-cli`'s implementation in
  [./byteback-cli/build/libs/byteback-cli.jar](./byteback-cli/build/libs/byteback-cli.jar).
- The `byteback-annotations` library (BBLib) in
  [./byteback-annotations/build/libs/byteback-annotations.jar](./byteback-annotations/build/libs/byteback-annotations.jar).

### Dependencies <span id="subsec:dependencies"></span>

As mentioned above, ByteBack encodes bytecode programs into Boogie.
To verify ByteBack's Boogie output, you also need to install:

-   [Z3](https://github.com/Z3Prover/z3) solver 4.11.2+
-   [boogie](https://github.com/boogie-org/boogie) 2.15.8+

For example, Z3 can be installed through `pypi` using the
[./requirements.txt](./requirements.txt) file, while Boogie can be
installed by using the [.NET SDK
6](https://dotnet.microsoft.com/en-us/download/dotnet/6.0):

``` bash
dotnet tool install -g boogie
```

### Testing your manual setup

ByteBack includes system tests and verification experiments.
A [separate file](./EXPERIMENTS.md) describes how to run them.


# Using ByteBack

## An example of usage

Let's see how to use `byteback-cli` to verify the Java program
`Main.java` in [./example](./example).

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

To verify this simple program, go to directory [./example](./example)
and follow these steps:

1. Compile `Main.java`, including the `BBlib` `.jar` in the classpath:

``` bash
javac Main.java -cp ../byteback-annotations/build/libs/byteback-annotations.jar
```

  This generates bytecode for class `Main`.

2. Run ByteBack using `byteback-cli` on the generated bytecode:

``` bash
../byteback-cli/build/install/byteback-cli/bin/byteback-cli \
	-cp ../byteback-annotations/build/libs/byteback-annotations.jar \
	-cp . \
	-c Main \
	-o Main.bpl
```

  This generates a Boogie program `Main.bpl`, which encodes the Java
  program's semantics and its specification in Boogie. 
  
3. Finally, verify the generated Boogie program using the Boogie verifier:
  
``` bash
boogie Main.bpl
```

## Using ByteBack's CLI

`byteback-cli` is a command line tool that encodes Java bytecode into
Boogie programs. `byteback-cli` supports the following options:

``` bash
byteback-cli \
	  -cp CLASS_PATH \
	  -c TARGET_CLASS \
	[ -o BOOGIE_OUTPUT ] \
	[ -p PRELUDE ]\
	[--nct ] \
	[--act ]
```

- `-cp CLASS_PATH` declares the classpath where ByteBack will look for
  bytecode to be analyzed. You can repeat this option to specify
  multiple classpaths.

- `-c TARGET_CLASS` give the fully qualified name of an entry class
  for ByteBack's analysis. You can repeat this option to declare
  multiple entry classes. ByteBack will recursively process all
  application classes that are referenced from any entry class
  (excluding standard library classes).

- `-o BOOGIE_OUTPUT` declares the name of the output Boogie file
  generated by ByteBack.  If this option is omitted, ByteBack prints
  the Boogie program to standard output.

- `-p PRELUDE` specifies the Boogie prelude files to be used. If this
  option is omitted, ByteBack uses its [standard
  prelude](./byteback-cli/src/main/resources/boogie/BytebackPrelude.bpl).

- `--nct` enables support for verifying behavior of *implicit*
  `NullPointer` exceptions.

- `--act` enables support for verifying behavior of *implicit*
  `IndexOutOfBounds` exceptions.

## ByteBack annotations

BBLib, the ByteBack annotations library, includes annotations and
static methods for specifying functional properties at the level of
Java source code, or any other language that compiles to JVM bytecode.

See the annotated programs in [TODO]() for examples of using BBLib.


# Citing ByteBack

To cite ByteBack refer to the paper:

> Marco Paganoni and Carlo A. Furia.
> **Verifying Functional Correctness Properties At the Level of Java Bytecode**.
> In *Proceedings of the 25th International Symposium on Formal Methods (FM)*.
> *Lecture Notes in Computer Science*, 14000:343–363, Springer, March 2023.

```bibtex
@InProceedings{PF-FM23-ByteBack,
  author = {Marco Paganoni and Carlo A. Furia},
  title = {Verifying Functional Correctness Properties At the Level of {J}ava Bytecode},
  booktitle = {Proceedings of the 25th International Symposium on Formal Methods (FM)},
  year = {2023},
  series = {Lecture Notes in Computer Science},
  pages = {343--363},
  volume = {14000},
  publisher = {Springer}
}
```

Another publication describing the research behind ByteBack:

> Marco Paganoni and Carlo A. Furia.
> **Reasoning About Exceptional Behavior At the Level of Java Bytecode**.
> In *Proceedings of the 18th International Conference on integrated Formal Methods (iFM)*.
> *Lecture Notes in Computer Science*, 14300:113–133, Springer, November 2023.

```bibtex
@InProceedings{PF-FM23-ByteBack,
  author = {Marco Paganoni and Carlo A. Furia},
  title = {Reasoning About Exceptional Behavior At the Level of {J}ava Bytecode},
  booktitle = {Proceedings of the 18th International Conference on integrated Formal Methods (iFM)},
  year = {2023},
  series = {Lecture Notes in Computer Science},
  pages = {113--133},
  volume = {14300},
  publisher = {Springer},
}
```
