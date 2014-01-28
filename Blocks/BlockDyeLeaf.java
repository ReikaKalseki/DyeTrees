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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import Reika.DragonAPI.Base.BlockCustomLeaf;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaParticleHelper;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Registry.DyeBlocks;
import Reika.DyeTrees.Registry.DyeItems;
import Reika.DyeTrees.Registry.DyeOptions;

public class BlockDyeLeaf extends BlockCustomLeaf {

	public BlockDyeLeaf(int ID) {
		super(ID);
	}

	@Override
	public final int getRenderColor(int dmg)
	{
		return ReikaDyeHelper.dyes[dmg].getColor();
	}

	@Override
	public final int colorMultiplier(IBlockAccess iba, int x, int y, int z)
	{
		int dmg = iba.getBlockMetadata(x, y, z);
		return ReikaDyeHelper.dyes[dmg].getJavaColor().brighter().getRGB();
	}

	@Override
	public final int idDropped(int id, Random r, int fortune)
	{
		return DyeBlocks.SAPLING.getBlockID();
	}

	@Override
	public int damageDropped(int dmg)
	{
		return dmg;
	}

	@Override
	public final void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance, int fortune)
	{
		if (!world.isRemote) {

			float saplingChance = 0.05F;
			float appleChance = 0.005F;
			float dyeChance = 0.1F;
			float rainbowChance = 0.0001F;

			saplingChance *= (1+fortune);
			appleChance *= (1+fortune*5);
			dyeChance *= (1+fortune);
			rainbowChance *= (1+fortune)*(1+fortune);

			if (ReikaRandomHelper.doWithChance(saplingChance))
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(DyeBlocks.SAPLING.getBlockID(), 1, metadata));
			if (ReikaRandomHelper.doWithChance(appleChance))
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(Item.appleRed, 1, 0));
			if (ReikaRandomHelper.doWithChance(dyeChance))
				this.dropDye(world, x, y, z, metadata);
			if (ReikaRandomHelper.doWithChance(rainbowChance))
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(DyeBlocks.RAINBOWSAPLING.getBlockID(), 1, 0));
		}
	}

	private final void dropDye(World world, int x, int y, int z, int metadata) {
		if (ReikaRandomHelper.doWithChance(DyeOptions.DYEFRAC.getValue())) {
			this.dropBlockAsItem_do(world, x, y, z, new ItemStack(Item.dyePowder.itemID, 1, metadata));
		}
		else {
			this.dropBlockAsItem_do(world, x, y, z, DyeItems.DYE.getStackOfMetadata(metadata));
		}
	}

	@Override
	public final ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(DyeBlocks.LEAF.getBlockID(), 1, world.getBlockMetadata(x, y, z)));
		return ret;
	}

	@Override
	protected final ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(DyeBlocks.LEAF.getBlockID(), 1, par1);
	}

	@Override
	public final void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		if (rand.nextInt(12) > 0)
			return;
		double offset = 0.125;
		int meta = world.getBlockMetadata(x, y, z);
		ReikaDyeHelper dye = ReikaDyeHelper.getColorFromDamage(meta);
		Color color = dye.getJavaColor();
		double r = color.getRed()/255D;
		double g = color.getGreen()/255D;
		double b = dye.getBlue()/255D;
		ReikaParticleHelper.spawnColoredParticlesWithOutset(world, x, y, z, r, g, b, 1, offset);
	}

	@Override
	public final int idPicked(World par1World, int par2, int par3, int par4)
	{
		return DyeBlocks.LEAF.getBlockID();
	}

	@Override
	public boolean decays() {
		return blockID == DyeBlocks.DECAY.getBlockID();
	}

	@Override
	public boolean showInCreative() {
		return blockID != DyeBlocks.DECAY.getBlockID();
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return DyeTrees.dyeTreeTab;
	}

	@Override
	public String getFastGraphicsIcon(int meta) {
		return "DyeTrees:leaves_opaque";
	}

	@Override
	public String getFancyGraphicsIcon(int meta) {
		return "DyeTrees:leaves";
	}

	@Override
	public boolean shouldTryDecay(World world, int x, int y, int z, int meta) {
		return this.decays();
	}

	@Override
	public boolean shouldRandomTick() {
		return false;
	}

}
