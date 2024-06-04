#!/bin/bash

set -euo pipefail

cd "$(dirname "$0")/.."

echo "file,goal,heap_count,missing_inv_count" > stats.csv

find . -path '*/test-*' -name '*.whyback' -exec gawk -v FS="," -v OFS="," -v fn=\{\} '
  @include "./scripts/descriptor.gawk"
  BEGIN {
    gsub(/^\.\//, "", fn);
    gsub(/\.whyback$/, ".mlcfg", fn);
  }
  /STATS: / {
    gsub(/^STATS: /, "", $1);
    print fn,descriptor_to_jvm($1),$2,$3
  }' \{\} \; >> stats.csv