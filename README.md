iRRT
====

A simulator for Rapidly-Exploring Random Tree (RRT) algorithm from Correll Lab.

The original project can be found at: http://correll.cs.colorado.edu/?p=1623

### Build

If you have CMake installed:

```sh
mkdir build && cd build
cmake ..
make
```

This will generate the `iRRT.jar` file. Then:

```sh
java -Djava.ext.dirs=../lib -jar iRRT.jar
```

where `../lib` targets the [directory](https://github.com/bchretien/iRRT/tree/master/lib) that contains the dependencies.
