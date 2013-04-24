package net.machinemuse.utils

import java.util.Random

object MuseMathUtils {
  val random = new Random;

  def nextDouble: Double = random.nextDouble

  def clampDouble(value: Double, min: Double, max: Double): Double = {
    if (value < min) min
    if (value > max) max
    value
  }
}