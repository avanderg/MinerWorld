

This directory contains the starting code for the class projects.  A
github assignment will be created for each homework assignment.  This
will mean that you'll end up with a handful of GIT repositories, each
containing a different copy of the Java code.

To run this from the command-line, you can do the following in _this_ directory:

    cd java
    javac -d  out -cp processing-core.jar:out *.java
    java -cp processing-core.jar:out VirtualWorld

The compiled .class files will be put in the diretory "out" under "java."

The directory "python" contains a python version of this game.  You can refer
to this code if you get confused by some of the Java idioms, but you don't need
to.  The python version relies on a module called "pygame" that isn't included
here, so you might not be able to run this version.

