#!/bin/sh

set -eou pipefail

cd "$(dirname "$0")"

for variant in machine math; do
  types="prelude.$variant.Operators"
  /opt/homebrew/opt/m4/bin/m4 -DTYPES="$types" -I . ./heap.mlw.m4 > "../heap_$variant.mlw"
done
