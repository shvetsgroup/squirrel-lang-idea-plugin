#!/bin/bash

rm -rf gen
/usr/bin/java -jar tools/grammar-kit.jar gen src/com/sqide/Squirrel.bnf
../idea/tools/lexer/jflex-1.4/bin/jflex --skel ../idea/tools/lexer/idea-flex.skeleton --charat --nobak src/com/sqide/lexer/SquirrelLexer.flex -d gen/com/sqide/lexer/
../idea/tools/lexer/jflex-1.4/bin/jflex --skel ../idea/tools/lexer/idea-flex.skeleton --charat --nobak src/com/sqide/lexer/SquirrelDocLexer.flex -d gen/com/sqide/lexer/
