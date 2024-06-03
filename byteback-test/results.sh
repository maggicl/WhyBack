#!/bin/bash

set -euo pipefail

cd "$(dirname "$0")"

cat "$1" | \
  gsed -z 's/Goal\n/Goal /g;s/mlcfg:/mlcfg/g' | \
  gawk -v IFS=" " -v OFS="," -f ./results.gawk