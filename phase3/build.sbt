def genPhase3(cp: Classpath, out: File, main: Option[String], run: ScalaRun, s: TaskStreams): Seq[File] = {
  IO.delete(out)
  IO.createDirectory(out)
  val args = out.getAbsolutePath :: Nil
  val mainClass = main getOrElse "No main class defined for phase3 generator"
  toError(run.run(mainClass, cp.files, args, s.log))
  (out ** "*.scala").get
}

(sourceGenerators in Compile) <+= (
    fullClasspath in Compile in phase2,
    sourceManaged in Compile,
    mainClass in Compile in phase2,
    runner, streams
) map (genPhase3 _)
