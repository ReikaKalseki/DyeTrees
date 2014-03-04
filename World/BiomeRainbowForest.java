/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.World;

import java.awt.Color;
import java.util.Random;

import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import Reika.DragonAPI.Libraries.IO.ReikaColorAPI;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BiomeRainbowForest extends BiomeGenBase {

	Random rand = new Random();

	private final int waterColor = new Color(0, 255, 255).getRGB();
	private final int skyColor = new Color(100, 140, 255).getRGB();

	public BiomeRainbowForest(int id) {
		super(id);
		this.setColor(16711935);
		this.setBiomeName("Rainbow Forest");
		this.func_76733_a(5159473); //not used in 1.6.4
		this.setTemperatureRainfall(0.7F, 0.8F);

		this.initSpawnRules();

		//topBlock = (byte)DyeBlocks.GRASS.getBlockID();
	}

	private void initSpawnRules() {
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableCaveCreatureList.clear();
		spawnableWaterCreatureList.clear();

		spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 1, 4, 4));

		spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 1, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));

		spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));

		//spawnableCaveCreatureList.add(new SpawnListEntry(EntityBat.class, 10, 8, 8));
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
		Color c = ReikaDyeHelper.dyes[rand.nextInt(16)].getJavaColor();
		double bias = 0.0005;
		double d = 1D-bias;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int r2 = ReikaColorAPI.getRedFromInteger(original);
		int g2 = ReikaColorAPI.getGreenFromInteger(original);
		int b2 = ReikaColorAPI.getBlueFromInteger(original);
		return new Color((int)(r2*d+bias*r), (int)(g2*d+bias*g), (int)(b2*d+bias*b)).getRGB();
	}

	@Override
	public int getBiomeGrassColor() //use dyeGrass block, with metadata
	{
		int original = BiomeGenBase.forest.getBiomeGrassColor();
		int x = 0;
		int y = 2;
		int z = 0;
		Color c = ReikaDyeHelper.dyes[(Math.abs(x/16)+y+Math.abs(z/16))%16].getJavaColor().brighter();
		double bias = 0.00095;
		double d = 1D-bias;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int r2 = ReikaColorAPI.getRedFromInteger(original);
		int g2 = ReikaColorAPI.getGreenFromInteger(original);
		int b2 = ReikaColorAPI.getBlueFromInteger(original);
		return new Color((int)(r2*d+bias*r), (int)(g2*d+bias*g), (int)(b2*d+bias*b)).getRGB();
	}

	@Override
	public float getSpawningChance()
	{
		return 0.6F;
	}

	@Override
	public int getSkyColorByTemp(float par1)
	{
		//return new Color(130, 170, 255).getRGB(); //original
		//return new Color(160, 130, 205).getRGB();
		//return new Color(0, 0, 0).getRGB();
		return skyColor; //original
	}

	@Override
	public int getWaterColorMultiplier()
	{
		return waterColor;
	}

}
