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

    df = df[(df["SpecRaiseCount"] > 0) | (df["SpecReturnCount"] > 0) | (df["UsesExceptionFeatures"])]

    def print_macro(key, value):
        print(f"{LATEX_MACRO}{{{key}}}{{{value}}}")

    # Experiments count
    print_macro("/bbe/count/", len(df.index))
    print_macro("/bbe/count/j8", len(df[df["Group"] == "j8"].index))
    print_macro("/bbe/count/j17", len(df[df["Group"] == "k18"].index))
    print_macro("/bbe/count/s2", len(df[df["Group"] == "s2"].index))
    print_macro("/bbe/count/k18", len(df[df["Group"] == "k18"].index))

    # Methods count
    print_macro("/bbe/count/method", df["MethodCount"].sum())
    print_macro("/bbe/count/method/j8", df.loc[df["Group"] == "j8"]["MethodCount"].sum())
    print_macro("/bbe/count/method/j17", df.loc[df["Group"] == "j17"]["MethodCount"].sum())
    print_macro("/bbe/count/method/s2", df.loc[df["Group"] == "s2"]["MethodCount"].sum())
    print_macro("/bbe/count/method/k18", df.loc[df["Group"] == "k18"]["MethodCount"].sum())

    # Annotation count
    print_macro("/bbe/count/raises", df["SpecRaiseCount"].sum())
    print_macro("/bbe/count/raises/j8", df.loc[df["Group"] == "j8"]["SpecRaiseCount"].sum())
    print_macro("/bbe/count/raises/j17", df.loc[df["Group"] == "j17"]["SpecRaiseCount"].sum())
    print_macro("/bbe/count/raises/s2", df.loc[df["Group"] == "s2"]["SpecRaiseCount"].sum())
    print_macro("/bbe/count/raises/k18", df.loc[df["Group"] == "k18"]["SpecRaiseCount"].sum())

    print_macro("/bbe/count/returns", df["SpecReturnCount"].sum())
    print_macro("/bbe/count/returns/j8", df.loc[df["Group"] == "j8"]["SpecReturnCount"].sum())
    print_macro("/bbe/count/returns/j17", df.loc[df["Group"] == "j17"]["SpecReturnCount"].sum())
    print_macro("/bbe/count/returns/s2", df.loc[df["Group"] == "s2"]["SpecReturnCount"].sum())
    print_macro("/bbe/count/returns/k18", df.loc[df["Group"] == "k18"]["SpecReturnCount"].sum())

    print_macro("/bbe/count/invariants", df["SpecInvariantCount"].sum())
    print_macro("/bbe/count/invariants/j8", df.loc[df["Group"] == "j8"]["SpecInvariantCount"].sum())
    print_macro("/bbe/count/invariants/j17", df.loc[df["Group"] == "j17"]["SpecInvariantCount"].sum())
    print_macro("/bbe/count/invariants/s2", df.loc[df["Group"] == "s2"]["SpecInvariantCount"].sum())
    print_macro("/bbe/count/invariants/k18", df.loc[df["Group"] == "k18"]["SpecInvariantCount"].sum())

    print_macro("/bbe/count/assertions", df["SpecAssertionCount"].sum())
    print_macro("/bbe/count/assertions/j8", df.loc[df["Group"] == "j8"]["SpecAssertionCount"].sum())
    print_macro("/bbe/count/assertions/j17", df.loc[df["Group"] == "j17"]["SpecAssertionCount"].sum())
    print_macro("/bbe/count/assertions/s2", df.loc[df["Group"] == "s2"]["SpecAssertionCount"].sum())
    print_macro("/bbe/count/assertions/k18", df.loc[df["Group"] == "k18"]["SpecAssertionCount"].sum())

    for index, row in df.iterrows():
        prefix = prefix if prefix != None else ""
        group = row["Group"]
        identifier = row["Test"].removeprefix(prefix)

        def print_field(field):
            print_macro(f"/bbe/{group}/{identifier}/{field}", row[field])

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
