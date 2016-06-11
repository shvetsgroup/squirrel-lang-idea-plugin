This plugin is in early development stage. It has a basic feature set implemented, which allow basic use with
Intellij IDEA or IDEA based IDEs, such as PHPStorm, PyCharm, etc.

Currently, I don't spend much time working on it, so if you want to help with any todo item, then go for it, send a PR
and I'll review it.

## Contribution guide

### Preparation

1. Download IntelliJ IDEA Community edition binaries from https://www.jetbrains.com/idea/
2. Download IntelliJ IDEA Community edition sources from https://github.com/JetBrains/intellij-community and place them
   into ../idea directory. Make sure you download sources tagged for the same build as your IDEA executable (check the
   build number in Help box of IDEA).

### Running plugin

1. Run build.sh to build the plugin. The should be a Bash run profile for this if you have bash plugin installed.
2. Launch "Plugin" run configuration to test it in IDEA.

# Features

0. Creation of Squirrel projects.
1. Syntax highlight.
2. Auto formatting.
3. Indentation.
4. Folding.
5. Running scripts using menu items.

# TODO:

0. debugger (code has some basic implementation, but much more work required to finish it)
1. automatically setup sdk?
2. download compiler from the web?
3. make compiler?
5. add debugger configuration (is it possible to download it?)
6. SquirrelSDKType Configurable
8. Test PATH detection on windows.
9. Custom icons for run config.
10. run configuration with C program.

# Known issues

1. Increment and decrement operators should always go in the same line with it's identifier.
