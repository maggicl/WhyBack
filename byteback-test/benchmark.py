import os
import re
import logging as lg
import time as tm
import pandas as pd
import subprocess as sp
import click as cl
import python_loc_counter as locc
import sys

BYTEBACK_EXECUTABLE = os.path.join(os.getenv("BYTEBACK_ROOT"), "bin/byteback-core")
BOOGIE_EXECUTABLE = "boogie"


def timeit(f):
    start = round(tm.time() * 1000)
    f()
    end = round(tm.time() * 1000)

    return end - start


def run_byteback(class_path, class_name, output_path):
    return sp.run([BYTEBACK_EXECUTABLE, "-cp", class_path, "-c", class_name,
                   "-o", output_path], stdout=sp.PIPE, stderr=sp.PIPE)


def run_boogie(path):
    return sp.run([BOOGIE_EXECUTABLE, "/infer:j", path], stdout=sp.PIPE)


def run_javap(class_path, class_name, output_path):
    with open(output_path, "w") as f:
        return sp.run(["javap", "-cp", class_path, "-c", class_name], stdout=f)


def verification_benchmark(path):
    r = re.compile("Boogie program verifier finished with [0-9]+ verified, 0 errors")
    def f():
        process = run_boogie(path)
        if not r.search(process.stdout.decode("utf-8")):
            raise RuntimeError("Boogie program could not be verified")
        if process.returncode != 0:
            raise RuntimeError("Boogie execution failed")

    return timeit(f)


def conversion_benchmark(class_path, class_name, output_path):
    r = re.compile("Conversion completed in ([0-9])+ms, total time ([0-9])+ms")
    d = re.compile("[0-9]+")

    process = run_byteback(class_path, class_name, output_path)
    output = r.search(process.stderr.decode("utf-8"));
    if process.returncode != 0:
        raise RuntimeError("ByteBack execution failed")
    if not output:
        raise RuntimeError("Could not match byteback's output")

    numbers = re.findall(d, output.group(0));

    return int(numbers[0]), int(numbers[1])


def benchmark(source_path, class_name, jar_path, temp_path, n=5):
    conversion_time = 0
    conversion_overhead = 0
    total_conversion_time = 0
    total_verification_time = 0
    bytecode_path = os.path.join(temp_path, class_name + ".j")
    boogie_path = os.path.join(temp_path, class_name + ".bpl")
    run_javap(jar_path, class_name, bytecode_path)

    for _ in range(0, n):
        b, t = conversion_benchmark(jar_path, class_name, boogie_path)
        total_conversion_time += t
        conversion_overhead +=  b / t
        total_verification_time += verification_benchmark(boogie_path)

    source_size = locc.LOCCounter(source_path).getLOC()
    bytecode_size = locc.LOCCounter(bytecode_path).getLOC()
    boogie_size = locc.LOCCounter(boogie_path).getLOC()

    return {
        "Experiment": class_name,
        "ConversionTime": total_conversion_time / n,
        "ConversionOverhead": conversion_overhead / n,
        "VerificationTime": total_verification_time / n,
        "SourceSize": source_size['source_loc'],
        "BytecodeSize": bytecode_size['source_loc'],
        "BoogieSize": boogie_size['source_loc']
    }


def test_components(root, source_path, name):
    path = root + os.sep + name;
    relative_path = os.path.relpath(path, source_path)
    path_components = os.path.splitext(relative_path)[0].split(os.sep)
    class_name = ".".join(path_components)

    return path, class_name


def walk_tests(source_path, extension):
    for root, dirs, files in os.walk(source_path):
        for name in files:
            if name.endswith(extension):
                yield test_components(root, source_path, name)


@cl.command()
@cl.option("--jar", required=True, help="Path to .jar containing the tests")
@cl.option("--source", required=True, help="File containing a list of the classes to be tested")
@cl.option("--output", required=True, help="Path to the output .csv file")
@cl.option("--temp", required=True, help="Temporary directory for boogie files")
@cl.option("--extension", required=True, help="Extension of the test files")
def main(jar, source, output, temp, extension):
    jar_path = jar
    source_path = source
    output_path = output
    temp_path = temp
    data = []
    for path, class_name in walk_tests(source_path, extension):
        try:
            data.append(benchmark(path, class_name, jar_path, temp_path, n=1))
        except (OSError, RuntimeError):
            lg.warning(f"Skipping {class_name} due to error")
            continue

    df = pd.DataFrame(data)
    df.to_csv(output_path)


main()
