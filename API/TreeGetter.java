/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.API;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TreeGetter {

	private static Class dyeTreeBlocks;
	private static Block[] dyeBlocks;

	public static final int LEAF_INDEX = 0;
	public static final int SAPLING_INDEX = 1;
	public static final int NATURAL_LEAF_INDEX = 2;
	public static final int RAINBOW_LEAF_INDEX = 4;

	public static boolean isDyeLeaf(ItemStack block) {
		return block.itemID == dyeBlocks[LEAF_INDEX].blockID;
	}

	public static boolean isDyeSapling(ItemStack block) {
		return block.itemID == dyeBlocks[SAPLING_INDEX].blockID;
	}

	public static ItemStack getDyeSapling(int dyeMeta) {
		return new ItemStack(dyeBlocks[SAPLING_INDEX].blockID, 1, dyeMeta);
	}

	public static ItemStack getHeldDyeLeaf(int dyeMeta) {
		return new ItemStack(getHeldDyeLeafID(), 1, dyeMeta);
	}

	public static ItemStack getNaturalDyeLeaf(int dyeMeta) {
		return new ItemStack(getNaturalDyeLeafID(), 1, dyeMeta);
	}

	public static int getHeldDyeLeafID() {
		return dyeBlocks[LEAF_INDEX].blockID;
	}

	public static int getNaturalDyeLeafID() {
		return dyeBlocks[NATURAL_LEAF_INDEX].blockID;
	}

	public static int getRainbowLeafID() {
		return dyeBlocks[RAINBOW_LEAF_INDEX].blockID;
	}

	public static int getSaplingID() {
		return dyeBlocks[SAPLING_INDEX].blockID;
	}

	static {
		try {
			dyeTreeBlocks = Class.forName("Reika.DyeTrees.DyeTrees", false, TreeGetter.class.getClassLoader());
			dyeBlocks = (Block[])dyeTreeBlocks.getField("blocks").get(null);
		}
		catch (ClassNotFoundException e) {
			System.out.println("DyeTrees class not found!");
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			System.out.println("DyeTrees class not read correctly!");
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			System.out.println("DyeTrees class not read correctly!");
			e.printStackTrace();
		}
		catch (NoSuchFieldException e) {
			System.out.println("DyeTrees class not read correctly!");
			e.printStackTrace();
		}
		catch (SecurityException e) {
			System.out.println("DyeTrees class not read correctly!");
			e.printStackTrace();
		}
	}

}
