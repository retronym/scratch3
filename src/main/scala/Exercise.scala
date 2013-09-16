import scala.concurrent._
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.Try

object Exercise1 {
  object Task {
    def apply[T](f: Future[T]): Task[T] = new Task[T] { def future = f }
  }

  trait Task[T] { self =>
    def future: Future[T]
    def flatMap[S](f: T => Task[S])
                        (implicit ex: ExecutionContext): Task[S] =
      Task(future flatMap (t => f(t).future))

    def map[S](f: T => S)
              (implicit ex: ExecutionContext): Task[S] =
      flatMap(t => Task(Future.successful(f(t))))

    def coFlatMap[S](f: Task[T] => S)
                    (implicit ex: ExecutionContext): Task[S] = {
      val p = Promise[S]()
      // called on success or failure
      future.onComplete {
        (_ : Try[T]) =>
          val task: Task[T] = Task(future)
          val s: Try[S] = Try(f(task))
          p.complete(s)
      }
      Task(p.future)
    }
  }

  final class TaskCompletionSource[T] {
    private val promise: Promise[T] = Promise[T]()
    def setResult(t: T): Unit = promise.success(t)
    def setException(t: Throwable): Unit = promise.failure(t)
    val task: Task[T] = Task(promise.future)
  }
}

object Exercise2 {
  val actorSystem = akka.actor.ActorSystem("exercise")

  def awaitAll[T](ts: List[Future[T]])
                 (implicit ex: ExecutionContext): Future[List[T]] =
    Future.sequence(ts)

  def awaitAll0[T](ts: List[Future[T]])
                  (implicit ex: ExecutionContext): Future[List[T]] = {
    ts.foldLeft(Future.successful(List[T]()))(
      ((flt: Future[List[T]], ft: Future[T]) =>
        for { lt <- flt; t <- ft} yield t :: lt )
    ).map(_.reverse)
  }

  def awaitFirst[T](ts: List[Future[T]])
                   (implicit ex: ExecutionContext): Future[T] =
    Future.firstCompletedOf(ts)

  def awaitFirst0[T](ts: List[Future[T]])
                    (implicit ex: ExecutionContext): Future[T] = {
    val promise = Promise[T]()
    ts.foreach(ft => ft.onComplete((result: Try[T]) => promise.tryComplete(result)))
    promise.future
  }

  def delay[T](t: Future[T], duration: FiniteDuration)
              (implicit ex: ExecutionContext): Future[T] = {
    val promise = Promise[T]()
    t.onComplete { (t: Try[T]) =>
      actorSystem.scheduler.scheduleOnce(duration) {
        promise.complete(t)
      }
    }
    promise.future
  }
}

