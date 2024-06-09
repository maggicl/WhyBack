#!/bin/sh

set -eou pipefail

types="prelude.numeric.Operators"

cd "$(dirname "$0")"
/opt/homebrew/opt/m4/bin/m4 -DTYPES="$types" -I . ./heap.mlw.m4 > ../heap.mlw
