/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import Reika.DragonAPI.Libraries.Registry.ReikaPlantHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DragonAPI.ModInteract.ReikaTwilightHelper;
import Reika.DyeTrees.Registry.DyeBlocks;
import cpw.mods.fml.common.IWorldGenerator;

public class ColorTreeGenerator implements IWorldGenerator {

	@Override
	public void generate(Random r, int chunkX, int chunkZ, World world, IChunkProvider cg, IChunkProvider cp) {
		chunkX *= 16;
		chunkZ *= 16;
		BiomeGenBase biome = world.getBiomeGenForCoords(chunkX, chunkZ);
		BiomeDecorator dec = biome.theBiomeDecorator;
		int trees = dec.treesPerChunk;
		if (biome == BiomeGenBase.plains)
			trees += 1;
		if (biome == BiomeGenBase.extremeHills)
			trees += 3;
		int x = chunkX+r.nextInt(16);
		int z = chunkZ+r.nextInt(16);
		if (this.canGenerateTree(world, x, z)) {
			for (int i = 0; i < trees; i++) {
				if (r.nextInt(50) == 0) {
					int y = world.getTopSolidOrLiquidBlock(x, z);
					int id = world.getBlockId(x, y, z);
					Block b = Block.blocksList[id];
					if (ReikaPlantHelper.SAPLING.canPlantAt(world, x, y, z) && ReikaWorldHelper.softBlocks(world, x, y, z) && !(b instanceof BlockFluid))
						this.growTree(world, x, y, z, 5+r.nextInt(3), r);
				}
			}
		}
	}

	public boolean canGenerateTree(World world, int x, int z) {
		int id = world.provider.dimensionId;
		if (id != 0 && id != ReikaTwilightHelper.TWILIGHT_ID)
			return false;
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		BiomeDecorator dec = biome.theBiomeDecorator;
		if (biome == BiomeGenBase.ocean || biome == BiomeGenBase.frozenOcean)
			return false;
		if (biome == BiomeGenBase.desert || biome == BiomeGenBase.desertHills)
			return false;
		if (biome == BiomeGenBase.mushroomIsland || biome == BiomeGenBase.mushroomIslandShore)
			return false;
		return true;
	}

	public void growTree(World world, int x, int y, int z, int h, Random r)
	{
		if (world.isRemote)
			return;
		int meta = r.nextInt(16);
		int log = r.nextInt(4);
		int w = 2;
		for (int i = 0; i < h; i++) {
			world.setBlock(x, y+i, z, Block.wood.blockID, log, 3);
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				if (ReikaWorldHelper.softBlocks(world, x+i, y+h-3, z+j) || world.getBlockId(x+i, y+h-3, z+j) == Block.leaves.blockID)
					world.setBlock(x+i, y+h-3, z+j, DyeBlocks.LEAF.getBlockID(), meta, 3);
			}
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				if (ReikaWorldHelper.softBlocks(world, x+i, y+h-2, z+j) || world.getBlockId(x+i, y+h-2, z+j) == Block.leaves.blockID)
					world.setBlock(x+i, y+h-2, z+j, DyeBlocks.LEAF.getBlockID(), meta, 3);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (ReikaWorldHelper.softBlocks(world, x+i, y+h-1, z+j) || world.getBlockId(x+i, y+h-1, z+j) == Block.leaves.blockID)
					world.setBlock(x+i, y+h-1, z+j, DyeBlocks.LEAF.getBlockID(), meta, 3);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i*j == 0 && ReikaWorldHelper.softBlocks(world, x+i, y+h, z+j) || world.getBlockId(x+i, y+h, z+j) == Block.leaves.blockID)
					world.setBlock(x+i, y+h, z+j, DyeBlocks.LEAF.getBlockID(), meta, 3);
			}
		}
	}

}
