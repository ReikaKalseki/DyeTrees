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
import net.minecraft.world.biome.BiomeGenBase;
import Reika.DragonAPI.Interfaces.RetroactiveGenerator;
import Reika.DyeTrees.Blocks.BlockDyeSapling;

public class RetroDyeTreeGen implements RetroactiveGenerator {

	@Override
	public void generate(Random r, World world, int chunkX, int chunkZ) {
		BiomeGenBase biome = world.getBiomeGenForCoords(chunkX, chunkZ);
		int trees = ColorTreeGenerator.getTreeCount(biome);
		int x = chunkX+r.nextInt(16);
		int z = chunkZ+r.nextInt(16);
		if (ColorTreeGenerator.canGenerateTree(world, x, z)) {
			for (int i = 0; i < trees; i++) {
				if (r.nextInt(ColorTreeGenerator.CHANCE) == 0) {
					int y = world.getTopSolidOrLiquidBlock(x, z);
					int id = world.getBlockId(x, y, z);
					Block b = Block.blocksList[id];
					if (BlockDyeSapling.canGrowAt(world, x, y, z))
						ColorTreeGenerator.growTree(world, x, y, z, 5+r.nextInt(3), r);
				}
			}
		}
	}

	@Override
	public boolean canGenerateAt(Random rand, World world, int chunkX, int chunkZ) {
		return false;
	}

	@Override
	public String getIDString() {
		return "DyeTreesGen";
	}

}
