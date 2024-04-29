#!/bin/bash

set -euo pipefail

dir="$(dirname "$0")"
prelude="$dir/../byteback-whyml/src/main/resources"

# FOR SOME REASON, $HOME is not set. Use tilde here to get home.
# Even why3 is not able to get its value and fails if config file path
# is not given explicitly.
conf="$(realpath ~)/.why3.conf"

file="$1"
extension=".mlcfg"

shift 1

#exec why3 --config "$conf" prove -P Z3,4.8.17, -L "$prelude" "$file$extension" "$@"

# Check syntax only
exec why3 --config "$conf" prove -L "$prelude" \
  --warn-off=unused_expression --warn-off=unused_variable --warn-off=useless_at \
  "$file$extension" "$@" >/dev/null
