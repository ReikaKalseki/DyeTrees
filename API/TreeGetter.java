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

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TreeGetter {

	private static Class dyeTreeBlocks;
	private static Block[] dyeBlocks;

	public static boolean isDyeLeaf(ItemStack block) {
		return block.itemID == dyeBlocks[0].blockID;
	}

	public static boolean isDyeSapling(ItemStack block) {
		return block.itemID == dyeBlocks[1].blockID;
	}

	public static ItemStack getDyeSapling(int dyeMeta) {
		return new ItemStack(dyeBlocks[1].blockID, 1, dyeMeta);
	}

	public static ItemStack getDyeLeaf(int dyeMeta) {
		return new ItemStack(dyeBlocks[0].blockID, 1, dyeMeta);
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
