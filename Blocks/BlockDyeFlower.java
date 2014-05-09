/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.DyeTrees;

public class BlockDyeFlower extends BlockFlower {

	public BlockDyeFlower(int par1) {
		super(par1);
		this.setCreativeTab(DyeTrees.dyeTreeTab);
		this.setStepSound(Block.soundGrassFootstep);
	}

	@Override
	public EnumPlantType getPlantType(World world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public void registerIcons(IconRegister ico) {
		blockIcon = ico.registerIcon("dyetrees:flower");
	}

	@Override
	public int damageDropped(int par1)
	{
		return par1;
	}

	@Override
	public int getRenderColor(int dmg)
	{
		return ReikaDyeHelper.dyes[dmg].getColor();
	}

	@Override
	public int colorMultiplier(IBlockAccess iba, int x, int y, int z)
	{
		int dmg = iba.getBlockMetadata(x, y, z);
		return ReikaDyeHelper.dyes[dmg].getJavaColor().brighter().getRGB();
	}

}
