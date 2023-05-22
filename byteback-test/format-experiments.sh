#!/bin/env bash

echo "% DATE: $(date)"
python format-keys.py ./test-java-8/benchmark/benchmark.csv j8 ./test-java-17/benchmark/benchmark.csv j17 ./test-scala-2/benchmark/benchmark.csv s2 --prefix /byteback/test/
