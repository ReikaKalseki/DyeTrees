/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.Items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockRainbowLeaf extends ItemBlock {

	public ItemBlockRainbowLeaf(int par1) {
		super(par1);
	}

	@Override
	public void getSubItems(int id, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(id, 1, 1));
	}

	@Override
	public int getMetadata(int dmg)
	{
		return dmg;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack is, int par2)
	{
		return 0xffffff;
	}

}
