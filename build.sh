#!/bin/bash

rm -rf gen
/usr/bin/java -jar tools/grammar-kit.jar gen src/com/squirrelplugin/Squirrel.bnf
../idea/tools/lexer/jflex-1.4/bin/jflex --skel ../idea/tools/lexer/idea-flex.skeleton --charat --nobak src/com/squirrelplugin/lexer/SquirrelLexer.flex -d gen/com/squirrelplugin/lexer/
../idea/tools/lexer/jflex-1.4/bin/jflex --skel ../idea/tools/lexer/idea-flex.skeleton --charat --nobak src/com/squirrelplugin/lexer/SquirrelDocLexer.flex -d gen/com/squirrelplugin/lexer/
