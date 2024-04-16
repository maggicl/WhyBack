#!/bin/sh

dir="$(dirname "$0")"
prelude="$dir/../byteback-whyml/src/main/resources"

# using the -P short argument name for the parameter borks everything
exec why3 prove --prover="Z3,4.8.17," -L "$prelude" "$@"
