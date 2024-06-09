#!/bin/sh

set -eou pipefail

cd "$(dirname "$0")"
/opt/homebrew/opt/m4/bin/m4 -I . ./heap.mlw.m4 > ../heap.mlw
