/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
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
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockDyeSapling extends ItemBlock {

	public ItemBlockDyeSapling(int par1) {
		super(par1);
		hasSubtypes = true;
	}

	@Override
	public void getSubItems(int id, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 16; i++)
			par3List.add(new ItemStack(id, 1, i));
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
		return ReikaDyeHelper.getColorFromDamage(is.getItemDamage()).getJavaColor().brighter().getRGB();
	}

	@Override
	public String getItemDisplayName(ItemStack is)
	{
		return ReikaDyeHelper.getColorFromDamage(is.getItemDamage()).colorName+" Sapling";
	}

}
