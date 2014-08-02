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

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaPlantHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Registry.DyeBlocks;
import Reika.DyeTrees.World.RainbowTreeGenerator;

public class BlockRainbowSapling extends BlockSapling {

	private Icon icon;

	private static Random r = new Random();

	public BlockRainbowSapling(int par1) {
		super(par1);
		this.setCreativeTab(DyeTrees.dyeTreeTab);
		this.setStepSound(Block.soundGrassFootstep);
	}

	@Override
	public void growTree(World world, int x, int y, int z, Random r) {
		this.growTree(world, x, y, z);
	}

	public void growTree(World world, int x, int y, int z)
	{
		if (world.isRemote)
			return;

		int id = world.getBlockId(x+1, y, z);
		if (id == blockID)
			x++;
		id = world.getBlockId(x, y, z+1);
		if (id == blockID)
			z++;

		RainbowTreeGenerator.getInstance().generateRainbowTree(world, x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer ep, int p6, float a, float b, float c) {
		ItemStack is = ep.getCurrentEquippedItem();
		if (is == null)
			return false;
		if (!ReikaItemHelper.matchStacks(is, ReikaDyeHelper.WHITE.getStackOf()))
			return false;
		int color = world.getBlockMetadata(x, y, z);
		if (this.canGrowAt(world, x, y, z))
			this.growTree(world, x, y, z);
		else
			world.spawnParticle("happyVillager", x+r.nextDouble(), y+r.nextDouble(), z+r.nextDouble(), 0, 0, 0);
		if (!ep.capabilities.isCreativeMode)
			is.stackSize--;
		return true;
	}

	@Override
	public Icon getIcon(int par1, int x)
	{
		return icon;
	}

	@Override
	public void registerIcons(IconRegister ico)
	{
		icon = ico.registerIcon("DyeTrees:rainbowsapling");
	}

	@Override
	public final int colorMultiplier(IBlockAccess iba, int x, int y, int z)
	{
		return 0xffffff;
	}

	public boolean canGrowAt(World world, int x, int y, int z) {
		if (y < 4)
			return false;

		int id = world.getBlockId(x, y, z);
		if (id != DyeBlocks.RAINBOWSAPLING.getBlockID())
			return false;

		boolean flag = false;
		for (int dx = 0; dx >= -1; dx--) {
			for (int dz = 0; dz >= -1; dz--)  {
				if (id == world.getBlockId(x + dx, y, z + dz) && id == world.getBlockId(x + dx + 1, y, z + dz) && id == world.getBlockId(x + dx, y, z + dz + 1) && id == world.getBlockId(x + dx + 1, y, z + dz + 1)) {
					flag = true;
					break;
				}
			}

			if (flag)
				break;
		}
		if (!flag)
			return false;

		if (ReikaWorldHelper.countAdjacentBlocks(world, x, y, z, blockID, true) != 3)
			return false;

		Block b = Block.blocksList[id];
		if (!ReikaPlantHelper.SAPLING.canPlantAt(world, x, y, z))
			return false;
		if (b instanceof BlockFluid)
			return false;

		id = world.getBlockId(x+1, y, z);
		if (id == DyeBlocks.RAINBOWSAPLING.getBlockID())
			x++;
		id = world.getBlockId(x, y, z+1);
		if (id == DyeBlocks.RAINBOWSAPLING.getBlockID())
			z++;
		return world.getBlockLightValue(x, y, z) >= 9 && RainbowTreeGenerator.getInstance().checkRainbowTreeSpace(world, x, y, z);
	}

	@Override
	public void markOrGrowMarked(World world, int x, int y, int z, Random par5Random)
	{
		if (r.nextInt(20) > 0)
			return;
		this.growTree(world, x, y, z, par5Random);
	}

	@Override
	public int damageDropped(int par1)
	{
		return par1;
	}

}
