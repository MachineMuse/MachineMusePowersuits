//package net.machinemuse.utils.render;
//
//import java.util.function.Function;
//
///**
// * Ported to Java by lehjr on 10/19/16.
// */
//public interface Render<A> {
//    Function<A> run() = A;
//
//
//
//    trait Render[A] {
//        def run(): A
//
//  import Render._
//
//        def compile(): Render[A] = Render.compile(this)
//
//        def map[B](f: A => B) = mk {
//            f(run())
//        }
//
//        def flatMap[B](f: A => Render[B]) = mk {
//            f(run()).run()
//        }
//}
