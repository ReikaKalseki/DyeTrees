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

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.DyeTrees;

public class ItemTreeDye extends Item { //ore dict and usages!!

	public ItemTreeDye(int par1) {
		super(par1);
		this.setCreativeTab(DyeTrees.dyeTreeTab);
		hasSubtypes = true;
	}

	@Override
	public void registerIcons(IconRegister ico) {
		itemIcon = ico.registerIcon("dyetrees:dye");
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 16; i++) {
			ReikaDyeHelper dye = ReikaDyeHelper.dyes[i];
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public int getColorFromItemStack(ItemStack is, int par2)
	{
		return ReikaDyeHelper.getColorFromDamage(is.getItemDamage()).getJavaColor().brighter().getRGB();
	}

	@Override
	public Icon getIconFromDamage(int par1)
	{
		return itemIcon;
	}

	@Override
	public String getItemDisplayName(ItemStack is)
	{
		return ReikaDyeHelper.getColorFromDamage(is.getItemDamage()).colorName+" Tree Dye";
	}

}
