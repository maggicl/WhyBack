#!/bin/env bash

echo "% DATE: $(date)"
python format-keys.py ./test-java8/benchmark/java/results.csv j8 ./test-java17/benchmark/java/results.csv j17 ./test-scala/benchmark/scala/results.csv s --prefix byteback.test.
