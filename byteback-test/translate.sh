#!/bin/bash

set -euo pipefail

byteback="$1"
jar="$2"
source="$3"
file="$4"
shift 4

extension=".mlw"

# strip extension, replace '/' with '.'
class="$(realpath "$source" --relative-to="$0" | sed 's/\.[a-z]*$//;s/\//\./g;s/.*\.src\.main\.[a-z][a-z]*\.//')"

exec "$byteback" -cp "$jar" -c "$class" -o "$file$extension" "$@"