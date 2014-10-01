def genPhase2Cached(cache: File, template: File, cp: Classpath, out: File, main: Option[String], run: ScalaRun, s: TaskStreams): Seq[File] = {
  val outputFile = out / "laws" / "Instances.scala"
  def gen() = genPhase2(cp, template, outputFile, main, run, s)
  val f = FileFunction.cached(cache / "gen-phase2", FilesInfo.hash) { _ => gen().toSet} // TODO: check if output directory changed
  f(Set(template)).toSeq
}

def genPhase2(cp: Classpath, template: File, out: File, main: Option[String], run: ScalaRun, s: TaskStreams): Seq[File] =
{
  println("here")
  IO.delete(out)
  IO.createDirectory(out.getParentFile)
  val args = out.getAbsolutePath :: template.getAbsolutePath :: Nil
  val mainClass = main getOrElse "No main class defined for phase2 generator"
  toError(run.run(mainClass, cp.files, args, s.log))
  (out ** "*.scala").get
}

(sourceGenerators in Compile) <+= (
    cacheDirectory,
    instancesTemplate in phase1,
    fullClasspath in Compile in phase1,
    sourceManaged in Compile,
    mainClass in Compile in phase1,
    runner, streams
) map genPhase2Cached