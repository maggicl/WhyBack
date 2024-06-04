#!/bin/bash

set -euo pipefail

cd "$(dirname "$0")/.."

find . -path '*/test-*' -name '*.out' -exec cat \{\} \; | gawk -f scripts/results.gawk > results.csv