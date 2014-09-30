## Sample SBT project with generated test sources

```
sbt test:run
[info] Loading global plugins from /Users/jason/.sbt/0.13/plugins
[info] Loading project definition from /Users/jason/code/scratch3/project
[info] Set current project to laws (in build file:/Users/jason/code/scratch3/)
[info] Running Test
[error] (run-main-0) java.lang.AssertionError: assertion failed: 42
java.lang.AssertionError: assertion failed: 42
	at scala.Predef$.assert(Predef.scala:165)
	at Test$.delayedEndpoint$Test$1(Test.scala:1)
```