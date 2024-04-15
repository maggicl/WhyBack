#!/bin/bash

package="$(grep -e '^package' $1 | sed 's/;//;s/package //')"
class="$(grep -e '^public class' $1 | sed 's/ {//;s/public class //')"

sed -i '' "s/%{class}/$package.$class/g" "$1"