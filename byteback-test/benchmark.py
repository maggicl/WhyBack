import os
import time as tm
import pandas as pd
import subprocess as sp
import python_loc_counter as locc

OUTPUT_DIR = "./output"
BOOGIE_EXECUTABLE = "boogie"
BYTEBACK_EXECUTABLE = os.path.join(os.getenv("BYTEBACK_ROOT"), "bin/byteback-core")
JAR = os.getenv("JAR")


def timeit(f):
    start = tm.time()
    f()
    end = tm.time()

    return end - start


def run_byteback(class_path, class_name, output_path):
    print(JAR)
    print(BYTEBACK_EXECUTABLE)
    return sp.run([BYTEBACK_EXECUTABLE, "-cp", class_path, "-c", class_name,
                   "-o", output_path])


def run_boogie(path):
    return sp.run([BOOGIE_EXECUTABLE, path])


def benchmark(path, class_name, n=30):
    total_conversion_time = 0
    total_verification_time = 0
    output_path = os.path.join(OUTPUT_DIR, class_name + ".bpl")

    for _ in range(0, n):
        total_conversion_time += timeit(lambda: run_byteback(JAR, class_name, output_path))
        total_verification_time += timeit(lambda: run_boogie(output_path))

    input_size = locc.LOCCounter(path).getLOC()
    output_size = locc.LOCCounter(output_path).getLOC()

    return {
        "Experiment": class_name,
        "ConversionTime": total_conversion_time / n,
        "VerificationTime": total_verification_time / n,
        "InputSize": input_size,
        "OutputSize": output_size
    }


def test_components(root, name):
    path = root + os.sep + name
    path_components = (path).split(os.sep)
    src_index = path_components.index("src")
    assert src_index > 0
    class_name = ".".join(path_components[src_index + 3:]).removesuffix(".java")

    return path, class_name


def walk_tests():
    for root, dirs, files in os.walk("."):
        for name in files:
            if name.endswith(".java"):
                yield test_components(root, name)


def main():
    data = []
    for path, class_name in walk_tests():
        data.append(benchmark(path, class_name))

    df = pd.DataFrame(data)
    df.to_csv(os.path.join(OUTPUT_DIR, "results.csv"))


main()
