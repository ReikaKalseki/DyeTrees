/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.World;

import java.awt.Color;
import java.util.Random;

import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import Reika.DragonAPI.Libraries.IO.ReikaColorAPI;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BiomeRainbowForest extends BiomeGenBase {

	Random rand = new Random();

	public BiomeRainbowForest(int id) {
		super(id);
		this.setColor(16711935);
		this.setBiomeName("Rainbow Forest");
		this.func_76733_a(5159473); //not used in 1.5.2
		this.setTemperatureRainfall(0.7F, 0.8F);
		spawnableWaterCreatureList.clear();
	}

	@Override
	public BiomeDecorator createBiomeDecorator()
	{
		return new RainbowForestDecorator(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getModdedBiomeGrassColor(int original)
	{
		Color c = ReikaDyeHelper.dyes[rand.nextInt(16)].getJavaColor().brighter().brighter().brighter();
		double bias = 0.0;
		double d = 1D-bias;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int r2 = ReikaColorAPI.getRedFromInteger(original);
		int g2 = ReikaColorAPI.getGreenFromInteger(original);
		int b2 = ReikaColorAPI.getBlueFromInteger(original);
		return new Color((int)(r2*d+bias*r), (int)(g2*d+bias*g), (int)(b2*d+bias*b)).getRGB();
	}

}
