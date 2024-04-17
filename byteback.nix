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
    # pkgs.alt-ergo # breaks nix on mac, codesign not found
    pkgs.why3
    pkgs.coq
  ];

  shellHook = ''
export PATH=$PATH:$PWD/byteback-cli/build/install/byteback-cli/bin
export CLASSPATH=$CLASSPATH:$PWD/byteback-annotations/build/libs/byteback-annotations.jar
why3 config detect
  '';

}
