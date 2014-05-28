/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/pressure/master/MMPL-1.0.txt
 */

package net.bdew.pressure.config

import net.bdew.lib.config.ItemManager
import net.bdew.pressure.ItemDebugger

object Items extends ItemManager(Config.IDs) {
  regItem(new ItemDebugger(ids.getItemId("Debugger")))
  regSimpleItem("Interface")
}