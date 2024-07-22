#!/bin/bash

set -euo pipefail

cd "$(dirname "$0")/.."
find . \( -name '*.out' -o -name '*.whyback' -o -name '*.tmp.mlcfg' \) -delete
