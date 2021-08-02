# Here is Linux bash project building script
echo [ Compiling library classes... ]
find ../src -name "*.java" > sources.txt
javac @sources.txt -encoding utf8 -d ../build
echo [ Library classes compiled successfully! ]
echo [ Compiling .jar... ]
jar cvf ../production/SimpleEncodings.jar -C ../build/ .
echo [ .jar compiled successfully! ]
