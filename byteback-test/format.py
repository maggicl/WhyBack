import pandas as pd
import click as cl
import sys

COL_EXPERIMENT = "Experiment"
COL_CONVERSION_TIME = "ConversionTime"
COL_VERIFICATION_TIME = "VerificationTime"
COL_SOURCE_SIZE = "SourceSize"
COL_BYTECODE_SIZE = "BytecodeSize"
COL_BOOGIE_SIZE = "BoogieSize"

LATEX_MACRO = "\pgfkeyssetvalue"


@cl.command()
@cl.option("--csv", required=True, help="Path to .csv containing the benchmark results")
@cl.option("--output", required=False, help="Output LaTeX")
def main(csv, output):
    df = pd.read_csv(csv)
    f = open(output, "w") if output else sys.stdout

    for index, row in df.iterrows():
        identifier = row["Experiment"].replace(".", "/")

        def print_macro(key, value):
            print(f"{LATEX_MACRO}{{/bb/{identifier}/{key}}}{{{value}}}", file=f)

        def print_column_macro(column):
            print_macro(column, row[column])

        name = row["Experiment"].split(".")[-1]
        print_column_macro(COL_CONVERSION_TIME)
        print_column_macro(COL_VERIFICATION_TIME)
        print_column_macro(COL_SOURCE_SIZE)
        print_column_macro(COL_BYTECODE_SIZE)
        print_column_macro(COL_BOOGIE_SIZE)


main()
