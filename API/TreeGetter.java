/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.API;

import net.minecraft.item.ItemStack;
import Reika.DyeTrees.Registry.DyeBlocks;

public class TreeGetter {

	private static Class dyeTreeBlocks;
	private static DyeBlocks[] dyeBlocks;

	public static boolean isDyeLeaf(ItemStack block) {
		return block.itemID == dyeBlocks[0].LEAF.getBlockID();
	}

	public static boolean isDyeSapling(ItemStack block) {
		return block.itemID == dyeBlocks[0].SAPLING.getBlockID();
	}

	public static ItemStack getDyeSapling(int dyeMeta) {
		return new ItemStack(dyeBlocks[0].SAPLING.getBlockID(), 1, dyeMeta);
	}

	public static ItemStack getDyeLeaf(int dyeMeta) {
		return new ItemStack(dyeBlocks[0].LEAF.getBlockID(), 1, dyeMeta);
	}

	static {
		try {
			dyeTreeBlocks = Class.forName("Reika.DyeTrees.Registry.DyeBlocks", false, TreeGetter.class.getClassLoader());
			dyeBlocks = (DyeBlocks[])dyeTreeBlocks.getEnumConstants();
		}
		catch (ClassNotFoundException e) {
			System.out.println("DyeTrees class not found!");
			e.printStackTrace();
		}
	}

}
