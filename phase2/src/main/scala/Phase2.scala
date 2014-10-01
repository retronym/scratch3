package laws

import laws.IO._

import java.io.File

// Second phase of code generation
object Phase2 {
  def main(args: Array[String]) {
    val Seq(outPath) = args.toSeq
    val outDir = new File(outPath)
    val testName = s"Test${laws.Instances.foo}"
    write(new File(outDir, s"$testName.scala"), s"object $testName extends App")
  }
}
