import pandas as pd
import click as cl
import datetime as dt
import sys

COL_EXPERIMENT = "Experiment"
COL_CONVERSION_TIME = "ConversionTime"
COL_CONVERSION_OVERHEAD = "ConversionOverhead"
COL_VERIFICATION_TIME = "VerificationTime"
COL_SOURCE_SIZE = "SourceSize"
COL_BYTECODE_SIZE = "BytecodeSize"
COL_BOOGIE_SIZE = "BoogieSize"

LATEX_MACRO = "\pgfkeyssetvalue"

def pairs(l):
    return zip(l[::2], l[1::2])

@cl.command()
@cl.option("--output", required=False, help="Output LaTeX")
@cl.option("--prefix", required=False, help="Excludes name prefix from pgf key")
@cl.argument("csvs", nargs=-1)
def main(csvs, output, prefix):
    df = pd.DataFrame()
    f = open(output, "w") if output else sys.stdout

    for csv, group in pairs(csvs):
        nf = pd.read_csv(csv)
        nf["Group"] = group
        df = df.append(nf)

    def print_macro(key, value):
        print(f"{LATEX_MACRO}{{{key}}}{{{value}}}")

    for index, row in df.iterrows():
        prefix = prefix if prefix != None else ""
        group = row["Group"]
        identifier = row["Experiment"].removeprefix(prefix).replace(".", "/")

        def print_field(field):
            print_macro(f"/bb/{group}/{identifier}/{field}", row[field])

        name = row["Experiment"].split(".")[-1]
        print_field(COL_CONVERSION_TIME)
        print_field(COL_CONVERSION_OVERHEAD)
        print_field(COL_VERIFICATION_TIME)
        print_field(COL_SOURCE_SIZE)
        print_field(COL_BYTECODE_SIZE)
        print_field(COL_BOOGIE_SIZE)

    def print_mean(column):
        print_macro(f"/bb/average/{column}", df[column].mean())

    def print_total(column):
        print_macro(f"/bb/total/{column}", df[column].sum())

    print_mean(COL_CONVERSION_TIME)
    print_mean(COL_CONVERSION_OVERHEAD)
    print_mean(COL_VERIFICATION_TIME)
    print_mean(COL_SOURCE_SIZE)
    print_mean(COL_BOOGIE_SIZE)

    print_total(COL_CONVERSION_TIME)
    print_total(COL_CONVERSION_OVERHEAD)
    print_total(COL_VERIFICATION_TIME)
    print_total(COL_SOURCE_SIZE)
    print_total(COL_BOOGIE_SIZE)

main()
