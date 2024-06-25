{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  # lit: LLVM INTEGRATION TESTING framework
  buildInputs = [
    pkgs.openjdk17
    (pkgs.python3.withPackages(
      packages: with packages; [pandas lit filecheck click]
    ))
    pkgs.boogie
    pkgs.z3
    pkgs.z3_4_8_5
    #pkgs.alt-ergo
    pkgs.why3
    pkgs.cvc4
    pkgs.cvc5
    pkgs.gappa
    pkgs.coq
  ];

  shellHook = ''
export PATH=$PATH:$PWD/byteback-cli/build/install/byteback-cli/bin
export CLASSPATH=$CLASSPATH:$PWD/byteback-annotations/build/libs/byteback-annotations.jar
why3 config detect
  '';

}
