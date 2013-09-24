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

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.REED;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND_PASS2;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class RainbowForestDecorator extends BiomeDecorator {

	private RainbowForestGenerator gen = new RainbowForestGenerator();

	public RainbowForestDecorator(BiomeGenBase bi) {
		super(bi);
		grassPerChunk = 8;
		flowersPerChunk = 6;
		reedsPerChunk = 50;
		treesPerChunk = -999;
	}

	@Override
	protected void decorate()
	{
		this.generateOres();

		this.auxDeco();

		for (int i = 0; i < 9; i++) {
			int x = chunk_X + randomGenerator.nextInt(16)+8;
			int z = chunk_Z + randomGenerator.nextInt(16)+8;
			int y = currentWorld.getTopSolidOrLiquidBlock(x, z);
			gen.generate(currentWorld, randomGenerator, x, y, z);
		}
	}

	private void auxDeco() {
		int i;
		int j;
		int k;
		boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SAND);
		for (i = 0; doGen && i < sandPerChunk2; ++i)
		{
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			sandGen.generate(currentWorld, randomGenerator, j, currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, CLAY);
		for (i = 0; doGen && i < clayPerChunk; ++i)
		{
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			clayGen.generate(currentWorld, randomGenerator, j, currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SAND_PASS2);
		for (i = 0; doGen && i < sandPerChunk; ++i)
		{
			j = chunk_X + randomGenerator.nextInt(16) + 8;
			k = chunk_Z + randomGenerator.nextInt(16) + 8;
			sandGen.generate(currentWorld, randomGenerator, j, currentWorld.getTopSolidOrLiquidBlock(j, k), k);
		}

		int i1;
		int l;
		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, FLOWERS);
		for (j = 0; doGen && j < flowersPerChunk; ++j)
		{
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = randomGenerator.nextInt(128);
			i1 = chunk_Z + randomGenerator.nextInt(16) + 8;
			plantYellowGen.generate(currentWorld, randomGenerator, k, l, i1);

			if (randomGenerator.nextInt(4) == 0)
			{
				k = chunk_X + randomGenerator.nextInt(16) + 8;
				l = randomGenerator.nextInt(128);
				i1 = chunk_Z + randomGenerator.nextInt(16) + 8;
				plantRedGen.generate(currentWorld, randomGenerator, k, l, i1);
			}
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, GRASS);
		for (j = 0; doGen && j < grassPerChunk; ++j)
		{
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = randomGenerator.nextInt(128);
			i1 = chunk_Z + randomGenerator.nextInt(16) + 8;
			WorldGenerator worldgenerator1 = biome.getRandomWorldGenForGrass(randomGenerator);
			worldgenerator1.generate(currentWorld, randomGenerator, k, l, i1);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, REED);
		for (j = 0; doGen && j < reedsPerChunk; ++j)
		{
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = chunk_Z + randomGenerator.nextInt(16) + 8;
			i1 = randomGenerator.nextInt(128);
			reedGen.generate(currentWorld, randomGenerator, k, i1, l);
		}

		for (j = 0; doGen && j < 10; ++j)
		{
			k = chunk_X + randomGenerator.nextInt(16) + 8;
			l = randomGenerator.nextInt(128);
			i1 = chunk_Z + randomGenerator.nextInt(16) + 8;
			reedGen.generate(currentWorld, randomGenerator, k, l, i1);
		}

		doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, LAKE);
		if (doGen && generateLakes)
		{
			for (j = 0; j < 50; ++j)
			{
				k = chunk_X + randomGenerator.nextInt(16) + 8;
				l = randomGenerator.nextInt(randomGenerator.nextInt(120) + 8);
				i1 = chunk_Z + randomGenerator.nextInt(16) + 8;
				(new WorldGenLiquids(Block.waterMoving.blockID)).generate(currentWorld, randomGenerator, k, l, i1);
			}

			for (j = 0; j < 20; ++j)
			{
				k = chunk_X + randomGenerator.nextInt(16) + 8;
				l = randomGenerator.nextInt(randomGenerator.nextInt(randomGenerator.nextInt(112) + 8) + 8);
				i1 = chunk_Z + randomGenerator.nextInt(16) + 8;
				(new WorldGenLiquids(Block.lavaMoving.blockID)).generate(currentWorld, randomGenerator, k, l, i1);
			}
		}

		MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, chunk_X, chunk_Z));
	}

}
