/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.Registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import Reika.DragonAPI.Interfaces.RegistryEnum;
import Reika.DragonAPI.Libraries.Java.ReikaStringParser;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Blocks.BlockDye;
import Reika.DyeTrees.Blocks.BlockDyeFlower;
import Reika.DyeTrees.Blocks.BlockDyeLeaf;
import Reika.DyeTrees.Blocks.BlockDyeSapling;
import Reika.DyeTrees.Blocks.BlockRainbowLeaf;
import Reika.DyeTrees.Blocks.BlockRainbowSapling;
import Reika.DyeTrees.Items.ItemBlockDyeBlock;
import Reika.DyeTrees.Items.ItemBlockDyeFlower;
import Reika.DyeTrees.Items.ItemBlockDyeLeaf;
import Reika.DyeTrees.Items.ItemBlockDyeSapling;
import Reika.DyeTrees.Items.ItemBlockRainbowLeaf;
import Reika.DyeTrees.Items.ItemBlockRainbowSapling;

public enum DyeBlocks implements RegistryEnum {

	LEAF(BlockDyeLeaf.class, ItemBlockDyeLeaf.class, "Leaves"),
	SAPLING(BlockDyeSapling.class, ItemBlockDyeSapling.class, "Sapling"),
	DECAY(BlockDyeLeaf.class, ItemBlockDyeLeaf.class, "Decaying Leaves"),
	DYE(BlockDye.class, ItemBlockDyeBlock.class, "Dye Block"),
	RAINBOW(BlockRainbowLeaf.class, ItemBlockRainbowLeaf.class, "Rainbow Leaves"),
	RAINBOWSAPLING(BlockRainbowSapling.class, ItemBlockRainbowSapling.class, "Rainbow Sapling"),
	FLOWER(BlockDyeFlower.class, ItemBlockDyeFlower.class, "Rainbow Flower");
	//GRASS(BlockDyeGrass.class, null, "Dye Grass");

	private Class blockClass;
	private String blockName;
	private Class itemBlock;

	public static final DyeBlocks[] blockList = values();

	private DyeBlocks(Class <? extends Block> cl, Class<? extends ItemBlock> ib, String n) {
		blockClass = cl;
		blockName = n;
		itemBlock = ib;
	}

	public int getBlockID() {
		return DyeTrees.config.getBlockID(this.ordinal());
	}

	@Override
	public Class[] getConstructorParamTypes() {
		return new Class[]{int.class};
	}

	@Override
	public Object[] getConstructorParams() {
		return new Object[]{this.getBlockID()};
	}

	@Override
	public String getUnlocalizedName() {
		return ReikaStringParser.stripSpaces(blockName);
	}

	@Override
	public Class getObjectClass() {
		return blockClass;
	}

	@Override
	public String getBasicName() {
		return blockName;
	}

	@Override
	public String getMultiValuedName(int meta) {
		switch(this) {
		default:
			return ReikaDyeHelper.dyes[meta].colorName+" "+this.getBasicName();
		}
	}

	@Override
	public boolean hasMultiValuedName() {
		switch(this) {
		//case GRASS:
		//	return false;
		default:
			return true;
		}
	}

	@Override
	public int getNumberMetadatas() {
		switch(this) {
		//case GRASS:
		//	return 1;
		default:
			return 16;
		}
	}

	@Override
	public Class<? extends ItemBlock> getItemBlock() {
		return itemBlock;
	}

	@Override
	public boolean hasItemBlock() {
		return itemBlock != null;
	}

	@Override
	public String getConfigName() {
		return this.getBasicName();
	}

	@Override
	public int getDefaultID() {
		return 2200+this.ordinal();
	}

	@Override
	public boolean isBlock() {
		return true;
	}

	@Override
	public boolean isItem() {
		return false;
	}

	@Override
	public String getCategory() {
		return "Blocks";
	}

	public boolean isDummiedOut() {
		return blockClass == null;
	}

	public Block getBlockInstance() {
		return DyeTrees.blocks[this.ordinal()];
	}

	public int getID() {
		return this.getBlockID();
	}

	@Override
	public boolean overwritingItem() {
		return false;
	}
}
