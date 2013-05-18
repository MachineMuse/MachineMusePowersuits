package net.machinemuse.powersuits.network

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 3:53 PM, 18/05/13
 */
trait ReadMonad[A] {
  def run(): A

  import ReadMonad._

  def map[B](f: A => B) = mk {
    f(run())
  }

  def flatMap[B](f: A => ReadMonad[B]) = mk {
    f(run()).run()
  }

}

object ReadMonad {
  private def mk[A](act: => A): ReadMonad[A] = new ReadMonad[A] {
    def run() = act
  }

  def apply[A](act: => A): ReadMonad[A] = mk(act)

  def liftIO[A](act: => A): ReadMonad[A] = mk(act)

  def pure[A](a: => A): ReadMonad[A] = mk(a)

}