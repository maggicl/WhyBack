#!/bin/bash

set -euo pipefail

prover="$1"
heap="$2"

. "$(dirname "$0")/clean.sh"

gradle :byteback-test:test-java-8:system "-Pprover=$prover" "-Pheap=$heap" || true
gradle :byteback-test:test-java-17:system "-Pprover=$prover" "-Pheap=$heap" || true
gradle :byteback-test:test-kotlin-1.8.0:system "-Pprover=$prover" "-Pheap=$heap" || true
gradle :byteback-test:test-scala-2.13.8:system "-Pprover=$prover" "-Pheap=$heap" || true

. "$(dirname "$0")/results.sh"

prover="$(echo "$prover" | tr ',' '_' | tr '.' '_' | sed 's/_$//')"

mv "results.csv" "results_${heap}_${prover}.csv"