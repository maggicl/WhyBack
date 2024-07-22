#!/bin/bash

set -euo pipefail

heap="$1"

byteback="$2"
jar="$3"
source="$4"
file="$5"

shift 5

extension=".mlcfg"

# strip extension, replace '/' with '.'
class="$(realpath "$source" --relative-to="$(dirname "$0")/.." | \
  sed 's/\.[a-z]*$//;s/\//\./g;s/.*\.src\.main\.[a-z][a-z]*\.//')"

exec "$byteback" -cp "$jar" -c "$class" -h "$heap" -o "$file$extension" $@ 2>&1 | tee "$file.whyback"