#!/bin/env bash

echo "% DATE: $(date)"
python format-keys.py --csv ./test-java8/benchmark/java/results.csv --root j8 --prefix-exclude byteback.test.
python format-keys.py --csv ./test-java17/benchmark/java/results.csv --root j17 --prefix-exclude byteback.test.
python format-keys.py --csv ./test-scala/benchmark/scala/results.csv --root s --prefix-exclude byteback.test.
