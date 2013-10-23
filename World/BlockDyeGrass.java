package Reika.DyeTrees.World;

import java.awt.Color;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.DyeTrees;

public class BlockDyeGrass extends BlockGrass {

	public BlockDyeGrass(int par1) {
		super(par1);
		this.setCreativeTab(DyeTrees.dyeTreeTab);
		this.setStepSound(Block.grass.stepSound);
		this.setHardness(Block.grass.blockHardness);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random par5Random)
	{
		if (!world.isRemote)
		{
			if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2)
			{
				world.setBlock(x, y, z, Block.dirt.blockID);
			}
			else if (world.getBlockLightValue(x, y + 1, z) >= 9)
			{
				for (int l = 0; l < 4; ++l)
				{
					int i1 = x + par5Random.nextInt(3) - 1;
					int j1 = y + par5Random.nextInt(5) - 3;
					int k1 = z + par5Random.nextInt(3) - 1;
					int l1 = world.getBlockId(i1, j1 + 1, k1);

					if (world.getBlockId(i1, j1, k1) == Block.dirt.blockID && world.getBlockLightValue(i1, j1 + 1, k1) >= 4 && world.getBlockLightOpacity(i1, j1 + 1, k1) <= 2)
					{
						world.setBlock(i1, j1, k1, blockID);
					}
				}
			}
		}
	}

	@Override
	public int getRenderColor(int par1)
	{
		return ReikaDyeHelper.getColorFromDamage(par1).getJavaColor().getRGB();
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		//int meta = world.getBlockMetadata(x, y, z);

		int rx = x < 0 ? -1 : 1;
		int rz = z < 0 ? -1 : 1;

		x = Math.abs(x);
		z = Math.abs(z);
		Color color = RainbowForestGenerator.getColor(x, y, z).getJavaColor().brighter();
		Color px = RainbowForestGenerator.getColor(x+16*rx, y, z).getJavaColor().brighter();
		Color mx = RainbowForestGenerator.getColor(x-16*rx, y, z).getJavaColor().brighter();
		Color pz = RainbowForestGenerator.getColor(x, y, z+16*rz).getJavaColor().brighter();
		Color mz = RainbowForestGenerator.getColor(x, y, z-16*rz).getJavaColor().brighter();
		double fx1 = x%16 < 7 ? (7-x%16)/8D : 0;
		double fz1 = z%16 < 7 ? (7-z%16)/8D : 0;

		double fx2 = x%16 > 8 ? (x%16-8)/8D : 0;
		double fz2 = z%16 > 8 ? (z%16-8)/8D : 0;

		//ReikaJavaLibrary.pConsole((int)(color.getRed()*(1-fx1)+px.getRed()*fx1)+":"+(int)(color.getGreen()*(1-fx1)+px.getGreen()*fx1)+":"+(int)(color.getBlue()*(1-fx1)+px.getBlue()*fx1));

		Color c1 = new Color((int)(color.getRed()*(1-fx1)+px.getRed()*fx1), (int)(color.getGreen()*(1-fx1)+px.getGreen()*fx1), (int)(color.getBlue()*(1-fx1)+px.getBlue()*fx1));
		Color c2 = new Color((int)(color.getRed()*(1-fx2)+mx.getRed()*fx2), (int)(color.getGreen()*(1-fx2)+mx.getGreen()*fx2), (int)(color.getBlue()*(1-fx2)+mx.getBlue()*fx2));
		Color c3 = new Color((int)(color.getRed()*(1-fz1)+pz.getRed()*fz1), (int)(color.getGreen()*(1-fz1)+pz.getGreen()*fz1), (int)(color.getBlue()*(1-fz1)+pz.getBlue()*fz1));
		Color c4 = new Color((int)(color.getRed()*(1-fz2)+mz.getRed()*fz2), (int)(color.getGreen()*(1-fz2)+mz.getGreen()*fz2), (int)(color.getBlue()*(1-fz2)+mz.getBlue()*fz2));

		int r; int g; int b;
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();

		fz2 *= 0.55;
		fz1 *= 0.55;
		fx2 *= 0.55;
		fx1 *= 0.55;

		if (fz1 > 0 && fx1 <= 0 && fx2 <= 0) {
			r = (int) (color.getRed()*(1-fz1)+pz.getRed()*fz1);
			g = (int) (color.getGreen()*(1-fz1)+pz.getGreen()*fz1);
			b = (int) (color.getBlue()*(1-fz1)+pz.getBlue()*fz1);
		}

		if (fz2 > 0 && fx1 <= 0 && fx2 <= 0) {
			r = (int) (color.getRed()*(1-fz2)+mz.getRed()*fz2);
			g = (int) (color.getGreen()*(1-fz2)+mz.getGreen()*fz2);
			b = (int) (color.getBlue()*(1-fz2)+mz.getBlue()*fz2);
		}

		if (fx1 > 0 && fz1 <= 0 && fz2 <= 0) {
			r = (int) (color.getRed()*(1-fx1)+pz.getRed()*fx1);
			g = (int) (color.getGreen()*(1-fx1)+pz.getGreen()*fx1);
			b = (int) (color.getBlue()*(1-fx1)+pz.getBlue()*fx1);
		}

		if (fx2 > 0 && fz1 <= 0 && fz2 <= 0) {
			r = (int) (color.getRed()*(1-fx2)+mz.getRed()*fx2);
			g = (int) (color.getGreen()*(1-fx2)+mz.getGreen()*fx2);
			b = (int) (color.getBlue()*(1-fx2)+mz.getBlue()*fx2);
		}


		return new Color(r, g, b).getRGB();
	}

	@Override
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return Block.grass.getBlockTexture(par1IBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public Icon getIcon(int par1, int par2)
	{
		return Block.grass.getIcon(par1, par2);
	}

}
