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

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.Blocks.BlockDyeSapling;

public class RainbowForestGenerator extends WorldGenerator {

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		if (ColorTreeGenerator.canGenerateTree(world, x, z) && BlockDyeSapling.canGrowAt(world, x, y, z)) {
			ColorTreeGenerator.growTree(world, x, y, z, 5+random.nextInt(3), random, this.getColor(x, y, z));
			return true;
		}
		return false;
	}

	private ReikaDyeHelper getColor(int x, int y, int z) {
		double[] color = new double[3];
		int sc = 32;
		int vsc = 64;
		int hexcolor;
		return ReikaDyeHelper.dyes[(Math.abs(x/16)+y+Math.abs(z/16))%16];
	}

}
