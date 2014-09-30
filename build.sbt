name := "laws"

version := "1.0.0"

// will be overriden by the Jenkins script
scalaVersion := "2.11.2"

(sourceGenerators in Test) <+= (sourceManaged in Test) map { dir =>
  // This can call out to code organized in ./project/*.scala
  // The generator will always be compiled and run with Scala 2.10.4,
  // the version used by SBT 0.13.6
  val file = dir / "demo" / "Test.scala"
  // If desired, you could make these JUnit test cases.
  IO.write(file, """object Test extends App { assert(Util.useful == 0, Util.useful) }""")
  Seq(file)
}
