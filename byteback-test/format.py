import pandas as pd
import click as cl

@cl.command()
@cl.argument("csv", required=True, help="Path to .csv containing the benchmark results")
def main():
    df = pd.read_csv()
    pass

main()
