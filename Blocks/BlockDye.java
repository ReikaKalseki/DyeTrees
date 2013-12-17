package Reika.DyeTrees.Blocks;

import java.awt.Color;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaParticleHelper;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Registry.DyeOptions;

public class BlockDye extends Block {

	public BlockDye(int ID) {
		super(ID, Material.rock);
		this.setCreativeTab(DyeTrees.dyeTreeTab);
		this.setStepSound(Block.soundStoneFootstep);
		this.setHardness(1F);
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
		return Item.dyePowder.itemID;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 9;
	}

	@Override
	public int damageDropped(int dmg)
	{
		return dmg;
	}

	@Override
	public Icon getIcon(int par1, int par2)
	{
		return blockIcon;
	}

	@Override
	public void registerIcons(IconRegister ico) {
		blockIcon = ico.registerIcon("dyetrees:dye");
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		if (!DyeOptions.BLOCKPARTICLES.getState())
			return;
		if (rand.nextInt(4) > 0)
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

}
