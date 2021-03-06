/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/pressure/master/MMPL-1.0.txt
 */

package net.bdew.pressure.blocks.pump

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.block.HasTE
import net.bdew.lib.rotate.{BaseRotateableBlock, IconType}
import net.bdew.pressure.blocks.{BlockFilterable, BlockNotifyUpdates}
import net.bdew.pressure.render.RotatedBlockRenderer
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection

object BlockPump extends Block(Material.iron) with BaseRotateableBlock
with HasTE[TilePump] with BlockNotifyUpdates with BlockFilterable[TilePump] {
  override val TEClass = classOf[TilePump]

  setBlockName("pressure.pump")
  setHardness(2)

  @SideOnly(Side.CLIENT)
  override def getRenderType = RotatedBlockRenderer.id

  override def getFacing(world: IBlockAccess, x: Int, y: Int, z: Int) =
    ForgeDirection.values()(world.getBlockMetadata(x, y, z) & 7)

  override def setFacing(world: World, x: Int, y: Int, z: Int, facing: ForgeDirection) =
    world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) & 8 | facing.ordinal(), 3)

  override def rotateBlock(world: World, x: Int, y: Int, z: Int, axis: ForgeDirection) = {
    val meta = world.getBlockMetadata(x, y, z)
    world.setBlockMetadataWithNotify(x, y, z, (meta & 8) | (((meta & 7) + 1) % 6), 3)
  }

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    val meta = world.getBlockMetadata(x, y, z)
    val powered = world.isBlockIndirectlyGettingPowered(x, y, z)
    if (powered && ((meta & 8) == 0))
      world.setBlockMetadataWithNotify(x, y, z, (meta & 7) | 8, 2)
    else if (!powered && ((meta & 8) == 8))
      world.setBlockMetadataWithNotify(x, y, z, meta & 7, 2)
  }

  @SideOnly(Side.CLIENT)
  override def getIcon(meta: Int, kind: IconType.Value) = kind match {
    case IconType.BACK => backIcon
    case IconType.FRONT => frontIcon
    case IconType.SIDE if (meta & 8) == 8 => sideIconOn
    case _ => sideIconOff
  }

  var frontIcon, sideIconOff, sideIconOn, backIcon: IIcon = null

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(ir: IIconRegister) = {
    frontIcon = ir.registerIcon("pressure:pump/front")
    backIcon = ir.registerIcon("pressure:pump/back")
    sideIconOn = ir.registerIcon("pressure:pump/side_on")
    sideIconOff = ir.registerIcon("pressure:pump/side_off")
  }
}
