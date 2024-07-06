#!/bin/bash

set -euo pipefail

byteback="$1"
jar="$2"
source="$3"
file="$4"
shift 4

heap="machine"

extension=".mlcfg"

# strip extension, replace '/' with '.'
class="$(realpath "$source" --relative-to="$(dirname "$0")/.." | \
  sed 's/\.[a-z]*$//;s/\//\./g;s/.*\.src\.main\.[a-z][a-z]*\.//')"

# TODO: handle --npe and --iobe options in "$@"

exec "$byteback" -cp "$jar" -c "$class" -h "$heap" -o "$file$extension" 2>&1 | tee "$file.whyback"