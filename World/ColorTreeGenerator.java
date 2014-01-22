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

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DragonAPI.ModInteract.ReikaTwilightHelper;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Blocks.BlockDyeSapling;
import Reika.DyeTrees.Registry.DyeOptions;
import cpw.mods.fml.common.IWorldGenerator;

public class ColorTreeGenerator implements IWorldGenerator {

	public static final int CHANCE = 24;

	@Override
	public void generate(Random r, int chunkX, int chunkZ, World world, IChunkProvider cg, IChunkProvider cp) {
		chunkX *= 16;
		chunkZ *= 16;
		BiomeGenBase biome = world.getBiomeGenForCoords(chunkX, chunkZ);
		if (biome != DyeTrees.forest && !DyeOptions.NORMAL.getState())
			return;
		int trees = this.getTreeCount(biome);
		int x = chunkX+r.nextInt(16);
		int z = chunkZ+r.nextInt(16);
		if (this.canGenerateTree(world, x, z)) {
			for (int i = 0; i < trees; i++) {
				if (r.nextInt(this.getTreeChance()) == 0) {
					int y = world.getTopSolidOrLiquidBlock(x, z);
					int id = world.getBlockId(x, y, z);
					Block b = Block.blocksList[id];
					if (DyeOptions.GENRAINBOW.getState() && r.nextInt(this.getRainbowChance(world)) == 0) {
						if (RainbowTreeGenerator.getInstance().checkRainbowTreeSpace(world, x, y, z)) {
							RainbowTreeGenerator.getInstance().generateRainbowTree(world, x, y, z);
						}
						else {
							TreeShaper.getInstance().generateRandomWeightedTree(world, x, y, z, ReikaDyeHelper.dyes[r.nextInt(16)]);
						}
					}
					else {
						TreeShaper.getInstance().generateRandomWeightedTree(world, x, y, z, ReikaDyeHelper.dyes[r.nextInt(16)]);
					}
				}
			}
		}
	}

	private int getRainbowChance(World world) {
		return world.provider.dimensionId == ReikaTwilightHelper.getDimensionID() ? 5 : 10;
	}

	private int getTreeChance() {
		return (int)(CHANCE/this.getScaledTreeCount());
	}

	private float getScaledTreeCount() {
		int scale = Math.max(1, Math.min(3, DyeOptions.DENSITY.getValue()));
		float factor = scale/2F;
		return factor;
	}

	public static int getTreeCount(BiomeGenBase biome) {
		BiomeDecorator dec = biome.theBiomeDecorator;
		int trees = dec.treesPerChunk;
		if (biome == BiomeGenBase.plains)
			trees += 2;
		if (biome == BiomeGenBase.extremeHills || biome == BiomeGenBase.extremeHillsEdge)
			trees += 3;
		if (biome == BiomeGenBase.iceMountains || biome == BiomeGenBase.icePlains)
			trees += 3;
		if (trees == 0) { //For BoP, BXL, and RealBiomes compatibility
			BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
			for (int i = 0; i < types.length; i++) {
				if (types[i] == BiomeDictionary.Type.FOREST) {
					trees = ReikaMathLibrary.extrema(trees, getTreeCount(BiomeGenBase.forest), "max");
				}
				if (types[i] == BiomeDictionary.Type.MOUNTAIN) {
					trees = ReikaMathLibrary.extrema(trees, getTreeCount(BiomeGenBase.extremeHills), "max");
				}
				if (types[i] == BiomeDictionary.Type.JUNGLE) {
					trees = ReikaMathLibrary.extrema(trees, getTreeCount(BiomeGenBase.jungle), "max");
				}
				if (types[i] == BiomeDictionary.Type.HILLS) {
					trees = ReikaMathLibrary.extrema(trees, getTreeCount(BiomeGenBase.forestHills), "max");
				}
				if (types[i] == BiomeDictionary.Type.FROZEN) {
					trees = ReikaMathLibrary.extrema(trees, getTreeCount(BiomeGenBase.icePlains), "max");
				}
				if (types[i] == BiomeDictionary.Type.PLAINS) {
					trees = ReikaMathLibrary.extrema(trees, getTreeCount(BiomeGenBase.plains), "max");
				}
				if (types[i] == BiomeDictionary.Type.SWAMP) {
					trees = ReikaMathLibrary.extrema(trees, getTreeCount(BiomeGenBase.swampland), "max");
				}
			}
		}
		return trees;
	}

	public static boolean canGenerateTree(World world, int x, int z) {
		if (world.isRemote)
			return false;
		if (Math.abs(world.provider.dimensionId) == 1)
			return false;
		if (Math.abs(world.provider.dimensionId) == ReikaTwilightHelper.getDimensionID())
			return false;
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		BiomeDecorator dec = biome.theBiomeDecorator;
		if (biome == BiomeGenBase.ocean || biome == BiomeGenBase.frozenOcean)
			return false;
		if (biome == BiomeGenBase.desert || biome == BiomeGenBase.desertHills)
			return false;
		if (biome == BiomeGenBase.mushroomIsland || biome == BiomeGenBase.mushroomIslandShore)
			return false;
		int y = world.getTopSolidOrLiquidBlock(x, z);
		//ReikaJavaLibrary.pConsole(world.getBlockId(x, y, z)+","+world.getBlockId(x, y-1, z)+","+world.getBlockId(x, y-2, z)+":"+BlockDyeSapling.canGrowAt(world, x, y, z));
		return BlockDyeSapling.canGrowAt(world, x, y, z);
	}

}
