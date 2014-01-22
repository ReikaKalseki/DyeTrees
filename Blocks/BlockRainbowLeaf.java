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

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import Reika.DragonAPI.Base.BlockCustomLeaf;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Registry.DyeBlocks;
import Reika.DyeTrees.Registry.DyeItems;
import Reika.DyeTrees.Registry.DyeOptions;

public class BlockRainbowLeaf extends BlockCustomLeaf {

	public BlockRainbowLeaf(int par1) {
		super(par1);
	}

	@Override
	public boolean decays() {
		return true;
	}

	@Override
	public boolean showInCreative() {
		return true;
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return DyeTrees.dyeTreeTab;
	}

	@Override
	public final int getRenderColor(int dmg)
	{
		//return Color.HSBtoRGB(((System.currentTimeMillis()/60)%360)/360F, 0.8F, 1);
		World world = Minecraft.getMinecraft().theWorld;
		EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
		int x = MathHelper.floor_double(ep.posX);
		int y = MathHelper.floor_double(ep.posY+ep.getEyeHeight());
		int z = MathHelper.floor_double(ep.posZ);
		return this.colorMultiplier(world, x, y, z);
	}

	@Override
	public final int colorMultiplier(IBlockAccess iba, int x, int y, int z)
	{
		int sc = 32;
		float hue = (float)(ReikaMathLibrary.py3d(x, y*3, z+x)%sc)/sc;
		return Color.HSBtoRGB(hue, 0.7F, 1F);
	}

	@Override
	public final int idDropped(int id, Random r, int fortune)
	{
		return DyeBlocks.RAINBOWSAPLING.getBlockID();
	}

	@Override
	public int damageDropped(int dmg)
	{
		return dmg%2 == 1 ? dmg : dmg+1;
	}

	@Override
	public final void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance, int fortune)
	{
		if (!world.isRemote) {
			float saplingChance = 0.0125F;
			float appleChance = 0.05F;

			saplingChance *= (1+fortune);
			appleChance *= (1+fortune*5);

			if (ReikaRandomHelper.doWithChance(saplingChance))
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(DyeBlocks.RAINBOWSAPLING.getBlockID(), 1, metadata));
			if (ReikaRandomHelper.doWithChance(appleChance))
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(Item.appleRed, 1, 0));
			this.dropDye(world, x, y, z, fortune);
		}
	}

	private final void dropDye(World world, int x, int y, int z, int fortune) {
		int drop = this.getDyeDropCount(fortune);
		for (int i = 0; i < drop; i++) {
			if (DyeOptions.VANILLADYES.getState()) {
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(Item.dyePowder.itemID, 1, rand.nextInt(16)));
			}
			else {
				this.dropBlockAsItem_do(world, x, y, z, new ItemStack(DyeItems.DYE.getShiftedItemID(), 1, rand.nextInt(16)));
			}
		}
	}

	private int getDyeDropCount(int fortune) {
		return 1+rand.nextInt(3*(1+fortune))+fortune+rand.nextInt(1+fortune*fortune);
	}

	@Override
	public final ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(DyeBlocks.RAINBOW.getBlockID(), 1, world.getBlockMetadata(x, y, z)));
		return ret;
	}

	@Override
	protected final ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(DyeBlocks.RAINBOW.getBlockID(), 1, par1);
	}

	@Override
	public final void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		int color = this.colorMultiplier(world, x, y, z);
		Color c = new Color(color);
		float r = c.getRed()/255F;
		float g = c.getGreen()/255F;
		float b = c.getBlue()/255F;
		world.spawnParticle("reddust", x+rand.nextDouble(), y, z+rand.nextDouble(), r, g, b);
	}

	@Override
	public final int idPicked(World par1World, int par2, int par3, int par4)
	{
		return DyeBlocks.RAINBOW.getBlockID();
	}

	@Override
	public String getFastGraphicsIcon() {
		return "DyeTrees:leaves_opaque";
	}

	@Override
	public String getFancyGraphicsIcon() {
		return "DyeTrees:leaves";
	}

	@Override
	public boolean shouldTryDecay(World world, int x, int y, int z, int meta) {
		return meta%2 == 0;
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		return 90;
	}

	@Override
	public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		return 180;
	}


}
