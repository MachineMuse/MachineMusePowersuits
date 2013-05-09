package net.machinemuse.utils

import scala.collection.mutable
import net.machinemuse.general.MuseLogger

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:30 AM, 29/04/13
 */
class MuseBiMap[S, T] {

  val nameMap = new mutable.HashMap[S, T]
  val elemMap = new mutable.HashMap[T, S]

  def get(name: S): Option[T] = nameMap.get(name)

  def putName(name: S, elem: T) = {
    nameMap.get(name) match {
      case Some(e) => {
        MuseLogger.logError(name + " already a member!")
        e
      }
      case None => {
        nameMap.put(name, elem)
        elemMap.put(elem, name)
        elem
      }
    }
  }

  def putElem(elem: T, name: S) = {
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

  def getName(elem: T): Option[S] = elemMap.get(elem)

  def removeElem(elem: T) = {
    getName(elem) match {
      case Some(n) => {
        nameMap.remove(n)
        elemMap.remove(elem)
        Some(n)
      }
      case None => None
    }
  }

  def removeName(name: S) = {
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

class MuseRegistry[T] extends MuseBiMap[String, T] {
  def put(name: String, elem: T) = putName(name, elem)

  def put(elem: T, name: String) = putElem(elem, name)
}

class MuseNumericRegistry[T] extends MuseBiMap[Int, T] {
  def put(name: Int, elem: T) = putName(name, elem)

  def put(elem: T, name: Int) = putElem(elem, name)
}