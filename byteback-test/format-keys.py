import pandas as pd
import click as cl
import datetime as dt
import sys

COL_EXPERIMENT = "Experiment"
COL_CONVERSION_TIME = "AverageConversionTime"
COL_CONVERSION_OVERHEAD = "AverageConversionOverhead"
COL_VERIFICATION_TIME = "AverageVerificationTime"
COL_SOURCE_SIZE = "SourceLinesOfCode"
COL_BYTECODE_SIZE = "BytecodeLinesOfCode"
COL_BOOGIE_SIZE = "BoogieLinesOfCode"

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

    df.to_csv("experiments.csv", index=False)

    def print_macro(key, value):
        print(f"{LATEX_MACRO}{{{key}}}{{{value}}}")

    for index, row in df.iterrows():
        prefix = prefix if prefix != None else ""
        group = row["Group"]
        identifier = row["Test"].removeprefix(prefix)

        def print_field(field):
            print_macro(f"/bb/{group}/{identifier}/{field}", row[field])

        print_field(COL_CONVERSION_TIME)
        print_field(COL_CONVERSION_OVERHEAD)
        print_field(COL_VERIFICATION_TIME)
        print_field(COL_SOURCE_SIZE)
        print_field(COL_BYTECODE_SIZE)
        print_field(COL_BOOGIE_SIZE)
        print_field("MethodCount")
        print_field("SpecRequireCount")
        print_field("SpecEnsureCount")
        print_field("SpecRaiseCount")
        print_field("SpecReturnCount")
        print_field("SpecPredicateCount")
        print_field("SpecPureCount")
        print_field("SpecAssertionCount")
        print_field("SpecAssumptionCount")
        print_field("SpecInvariantCount")

    def print_mean(column):
        print_macro(f"/bb/average/{column}", df[column].mean())

    def print_total(column):
        print_macro(f"/bb/total/{column}", df[column].sum())

    print_mean(COL_CONVERSION_TIME)
    print_mean(COL_CONVERSION_OVERHEAD)
    print_mean(COL_VERIFICATION_TIME)
    print_mean(COL_SOURCE_SIZE)
    print_mean(COL_BYTECODE_SIZE)
    print_mean(COL_BOOGIE_SIZE)

    print_total(COL_CONVERSION_TIME)
    print_total(COL_CONVERSION_OVERHEAD)
    print_total(COL_VERIFICATION_TIME)
    print_total(COL_SOURCE_SIZE)
    print_total(COL_BYTECODE_SIZE)
    print_total(COL_BOOGIE_SIZE)
    print_total("MethodCount")
    print_total("SpecRequireCount")
    print_total("SpecEnsureCount")
    print_total("SpecRaiseCount")
    print_total("SpecReturnCount")
    print_total("SpecPredicateCount")
    print_total("SpecPureCount")
    print_total("SpecAssertionCount")
    print_total("SpecAssumptionCount")
    print_total("SpecInvariantCount")


main()
