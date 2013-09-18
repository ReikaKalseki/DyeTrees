/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.Blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Registry.DyeBlocks;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class BlockDyeLeaf extends BlockLeaves {

	private Icon[] icon = new Icon[2];

	public BlockDyeLeaf(int ID) {
		super(ID);
		this.setCreativeTab(DyeTrees.dyeTreeTab);
		this.setStepSound(Block.soundGrassFootstep);
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			this.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
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

	@Override
	public int idDropped(int id, Random r, int fortune)
	{
		return DyeBlocks.SAPLING.getBlockID();
	}

	@Override
	public int damageDropped(int dmg)
	{
		return dmg;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance, int fortune)
	{
		if (!world.isRemote)
		{
			int saplingChance = 20;
			int appleChance = 200;
			int dyeChance = 50;

			if (fortune > 0) {
				saplingChance -= 2 << fortune;
				appleChance -= 10 << fortune;
				dyeChance -= 15 << fortune;
			}
			if (saplingChance <= 0)
				saplingChance = 1;
			if (appleChance <= 0)
				appleChance = 1;
			if (dyeChance <= 0)
				dyeChance = 1;

			if (world.rand.nextInt(saplingChance) == 0) {
				int k1 = this.idDropped(metadata, world.rand, fortune);
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(k1, 1, this.damageDropped(metadata)));
			}
			if (world.rand.nextInt(appleChance) == 0)
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(Item.appleRed, 1, 0));
			if (world.rand.nextInt(dyeChance) == 0)
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(Item.dyePowder.itemID, 1, metadata));
		}
	}

	@Override
	public Icon getIcon(int par1, int par2)
	{
		return icon[this.getOpacityIndex()];
	}

	private int getOpacityIndex() {
		graphicsLevel = Minecraft.getMinecraft().gameSettings.fancyGraphics;
		return graphicsLevel ? 0 : 1;
	}

	@Override
	public void registerIcons(IconRegister ico)
	{
		icon[0] = ico.registerIcon("DyeTrees:leaves");
		icon[1] = ico.registerIcon("DyeTrees:leaves_opaque");
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random par5Random)
	{
		//no decay for technical reasons
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z)));
		return ret;
	}

	@Override
	public void beginLeavesDecay(World world, int x, int y, int z)
	{
		//no decay for technical reasons
	}

	@Override
	protected ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(blockID, 1, par1);
	}

}
