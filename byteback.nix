{ pkgs ? import <nixpkgs> {} }:

# vampire = import ./vampire.nix

let
  old_pkgs = import (builtins.fetchGit {
    name = "old_pkgs";
    url = "https://github.com/NixOS/nixpkgs/";
    rev = "4a7f99d55d299453a9c2397f90b33d1120669775";
  }) { inherit (pkgs) system; };
in
  pkgs.mkShell {
    # lit: LLVM INTEGRATION TESTING framework
    buildInputs = [
      pkgs.openjdk17
      (pkgs.python3.withPackages(
        packages: with packages; [pandas lit filecheck click]
      ))
      pkgs.boogie
      pkgs.gcc
      pkgs.z3
      pkgs.alt-ergo
      #old_pkgs.vampire
      pkgs.why3
      pkgs.cvc4
      pkgs.cvc5
      pkgs.gappa
      pkgs.coq
    ];

    shellHook = ''
  export PATH=$PATH:$PWD/byteback-cli/build/install/byteback-cli/bin
  export CLASSPATH=$CLASSPATH:$PWD/byteback-annotations/build/libs/byteback-annotations.jar
  # pip install z3-solver==4.8.5.0
  pip install z3-solver==4.8.9.0
  why3 config detect
    '';

  }
