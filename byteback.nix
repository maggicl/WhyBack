{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  # lit: LLVM INTEGRATION TESTING framework
  buildInputs = [
    pkgs.openjdk17
    (pkgs.python3.withPackages(
      packages: with packages; [pandas lit filecheck click]
    ))
    pkgs.boogie
    #pkgs.z3
    #pkgs.python312Packages.pip
    pkgs.alt-ergo
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
