#!/bin/bash

rm -rf gen
/usr/bin/java -jar tools/grammar-kit.jar gen src/com/sqide/Squirrel.bnf
/usr/bin/java -jar ../idea/tools/lexer/jflex-1.7.0-SNAPSHOT.jar --skel ../idea/tools/lexer/idea-flex.skeleton --nobak src/com/sqide/lexer/SquirrelLexer.flex -d gen/com/sqide/lexer/
/usr/bin/java -jar ../idea/tools/lexer/jflex-1.7.0-SNAPSHOT.jar --skel ../idea/tools/lexer/idea-flex.skeleton --nobak src/com/sqide/lexer/SquirrelDocLexer.flex -d gen/com/sqide/lexer/
