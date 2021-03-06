// Copyright (c) 2017 PSForever
package net.psforever.objects

import net.psforever.objects.definition.AmmoBoxDefinition
import net.psforever.objects.equipment.{Ammo, Equipment}

class AmmoBox(private val ammoDef : AmmoBoxDefinition,
              cap : Option[Int] = None
             ) extends Equipment {
  private var capacity = if(cap.isDefined) { AmmoBox.limitCapacity(cap.get, 1) } else { FullCapacity }

  def AmmoType : Ammo.Value = ammoDef.AmmoType

  def Capacity : Int = capacity

  def Capacity_=(toCapacity : Int) : Int = {
    capacity = AmmoBox.limitCapacity(toCapacity)
    Capacity
  }

  def FullCapacity : Int = ammoDef.Capacity

  def Definition : AmmoBoxDefinition = ammoDef

  override def toString : String = {
    AmmoBox.toString(this)
  }
}

object AmmoBox {
  def apply(ammoDef : AmmoBoxDefinition) : AmmoBox = {
    new AmmoBox(ammoDef)
  }

  def apply(ammoDef : AmmoBoxDefinition, capacity : Int) : AmmoBox = {
    new AmmoBox(ammoDef, Some(capacity))
  }

  /**
    * Accepting an `AmmoBox` object that has an uncertain amount of ammunition in it,
    * create multiple `AmmoBox` objects where none contain more than the maximum capacity for that ammunition type,
    * and the sum of all objects' capacities is the original object's capacity.
    * @param box an `AmmoBox` object of unspecified capacity
    * @return a `List` of `AmmoBox` objects with correct capacities
    */
  def Split(box : AmmoBox) : List[AmmoBox] = {
    val ammoDef = box.Definition
    var boxCap : Int = box.Capacity
    val maxCap : Int = ammoDef.Capacity
    val splitCap : Int = boxCap / maxCap
    val list : List[AmmoBox] = List.fill(splitCap)(new AmmoBox(ammoDef))
    val leftover = boxCap - maxCap * splitCap
    if(leftover > 0) {
      list :+ AmmoBox(ammoDef, leftover)
    }
    else {
      list
    }
  }

  def limitCapacity(count : Int, min : Int = 0) : Int = math.min(math.max(min, count), 65535)

  def toString(obj : AmmoBox) : String = {
    s"box of ${obj.AmmoType} ammo (${obj.Capacity})"
  }
}
