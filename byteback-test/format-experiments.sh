#!/bin/env bash

echo "% DATE: $(date)"
python format-experiments.py ./test-java-8/build/benchmark/benchmark.csv j8 ./test-java-17/build/benchmark/benchmark.csv j17 ./test-scala-2/build/benchmark/benchmark.csv s2 ./test-kotlin-1.8.0/build/benchmark/benchmark.csv k18 --prefix /byteback/test/ --output-tex ./build/experiments/experiments.tex --output-csv ./build/experiments/experiments.csv --index ./bbe-order.csv
