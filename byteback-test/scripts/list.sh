#!/bin/bash

set -eou pipefail

cd "$(dirname "$0")/.."

find . -path '*/test-*' \( -name '*.java' -or -name '*.kt' -or -name '*.scala' \) | \
  sed 's/^.\///'