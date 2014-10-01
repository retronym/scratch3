import sbt._
import Keys._

object build extends Build {
  val phase1 = project

  val phase2 = project dependsOn phase1

  val phase3 = project dependsOn phase2

  val instancesTemplate = settingKey[File]("instances template")
}
