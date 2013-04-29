package net.machinemuse.utils

import scala.collection.mutable
import net.machinemuse.general.MuseLogger

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:30 AM, 29/04/13
 */
abstract class MuseRegistry[T] {

  val nameMap = new mutable.HashMap[String, T]
  val elemMap = new mutable.HashMap[T, String]

  def get(name: String): Option[T] = nameMap.get(name)

  def put(name: String, elem: T) = {
    nameMap.get(name) match {
      case Some(e) => {
        MuseLogger.logError(name + " already a member!")
      }
      case None => {
        nameMap.put(name, elem)
        elemMap.put(elem, name)
      }
    }
    elem
  }

  def put(elem: T, name: String) = {
    nameMap.get(name) match {
      case Some(e) => {
        MuseLogger.logError(name + " already a member!")
      }
      case None => {
        nameMap.put(name, elem)
        elemMap.put(elem, name)
      }
    }
    name
  }

  def apply = nameMap

  def inverse = elemMap

  def getName(elem: T): Option[String] = elemMap.get(elem)

  def remove(elem: T) = {
    getName(elem) match {
      case Some(n) => {
        nameMap.remove(n)
        elemMap.remove(elem)
        Some(n)
      }
      case None => None
    }
  }

  def remove(name: String) = {
    get(name) match {
      case Some(e) => {
        nameMap.remove(name)
        elemMap.remove(e)
        Some(e)
      }
      case None => None
    }
  }

}
