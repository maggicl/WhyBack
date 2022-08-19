import os
import re
import logging as lg
import time as tm
import pandas as pd
import subprocess as sp
import click as cl
import python_loc_counter as locc

BYTEBACK_EXECUTABLE = os.path.join(os.getenv("BYTEBACK_ROOT"), "bin/byteback-core")
BOOGIE_EXECUTABLE = "boogie"

def timeit(f):
    start = tm.time()
    f()
    end = tm.time()

    return end - start


def run_byteback(class_path, class_name, output_path):
    return sp.run([BYTEBACK_EXECUTABLE, "-cp", class_path, "-c", class_name,
                   "-o", output_path], stdout=sp.PIPE)


def run_boogie(path):
    return sp.run([BOOGIE_EXECUTABLE, path], stdout=sp.PIPE)


def verification_benchmark(path):
    r = re.compile("Boogie program verifier finished with [0-9]+ verified, 0 errors")
    def f():
        process = run_boogie(path)
        if not r.search(process.stdout.decode("utf-8")):
            raise OSError("Boogie program could not be verified")
        if process.returncode != 0:
            raise OSError("Boogie execution failed")

    return timeit(f)


def conversion_benchmark(class_path, class_name, output_path):
    def f():
        process = run_byteback(class_path, class_name, output_path)
        if process.returncode != 0:
            raise OSError("ByteBack execution failed")

    return timeit(f)


def benchmark(path, class_name, jar_path, temp_path, n=5):
    total_conversion_time = 0
    total_verification_time = 0
    output_path = os.path.join(temp_path, class_name + ".bpl")

    for _ in range(0, n):
        total_conversion_time += conversion_benchmark(jar_path, class_name, output_path)
        total_verification_time += verification_benchmark(output_path)

    input_size = locc.LOCCounter(path).getLOC()
    output_size = locc.LOCCounter(output_path).getLOC()

    return {
        "Experiment": class_name,
        "ConversionTime": total_conversion_time / n,
        "VerificationTime": total_verification_time / n,
        "InputSize": input_size['source_loc'],
        "OutputSize": output_size['source_loc']
    }


def test_components(root, source_path, name):
    path = root + os.sep + name;
    relative_path = os.path.relpath(path, source_path)
    path_components = relative_path.split(os.sep)
    class_name = ".".join(path_components).removesuffix(".java")

    return path, class_name


def walk_tests(source_path):
    for root, dirs, files in os.walk(source_path):
        for name in files:
            if name.endswith(".java"):
                yield test_components(root, source_path, name)


@cl.command()
@cl.option("--jar", required=True, help="Path to .jar containing the tests")
@cl.option("--source", required=True, help="File containing a list of the classes to be tested")
@cl.option("--output", required=True, help="Path to the output .csv file")
@cl.option("--temp", required=True, help="Temporary directory for boogie files")
def main(jar, source, output, temp):
    jar_path = jar
    source_path = source
    output_path = output
    temp_path = temp
    data = []
    for path, class_name in walk_tests(source_path):
        try:
            data.append(benchmark(path, class_name, jar_path, temp_path, n=30))
        except OSError:
            lg.warning(f"Skipping {class_name} due to error")
            continue

    df = pd.DataFrame(data)
    df.to_csv(output_path)


main()
