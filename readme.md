# Quikstrument:  Java Source Instrumenter 

This actually rewrites all the source in the target directory so we can get method level metrics without running a heavier weight profiler.  Uses ANTLR to generate the parser.

## How it works
0. Checks out an orphan branch from git so the source rewrite doesn't blow away changes.
1. Runs a shell script to find all the Java Files
2. Parses the source and adds an opening try block on method definitions
3. On exit of a method definition it closes the try and adds a finally block with instrumentation code.

## Directions

* Run the below command

```
./instrument.sh <src directory>
```

* You will then have to run the jar created as usual.

* Using grep/ag/egrep look for lines in standard out containing %%!!%%

* Unix sort:  ```sort -r -n -k 3``` should be able to sort the calls by longest

## Known Issues

* Since each source file results in an invoking a java run time it's slow.
* Errors on failed parse get swallowed.  
