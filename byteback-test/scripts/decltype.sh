#!/bin/bash

set -eou pipefail

grep "$2" "$1" | \
    grep "heap:" | \
    sed 's/^ *//' | \
    awk '{
    if ($1=="let") {
        fndecl = $2=="rec" ? $3 : $2
        if (fndecl=="cfg" || fndecl=="function" || fndecl=="predicate") { print fndecl }
        else { print "unknown" }
    }
    else { print "unknown" }
    }'
