import os
import re
import logging as lg
import time as tm
import pandas as pd
import subprocess as sp
import click as cl
import sys

lg.basicConfig(level=lg.DEBUG)


def timeit(f):
    start = round(tm.time() * 1000)
    f()
    end = round(tm.time() * 1000)

    return end - start


def run_byteback(class_path, class_name, output_path):
    return sp.run([BYTEBACK_EXECUTABLE, "-cp", class_path, "-c", class_name,
                   "-o", output_path], stdout=sp.PIPE, stderr=sp.PIPE)


def run_boogie(path, infer):
    command = [BOOGIE_EXECUTABLE]
    if infer: command.append("/infer:j")
    command.append(path)
    
    return sp.run(command, stdout=sp.PIPE)


def run_javap(class_path, class_name, output_path):
    with open(output_path, "w") as f:
        return sp.run(["javap", "-cp", class_path, "-c", class_name], stdout=f)


def count_lines(file_path):
    return int(sp.check_output(f"cat {file_path} | grep -c '[^[:space:]]'", shell=True))


def verification_benchmark(path, infer):
    r = re.compile("Boogie program verifier finished with [0-9]+ verified, 0 errors")
    def f():
        process = run_boogie(path, infer)
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


def benchmark(source_path, class_name, jar_path, temp_path, infer_regex=".*", n=1):
    r = re.compile(infer_regex)
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
        total_verification_time += verification_benchmark(boogie_path, r.match(source_path))

    return {
        "Experiment": class_name,
        "ConversionTime": total_conversion_time / n,
        "ConversionOverhead": conversion_overhead / n,
        "VerificationTime": total_verification_time / n,
        "SourceSize": count_lines(source_path),
        "BytecodeSize": count_lines(bytecode_path),
        "BoogieSize": count_lines(boogie_path)
    }


@cl.command()
@cl.option("--output", required=True, help="Path to the output .csv file")
@cl.option("--repetitions", required=True, type=cl.INT, help="Repetitions for each test")
@cl.option("--commands", required=True, help="Path to the .csv containing the commands to run")
def main(output, repetitions, commands):
    output_path = output
    data = []

    for entry in pd.read_csv(commands).iterrows():
        try:
            print(entry)
        except RuntimeError as e:
            lg.warning(f"Skipping {class_name} due to error:")
            continue

    df = pd.DataFrame(data)
    df.to_csv(output_path)


main()
