/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.Registry.DyeBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DyeTreeTab extends CreativeTabs {

	public DyeTreeTab(int par1, String par2Str) {
		super(par1, par2Str);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		return new ItemStack(DyeBlocks.LEAF.getBlockID(), 1, ReikaDyeHelper.BLUE.ordinal());
	}

	@Override
	public String getTranslatedTabLabel() {
		return "Dye Trees";
	}

}
