#!/bin/sh

dir="$(dirname "$0")"
prelude="$dir/../byteback-whyml/src/main/resources"

# FOR SOME REASON, $HOME is not set. Use tilde here to get home.
# Even why3 is not able to get its value and fails if given
conf="$(realpath ~)/.why3.conf"

exec why3 --config "$conf" prove -P Z3,4.8.17, -L "$prelude" "$@"
