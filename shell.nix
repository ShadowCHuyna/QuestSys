{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    jdk21
    maven
    gnumake
	nodejs
    nodePackages.npm
  ];

  shellHook = ''
    export JAVA_HOME=${pkgs.jdk21}
    export MAVEN_OPTS="-Xmx2g"
    echo "Java version:"
    java -version
    echo "Maven version:"
    mvn -version
  '';
}