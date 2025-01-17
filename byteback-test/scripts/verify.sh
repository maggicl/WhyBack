#!/bin/bash

set -euo pipefail

prelude="$(dirname "$0")/../../byteback-whyml/src/main/resources"

# FOR SOME REASON, $HOME is not set. Use tilde here to get home.
# Even why3 is not able to get its value and fails if config file path
# is not given explicitly.
conf="$(realpath ~)/.why3.conf"

prover="$1"
file="$2"

shift 2

extension=".mlcfg"

# Check syntax only
#exec why3 --config "$conf" prove -L "$prelude" \
#  --warn-off=unused_expression --warn-off=unused_variable --warn-off=useless_at \
#  "$file$extension" "$@" >/dev/null

# Why3 platform, version 1.7.1
# Z3,4.8.17,
# CVC4,1.8,
# CVC5,1.0.8,
# Gappa,1.4.0,
# Alt-Ergo,2.5.4,
# Z3,4.8.5,

# Run prover
exec why3 --config "$conf" prove -P "$prover" -L "$prelude" \
  --timelimit=10 \
  --warn-off=unused_expression --warn-off=unused_variable --warn-off=useless_at \
  "$file$extension" "$@" 2>&1 | tee "$file.out"
