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

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import Reika.DragonAPI.Exception.InstallationException;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaTreeHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DragonAPI.ModRegistry.ModWoodList;
import Reika.DyeTrees.DyeTrees;
import Reika.DyeTrees.Registry.DyeBlocks;

public class TreeShaper {

	public static final int LUMPY_CHANCE = 10;
	public static final int TALL_CHANCE = 30;
	public static final int NORMAL_CHANCE = 60;

	private static final int leafID = DyeBlocks.DECAY.getBlockID();

	private static final TreeShaper instance = new TreeShaper();

	private final Random rand = new Random();

	private final ArrayList<ItemStack> validLogs = new ArrayList();

	public boolean isLogTypeEverAllowed(ModWoodList wood) {
		return wood != ModWoodList.BAMBOO;
	}

	private TreeShaper() {
		for (int i = 0; i < ReikaTreeHelper.treeList.length; i++) {
			ReikaTreeHelper tree = ReikaTreeHelper.treeList[i];
			if (DyeTrees.config.shouldGenerateLogType(tree)) {
				validLogs.add(tree.getLog());
				DyeTrees.logger.log("Dye Tree Generation with Log Type "+tree.getName()+" logs: Enabled");
			}
			else {
				DyeTrees.logger.log("Dye Tree Generation with Log Type "+tree.getName()+" logs: Disabled");
			}
		}

		for (int i = 0; i < ModWoodList.woodList.length; i++) {
			ModWoodList tree = ModWoodList.woodList[i];
			if (this.isLogTypeEverAllowed(tree)) {
				if (tree.exists()) {
					if (DyeTrees.config.shouldGenerateLogType(tree)) {
						validLogs.add(tree.getItem());
						DyeTrees.logger.log("Dye Tree Generation with Log Type "+tree.getBasicInfo()+": Enabled");
					}
					else {
						DyeTrees.logger.log("Dye Tree Generation with Log Type "+tree.getBasicInfo()+": Disabled (Config Option)");
					}
				}
				else {
					DyeTrees.logger.log("Dye Tree Generation with Log Type "+tree.getBasicInfo()+": Disabled (Not Loaded)");
				}
			}
			else {
				DyeTrees.logger.log("Dye Tree Generation with Log Type "+tree.getBasicInfo()+": Disabled (Disallowed)");
			}
		}

		if (validLogs.isEmpty()) {
			throw new InstallationException(DyeTrees.instance, "You must enable at least one log type!");
		}
	}

	public static TreeShaper getInstance() {
		return instance;
	}

	public void generateRandomWeightedTree(World world, int x, int y, int z, ReikaDyeHelper color) {
		int val = rand.nextInt(100);
		int chance = LUMPY_CHANCE;
		if (val < chance) {
			this.generateLumpyTree(world, x, y, z, color);
			return;
		}
		chance += TALL_CHANCE;
		if (val < chance) {
			this.generateTallTree(world, x, y, z, color);
			return;
		}
		this.generateNormalTree(world, x, y, z, color);
	}

	public ItemStack getLogType() {
		return validLogs.get(rand.nextInt(validLogs.size()));
	}

	public void generateNormalTree(World world, int x, int y, int z, ReikaDyeHelper color) {
		if (ColorTreeGenerator.canGenerateTree(world, x, z)) {
			int meta = color.ordinal();
			ItemStack log = this.getLogType();
			int w = 2;
			int h = 5+rand.nextInt(3);

			for (int i = 0; i < h; i++) {
				world.setBlock(x, y+i, z, log.itemID, log.getItemDamage(), 3);
			}
			for (int i = -w; i <= w; i++) {
				for (int j = -w; j <= w; j++) {
					if (this.canGenerateLeavesAt(world, x+i, y+h-3, z+j)) {
						world.setBlock(x+i, y+h-3, z+j, leafID, meta, 3);
					}
				}
			}
			for (int i = -w; i <= w; i++) {
				for (int j = -w; j <= w; j++) {
					if (this.canGenerateLeavesAt(world, x+i, y+h-2, z+j)) {
						world.setBlock(x+i, y+h-2, z+j, leafID, meta, 3);
					}
				}
			}
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (this.canGenerateLeavesAt(world, x+i, y+h-1, z+j)) {
						world.setBlock(x+i, y+h-1, z+j, leafID, meta, 3);
					}
				}
			}
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i*j == 0) {
						if (this.canGenerateLeavesAt(world, x+i, y+h, z+j)) {
							world.setBlock(x+i, y+h, z+j, leafID, meta, 3);
						}
					}
				}
			}
		}
	}

	public void generateTallTree(World world, int x, int y, int z, ReikaDyeHelper color) {
		if (ColorTreeGenerator.canGenerateTree(world, x, z)) {
			int h = 10+rand.nextInt(3);
			ItemStack log = this.getLogType();
			int meta = color.ordinal();
			int w = 2;

			for (int i = 0; i < h; i++) {
				world.setBlock(x, y+i, z, log.itemID, log.getItemDamage(), 3);
			}
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i*j == 0)
						if (this.canGenerateLeavesAt(world, x+i, y+h-8, z+j)) {
							world.setBlock(x+i, y+h-8, z+j, leafID, meta, 3);
						}
				}
			}
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (this.canGenerateLeavesAt(world, x+i, y+h-7, z+j)) {
						world.setBlock(x+i, y+h-7, z+j, leafID, meta, 3);
					}
				}
			}
			for (int i = -w; i <= w; i++) {
				for (int j = -w; j <= w; j++) {
					if (i*j != w*w && i*j != -w*w)
						if (this.canGenerateLeavesAt(world, x+i, y+h-6, z+j)) {
							world.setBlock(x+i, y+h-6, z+j, leafID, meta, 3);
						}
				}
			}
			for (int i = -w; i <= w; i++) {
				for (int j = -w; j <= w; j++) {
					if (i*j != w*w && i*j != -w*w)
						if (this.canGenerateLeavesAt(world, x+i, y+h-5, z+j)) {
							world.setBlock(x+i, y+h-5, z+j, leafID, meta, 3);
						}
				}
			}
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (this.canGenerateLeavesAt(world, x+i, y+h-4, z+j)) {
						world.setBlock(x+i, y+h-4, z+j, leafID, meta, 3);
					}
				}
			}
			for (int i = -w; i <= w; i++) {
				for (int j = -w; j <= w; j++) {
					if (i*j != w*w && i*j != -w*w)
						if (this.canGenerateLeavesAt(world, x+i, y+h-3, z+j)) {
							world.setBlock(x+i, y+h-3, z+j, leafID, meta, 3);
						}
				}
			}
			for (int i = -w; i <= w; i++) {
				for (int j = -w; j <= w; j++) {
					if (i*j != w*w && i*j != -w*w)
						if (this.canGenerateLeavesAt(world, x+i, y+h-2, z+j)) {
							world.setBlock(x+i, y+h-2, z+j, leafID, meta, 3);
						}
				}
			}
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (this.canGenerateLeavesAt(world, x+i, y+h-1, z+j)) {
						world.setBlock(x+i, y+h-1, z+j, leafID, meta, 3);
					}
				}
			}
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i*j == 0) {
						if (this.canGenerateLeavesAt(world, x+i, y+h, z+j)) {
							world.setBlock(x+i, y+h, z+j, leafID, meta, 3);
						}
					}
				}
			}
		}
	}

	public void generateLumpyTree(World world, int x, int y, int z, ReikaDyeHelper color) {
		if (ColorTreeGenerator.canGenerateTree(world, x, z)) {
			int h = 8+rand.nextInt(4);
			ItemStack log = this.getLogType();
			int meta = color.ordinal();

			for (int i = 0; i < h; i++) {
				world.setBlock(x, y+i, z, log.itemID, log.getItemDamage(), 3);
			}

			for (int i = 1; i < 2; i++) {
				int dx = x+i;
				int dy = y+h-2;
				int dz = z;
				world.setBlock(dx, dy, dz, log.itemID, log.getItemDamage(), 3);
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						for (int m = -1; m <= 1; m++) {
							if (j*k*m == 0 && this.canGenerateLeavesAt(world, dx+j, dy+k, dz+m)) {
								world.setBlock(dx+j, dy+k, dz+m, leafID, meta, 3);
							}
						}
					}
				}

				dx = x-i;
				dz = z;
				world.setBlock(dx, dy, dz, log.itemID, log.getItemDamage(), 3);
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						for (int m = -1; m <= 1; m++) {
							if (j*k*m == 0 && this.canGenerateLeavesAt(world, dx+j, dy+k, dz+m)) {
								world.setBlock(dx+j, dy+k, dz+m, leafID, meta, 3);
							}
						}
					}
				}

				dx = x;
				dz = z-i;
				world.setBlock(dx, dy, dz, log.itemID, log.getItemDamage(), 3);
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						for (int m = -1; m <= 1; m++) {
							if (j*k*m == 0 && this.canGenerateLeavesAt(world, dx+j, dy+k, dz+m)) {
								world.setBlock(dx+j, dy+k, dz+m, leafID, meta, 3);
							}
						}
					}
				}

				dx = x;
				dz = z+i;
				world.setBlock(dx, dy, dz, log.itemID, log.getItemDamage(), 3);
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						for (int m = -1; m <= 1; m++) {
							if (j*k*m == 0 && this.canGenerateLeavesAt(world, dx+j, dy+k, dz+m)) {
								world.setBlock(dx+j, dy+k, dz+m, leafID, meta, 3);
							}
						}
					}
				}

				dx = x+i;
				dy = y+h-6;
				dz = z;
				world.setBlock(dx, dy, dz, log.itemID, log.getItemDamage(), 3);
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						for (int m = -1; m <= 1; m++) {
							if (j*k*m == 0 && this.canGenerateLeavesAt(world, dx+j, dy+k, dz+m)) {
								world.setBlock(dx+j, dy+k, dz+m, leafID, meta, 3);
							}
						}
					}
				}

				dx = x-i;
				dz = z;
				world.setBlock(dx, dy, dz, log.itemID, log.getItemDamage(), 3);
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						for (int m = -1; m <= 1; m++) {
							if (j*k*m == 0 && this.canGenerateLeavesAt(world, dx+j, dy+k, dz+m)) {
								world.setBlock(dx+j, dy+k, dz+m, leafID, meta, 3);
							}
						}
					}
				}

				dx = x;
				dz = z-i;
				world.setBlock(dx, dy, dz, log.itemID, log.getItemDamage(), 3);
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						for (int m = -1; m <= 1; m++) {
							if (j*k*m == 0 && this.canGenerateLeavesAt(world, dx+j, dy+k, dz+m)) {
								world.setBlock(dx+j, dy+k, dz+m, leafID, meta, 3);
							}
						}
					}
				}

				dx = x;
				dz = z+i;
				world.setBlock(dx, dy, dz, log.itemID, log.getItemDamage(), 3);
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						for (int m = -1; m <= 1; m++) {
							if (j*k*m == 0 && this.canGenerateLeavesAt(world, dx+j, dy+k, dz+m)) {
								world.setBlock(dx+j, dy+k, dz+m, leafID, meta, 3);
							}
						}
					}
				}
			}

			int dy = y+h-4;
			for (int i = 1; i < 2; i++) {
				if (this.canGenerateLeavesAt(world, x+i, dy, z)) {
					world.setBlock(x+i, dy, z, leafID, meta, 3);
				}
				if (this.canGenerateLeavesAt(world, x-i, dy, z)) {
					world.setBlock(x-i, dy, z, leafID, meta, 3);
				}
				if (this.canGenerateLeavesAt(world, x, dy, z+i)) {
					world.setBlock(x, dy, z+i, leafID, meta, 3);
				}
				if (this.canGenerateLeavesAt(world, x, dy, z-i)) {
					world.setBlock(x, dy, z-i, leafID, meta, 3);
				}
			}

			dy = y+h;
			for (int k = -1; k <= 1; k++) {
				for (int m = -1; m <= 1; m++) {
					if (k*m == 0 && this.canGenerateLeavesAt(world, x+k, dy, z+m)) {
						world.setBlock(x+k, dy, z+m, leafID, meta, 3);
					}
				}
			}
		}
	}

	private boolean canGenerateLeavesAt(World world, int x, int y, int z) {
		boolean soft = ReikaWorldHelper.softBlocks(world, x, y, z);
		boolean leaves = world.getBlockId(x, y, z) == Block.leaves.blockID;
		//ReikaJavaLibrary.pConsole(x+", "+y+", "+z, !soft && !leaves && world.getBlockId(x, y, z) != Block.wood.blockID);
		return soft || leaves;
	}

	public void generateRainbowTree(World world, int x, int y, int z) {
		y -= 2; //to compensate for the offset Mithion's generator added
		int id = DyeBlocks.RAINBOW.getBlockID();
		ItemStack wood = this.getLogType();
		int log = wood.itemID;
		/*
		world.setBlock(x + 0, y + 0, z + 0, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 1, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 2, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 3, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 4, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 5, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 6, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 7, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 8, Block.dirt.blockID);
		world.setBlock(x + 0, y + 0, z + 9, Block.dirt.blockID);
		world.setBlock(x + 0, y + 1, z + 0, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 1, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 2, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 3, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 4, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 5, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 6, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 7, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 8, Block.grass.blockID);
		world.setBlock(x + 0, y + 1, z + 9, Block.grass.blockID);
		 */
		world.setBlock(x + 0, y + 10, z + 4, id, 0, 3);
		world.setBlock(x + 0, y + 10, z + 5, id, 0, 3);
		world.setBlock(x + 0, y + 11, z + 3, id, 0, 3);
		world.setBlock(x + 0, y + 11, z + 4, id, 0, 3);
		world.setBlock(x + 0, y + 11, z + 5, id, 0, 3);
		world.setBlock(x + 0, y + 11, z + 6, id, 0, 3);
		world.setBlock(x + 0, y + 12, z + 4, id, 0, 3);
		world.setBlock(x + 0, y + 12, z + 5, id, 0, 3);
		world.setBlock(x + 0, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 0, y + 13, z + 4, id, 0, 3);
		world.setBlock(x + 0, y + 13, z + 5, id, 0, 3);
		world.setBlock(x + 0, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 0, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 0, y + 14, z + 4, id, 0, 3);
		world.setBlock(x + 0, y + 14, z + 5, id, 0, 3);
		world.setBlock(x + 0, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 0, y + 15, z + 4, id, 0, 3);
		world.setBlock(x + 0, y + 15, z + 5, id, 0, 3);
		world.setBlock(x + 0, y + 16, z + 3, id, 0, 3);
		world.setBlock(x + 0, y + 16, z + 4, id, 0, 3);
		world.setBlock(x + 0, y + 16, z + 5, id, 0, 3);
		world.setBlock(x + 0, y + 16, z + 6, id, 0, 3);
		world.setBlock(x + 0, y + 17, z + 4, id, 0, 3);
		world.setBlock(x + 0, y + 17, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 1, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 3, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 2, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 3, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 4, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 5, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 6, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 7, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 1, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 1, y + 8, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 8, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 9, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 9, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 9, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 9, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 10, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 10, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 10, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 10, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 11, z + 2, id, 0, 3);
		world.setBlock(x + 1, y + 11, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 11, z + 4, log, 4, 3);
		world.setBlock(x + 1, y + 11, z + 5, log, 4, 3);
		world.setBlock(x + 1, y + 11, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 11, z + 7, id, 0, 3);
		world.setBlock(x + 1, y + 12, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 12, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 12, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 12, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 13, z + 1, id, 0, 3);
		world.setBlock(x + 1, y + 13, z + 2, id, 0, 3);
		world.setBlock(x + 1, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 13, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 13, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 13, z + 7, id, 0, 3);
		world.setBlock(x + 1, y + 13, z + 8, id, 0, 3);
		world.setBlock(x + 1, y + 14, z + 1, id, 0, 3);
		world.setBlock(x + 1, y + 14, z + 2, id, 0, 3);
		world.setBlock(x + 1, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 14, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 14, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 14, z + 7, id, 0, 3);
		world.setBlock(x + 1, y + 14, z + 8, id, 0, 3);
		world.setBlock(x + 1, y + 15, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 15, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 15, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 15, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 16, z + 2, id, 0, 3);
		world.setBlock(x + 1, y + 16, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 16, z + 4, log, 4, 3);
		world.setBlock(x + 1, y + 16, z + 5, log, 4, 3);
		world.setBlock(x + 1, y + 16, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 16, z + 7, id, 0, 3);
		world.setBlock(x + 1, y + 17, z + 3, id, 0, 3);
		world.setBlock(x + 1, y + 17, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 17, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 17, z + 6, id, 0, 3);
		world.setBlock(x + 1, y + 18, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 18, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 19, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 19, z + 5, id, 0, 3);
		world.setBlock(x + 1, y + 20, z + 4, id, 0, 3);
		world.setBlock(x + 1, y + 20, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 2, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 3, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 2, Block.grass.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 3, Block.grass.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 6, Block.grass.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 7, Block.grass.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 2, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 2, y + 2, z + 4, log, 4, 3);
		world.setBlock(x + 2, y + 2, z + 5, log, 4, 3);
		world.setBlock(x + 2, y + 7, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 7, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 8, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 8, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 8, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 8, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 9, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 9, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 9, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 9, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 9, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 9, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 10, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 10, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 10, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 10, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 10, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 10, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 11, z + 1, id, 0, 3);
		world.setBlock(x + 2, y + 11, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 11, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 11, z + 4, log, 4, 3);
		world.setBlock(x + 2, y + 11, z + 5, log, 4, 3);
		world.setBlock(x + 2, y + 11, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 11, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 11, z + 8, id, 0, 3);
		world.setBlock(x + 2, y + 12, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 12, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 12, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 12, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 12, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 12, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 13, z + 1, id, 0, 3);
		world.setBlock(x + 2, y + 13, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 13, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 13, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 13, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 13, z + 8, id, 0, 3);
		world.setBlock(x + 2, y + 14, z + 1, id, 0, 3);
		world.setBlock(x + 2, y + 14, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 14, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 14, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 14, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 14, z + 8, id, 0, 3);
		world.setBlock(x + 2, y + 15, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 15, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 15, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 15, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 15, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 15, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 16, z + 1, id, 0, 3);
		world.setBlock(x + 2, y + 16, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 16, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 16, z + 4, log, 4, 3);
		world.setBlock(x + 2, y + 16, z + 5, log, 4, 3);
		world.setBlock(x + 2, y + 16, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 16, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 16, z + 8, id, 0, 3);
		world.setBlock(x + 2, y + 17, z + 2, id, 0, 3);
		world.setBlock(x + 2, y + 17, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 17, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 17, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 17, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 17, z + 7, id, 0, 3);
		world.setBlock(x + 2, y + 18, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 18, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 18, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 18, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 19, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 19, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 19, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 19, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 20, z + 3, id, 0, 3);
		world.setBlock(x + 2, y + 20, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 20, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 20, z + 6, id, 0, 3);
		world.setBlock(x + 2, y + 21, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 21, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 22, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 22, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 23, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 23, z + 5, id, 0, 3);
		world.setBlock(x + 2, y + 24, z + 4, id, 0, 3);
		world.setBlock(x + 2, y + 24, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 3, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 3, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 3, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 3, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 3, y + 1, z + 2, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 3, y + 1, z + 3, log, 0, 3);
		world.setBlock(x + 3, y + 1, z + 4, log, 0, 3);
		world.setBlock(x + 3, y + 1, z + 5, log, 0, 3);
		world.setBlock(x + 3, y + 1, z + 6, log, 0, 3);
		/*
		world.setBlock(x + 3, y + 1, z + 7, Block.grass.blockID, 3);
		world.setBlock(x + 3, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 3, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 3, y + 2, z + 3, log, 0, 3);
		world.setBlock(x + 3, y + 2, z + 4, log, 0, 3);
		world.setBlock(x + 3, y + 2, z + 5, log, 0, 3);
		world.setBlock(x + 3, y + 2, z + 6, log, 0, 3);
		world.setBlock(x + 3, y + 3, z + 4, log, 0, 3);
		world.setBlock(x + 3, y + 3, z + 5, log, 0, 3);
		world.setBlock(x + 3, y + 4, z + 4, log, 0, 3);
		world.setBlock(x + 3, y + 4, z + 5, log, 0, 3);
		world.setBlock(x + 3, y + 6, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 6, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 7, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 7, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 7, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 7, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 8, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 8, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 8, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 8, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 8, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 8, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 9, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 9, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 9, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 9, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 9, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 9, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 9, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 9, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 10, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 10, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 10, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 10, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 10, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 10, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 10, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 10, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 11, z + 0, id, 0, 3);
		world.setBlock(x + 3, y + 11, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 11, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 11, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 11, z + 4, log, 4, 3);
		world.setBlock(x + 3, y + 11, z + 5, log, 4, 3);
		world.setBlock(x + 3, y + 11, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 11, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 11, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 11, z + 9, id, 0, 3);
		world.setBlock(x + 3, y + 12, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 12, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 12, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 12, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 12, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 12, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 12, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 12, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 0, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 13, z + 9, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 0, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 14, z + 9, id, 0, 3);
		world.setBlock(x + 3, y + 15, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 15, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 15, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 15, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 15, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 15, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 15, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 15, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 16, z + 0, id, 0, 3);
		world.setBlock(x + 3, y + 16, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 16, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 16, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 16, z + 4, log, 4, 3);
		world.setBlock(x + 3, y + 16, z + 5, log, 4, 3);
		world.setBlock(x + 3, y + 16, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 16, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 16, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 16, z + 9, id, 0, 3);
		world.setBlock(x + 3, y + 17, z + 1, id, 0, 3);
		world.setBlock(x + 3, y + 17, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 17, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 17, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 17, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 17, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 17, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 17, z + 8, id, 0, 3);
		world.setBlock(x + 3, y + 18, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 18, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 18, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 18, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 18, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 18, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 19, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 19, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 19, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 19, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 19, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 19, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 20, z + 2, id, 0, 3);
		world.setBlock(x + 3, y + 20, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 20, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 20, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 20, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 20, z + 7, id, 0, 3);
		world.setBlock(x + 3, y + 21, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 21, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 21, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 21, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 22, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 22, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 22, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 22, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 23, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 23, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 23, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 23, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 24, z + 3, id, 0, 3);
		world.setBlock(x + 3, y + 24, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 24, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 24, z + 6, id, 0, 3);
		world.setBlock(x + 3, y + 25, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 25, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 26, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 26, z + 5, id, 0, 3);
		world.setBlock(x + 3, y + 27, z + 4, id, 0, 3);
		world.setBlock(x + 3, y + 27, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 4, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 4, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 4, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 4, y + 0, z + 3, Block.dirt.blockID, 3);
		 */
		world.setBlock(x + 4, y + 0, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 0, z + 5, log, 0, 3);
		/*
		world.setBlock(x + 4, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 4, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 4, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 4, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 4, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 4, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 4, y + 1, z + 2, Block.dirt.blockID, 3);
		 */
		world.setBlock(x + 4, y + 1, z + 3, log, 0, 3);
		world.setBlock(x + 4, y + 1, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 1, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 1, z + 6, log, 0, 3);
		/*
		world.setBlock(x + 4, y + 1, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 4, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 4, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 4, y + 2, z + 2, log, 8, 3);
		world.setBlock(x + 4, y + 2, z + 3, log, 0, 3);
		world.setBlock(x + 4, y + 2, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 2, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 2, z + 6, log, 0, 3);
		world.setBlock(x + 4, y + 2, z + 7, log, 8, 3);
		world.setBlock(x + 4, y + 3, z + 3, log, 0, 3);
		world.setBlock(x + 4, y + 3, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 3, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 3, z + 6, log, 0, 3);
		world.setBlock(x + 4, y + 4, z + 3, log, 0, 3);
		world.setBlock(x + 4, y + 4, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 4, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 4, z + 6, log, 0, 3);
		world.setBlock(x + 4, y + 5, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 5, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 6, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 6, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 6, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 6, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 7, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 7, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 7, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 7, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 7, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 7, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 8, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 8, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 8, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 8, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 8, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 8, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 8, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 8, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 9, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 9, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 9, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 9, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 9, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 9, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 9, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 9, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 10, z + 0, id, 0, 3);
		world.setBlock(x + 4, y + 10, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 10, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 10, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 10, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 10, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 10, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 10, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 10, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 10, z + 9, id, 0, 3);
		world.setBlock(x + 4, y + 11, z + 0, id, 0, 3);
		world.setBlock(x + 4, y + 11, z + 1, log, 8, 3);
		world.setBlock(x + 4, y + 11, z + 2, log, 8, 3);
		world.setBlock(x + 4, y + 11, z + 3, log, 8, 3);
		world.setBlock(x + 4, y + 11, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 11, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 11, z + 6, log, 8, 3);
		world.setBlock(x + 4, y + 11, z + 7, log, 8, 3);
		world.setBlock(x + 4, y + 11, z + 8, log, 8, 3);
		world.setBlock(x + 4, y + 11, z + 9, id, 0, 3);
		world.setBlock(x + 4, y + 12, z + 0, id, 0, 3);
		world.setBlock(x + 4, y + 12, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 12, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 12, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 12, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 12, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 12, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 12, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 12, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 12, z + 9, id, 0, 3);
		world.setBlock(x + 4, y + 13, z + 0, id, 0, 3);
		world.setBlock(x + 4, y + 13, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 13, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 13, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 13, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 13, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 13, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 13, z + 9, id, 0, 3);
		world.setBlock(x + 4, y + 14, z + 0, id, 0, 3);
		world.setBlock(x + 4, y + 14, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 14, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 14, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 14, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 14, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 14, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 14, z + 9, id, 0, 3);
		world.setBlock(x + 4, y + 15, z + 0, id, 0, 3);
		world.setBlock(x + 4, y + 15, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 15, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 15, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 15, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 15, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 15, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 15, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 15, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 15, z + 9, id, 0, 3);
		world.setBlock(x + 4, y + 16, z + 0, id, 0, 3);
		world.setBlock(x + 4, y + 16, z + 1, log, 8, 3);
		world.setBlock(x + 4, y + 16, z + 2, log, 8, 3);
		world.setBlock(x + 4, y + 16, z + 3, log, 8, 3);
		world.setBlock(x + 4, y + 16, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 16, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 16, z + 6, log, 8, 3);
		world.setBlock(x + 4, y + 16, z + 7, log, 8, 3);
		world.setBlock(x + 4, y + 16, z + 8, log, 8, 3);
		world.setBlock(x + 4, y + 16, z + 9, id, 0, 3);
		world.setBlock(x + 4, y + 17, z + 0, id, 0, 3);
		world.setBlock(x + 4, y + 17, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 17, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 17, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 17, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 17, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 17, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 17, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 17, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 17, z + 9, id, 0, 3);
		world.setBlock(x + 4, y + 18, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 18, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 18, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 18, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 18, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 18, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 18, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 18, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 19, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 19, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 19, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 19, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 19, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 19, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 19, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 19, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 20, z + 1, id, 0, 3);
		world.setBlock(x + 4, y + 20, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 20, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 20, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 20, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 20, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 20, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 20, z + 8, id, 0, 3);
		world.setBlock(x + 4, y + 21, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 21, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 21, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 21, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 21, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 21, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 22, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 22, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 22, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 22, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 22, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 22, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 23, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 23, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 23, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 23, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 23, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 23, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 24, z + 2, id, 0, 3);
		world.setBlock(x + 4, y + 24, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 24, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 24, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 24, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 24, z + 7, id, 0, 3);
		world.setBlock(x + 4, y + 25, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 25, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 25, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 25, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 26, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 26, z + 4, log, 0, 3);
		world.setBlock(x + 4, y + 26, z + 5, log, 0, 3);
		world.setBlock(x + 4, y + 26, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 27, z + 3, id, 0, 3);
		world.setBlock(x + 4, y + 27, z + 4, id, 0, 3);
		world.setBlock(x + 4, y + 27, z + 5, id, 0, 3);
		world.setBlock(x + 4, y + 27, z + 6, id, 0, 3);
		world.setBlock(x + 4, y + 28, z + 4, id, 0, 3);
		world.setBlock(x + 4, y + 28, z + 5, id, 0, 3);
		world.setBlock(x + 4, y + 29, z + 4, id, 0, 3);
		world.setBlock(x + 4, y + 29, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 5, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 5, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 5, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 5, y + 0, z + 3, Block.dirt.blockID, 3);
		 */
		world.setBlock(x + 5, y + 0, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 0, z + 5, log, 0, 3);
		/*
		world.setBlock(x + 5, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 5, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 5, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 5, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 5, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 5, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 5, y + 1, z + 2, Block.dirt.blockID, 3);
		 */
		world.setBlock(x + 5, y + 1, z + 3, log, 0, 3);
		world.setBlock(x + 5, y + 1, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 1, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 1, z + 6, log, 0, 3);
		/*
		world.setBlock(x + 5, y + 1, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 5, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 5, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 5, y + 2, z + 2, log, 8, 3);
		world.setBlock(x + 5, y + 2, z + 3, log, 0, 3);
		world.setBlock(x + 5, y + 2, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 2, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 2, z + 6, log, 0, 3);
		world.setBlock(x + 5, y + 2, z + 7, log, 8, 3);
		world.setBlock(x + 5, y + 3, z + 3, log, 0, 3);
		world.setBlock(x + 5, y + 3, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 3, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 3, z + 6, log, 0, 3);
		world.setBlock(x + 5, y + 4, z + 3, log, 0, 3);
		world.setBlock(x + 5, y + 4, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 4, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 4, z + 6, log, 0, 3);
		world.setBlock(x + 5, y + 5, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 5, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 6, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 6, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 6, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 6, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 7, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 7, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 7, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 7, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 7, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 7, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 8, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 8, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 8, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 8, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 8, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 8, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 8, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 8, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 9, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 9, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 9, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 9, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 9, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 9, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 9, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 9, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 10, z + 0, id, 0, 3);
		world.setBlock(x + 5, y + 10, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 10, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 10, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 10, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 10, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 10, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 10, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 10, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 10, z + 9, id, 0, 3);
		world.setBlock(x + 5, y + 11, z + 0, id, 0, 3);
		world.setBlock(x + 5, y + 11, z + 1, log, 8, 3);
		world.setBlock(x + 5, y + 11, z + 2, log, 8, 3);
		world.setBlock(x + 5, y + 11, z + 3, log, 8, 3);
		world.setBlock(x + 5, y + 11, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 11, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 11, z + 6, log, 8, 3);
		world.setBlock(x + 5, y + 11, z + 7, log, 8, 3);
		world.setBlock(x + 5, y + 11, z + 8, log, 8, 3);
		world.setBlock(x + 5, y + 11, z + 9, id, 0, 3);
		world.setBlock(x + 5, y + 12, z + 0, id, 0, 3);
		world.setBlock(x + 5, y + 12, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 12, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 12, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 12, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 12, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 12, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 12, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 12, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 12, z + 9, id, 0, 3);
		world.setBlock(x + 5, y + 13, z + 0, id, 0, 3);
		world.setBlock(x + 5, y + 13, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 13, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 13, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 13, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 13, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 13, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 13, z + 9, id, 0, 3);
		world.setBlock(x + 5, y + 14, z + 0, id, 0, 3);
		world.setBlock(x + 5, y + 14, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 14, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 14, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 14, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 14, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 14, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 14, z + 9, id, 0, 3);
		world.setBlock(x + 5, y + 15, z + 0, id, 0, 3);
		world.setBlock(x + 5, y + 15, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 15, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 15, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 15, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 15, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 15, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 15, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 15, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 15, z + 9, id, 0, 3);
		world.setBlock(x + 5, y + 16, z + 0, id, 0, 3);
		world.setBlock(x + 5, y + 16, z + 1, log, 8, 3);
		world.setBlock(x + 5, y + 16, z + 2, log, 8, 3);
		world.setBlock(x + 5, y + 16, z + 3, log, 8, 3);
		world.setBlock(x + 5, y + 16, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 16, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 16, z + 6, log, 8, 3);
		world.setBlock(x + 5, y + 16, z + 7, log, 8, 3);
		world.setBlock(x + 5, y + 16, z + 8, log, 8, 3);
		world.setBlock(x + 5, y + 16, z + 9, id, 0, 3);
		world.setBlock(x + 5, y + 17, z + 0, id, 0, 3);
		world.setBlock(x + 5, y + 17, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 17, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 17, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 17, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 17, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 17, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 17, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 17, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 17, z + 9, id, 0, 3);
		world.setBlock(x + 5, y + 18, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 18, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 18, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 18, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 18, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 18, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 18, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 18, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 19, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 19, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 19, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 19, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 19, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 19, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 19, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 19, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 20, z + 1, id, 0, 3);
		world.setBlock(x + 5, y + 20, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 20, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 20, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 20, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 20, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 20, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 20, z + 8, id, 0, 3);
		world.setBlock(x + 5, y + 21, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 21, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 21, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 21, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 21, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 21, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 22, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 22, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 22, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 22, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 22, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 22, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 23, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 23, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 23, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 23, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 23, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 23, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 24, z + 2, id, 0, 3);
		world.setBlock(x + 5, y + 24, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 24, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 24, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 24, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 24, z + 7, id, 0, 3);
		world.setBlock(x + 5, y + 25, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 25, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 25, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 25, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 26, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 26, z + 4, log, 0, 3);
		world.setBlock(x + 5, y + 26, z + 5, log, 0, 3);
		world.setBlock(x + 5, y + 26, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 27, z + 3, id, 0, 3);
		world.setBlock(x + 5, y + 27, z + 4, id, 0, 3);
		world.setBlock(x + 5, y + 27, z + 5, id, 0, 3);
		world.setBlock(x + 5, y + 27, z + 6, id, 0, 3);
		world.setBlock(x + 5, y + 28, z + 4, id, 0, 3);
		world.setBlock(x + 5, y + 28, z + 5, id, 0, 3);
		world.setBlock(x + 5, y + 29, z + 4, id, 0, 3);
		world.setBlock(x + 5, y + 29, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 6, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 3, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 6, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 6, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 6, y + 1, z + 2, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 6, y + 1, z + 3, log, 0, 3);
		world.setBlock(x + 6, y + 1, z + 4, log, 0, 3);
		world.setBlock(x + 6, y + 1, z + 5, log, 0, 3);
		world.setBlock(x + 6, y + 1, z + 6, log, 0, 3);
		/*
		world.setBlock(x + 6, y + 1, z + 7, Block.grass.blockID, 3);
		world.setBlock(x + 6, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 6, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 6, y + 2, z + 3, log, 0, 3);
		world.setBlock(x + 6, y + 2, z + 4, log, 0, 3);
		world.setBlock(x + 6, y + 2, z + 5, log, 0, 3);
		world.setBlock(x + 6, y + 2, z + 6, log, 0, 3);
		world.setBlock(x + 6, y + 3, z + 4, log, 0, 3);
		world.setBlock(x + 6, y + 3, z + 5, log, 0, 3);
		world.setBlock(x + 6, y + 4, z + 4, log, 0, 3);
		world.setBlock(x + 6, y + 4, z + 5, log, 0, 3);
		world.setBlock(x + 6, y + 6, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 6, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 7, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 7, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 7, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 7, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 8, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 8, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 8, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 8, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 8, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 8, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 9, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 9, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 9, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 9, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 9, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 9, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 9, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 9, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 10, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 10, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 10, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 10, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 10, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 10, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 10, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 10, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 11, z + 0, id, 0, 3);
		world.setBlock(x + 6, y + 11, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 11, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 11, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 11, z + 4, log, 4, 3);
		world.setBlock(x + 6, y + 11, z + 5, log, 4, 3);
		world.setBlock(x + 6, y + 11, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 11, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 11, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 11, z + 9, id, 0, 3);
		world.setBlock(x + 6, y + 12, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 12, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 12, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 12, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 12, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 12, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 12, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 12, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 0, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 13, z + 9, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 0, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 14, z + 9, id, 0, 3);
		world.setBlock(x + 6, y + 15, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 15, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 15, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 15, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 15, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 15, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 15, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 15, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 16, z + 0, id, 0, 3);
		world.setBlock(x + 6, y + 16, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 16, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 16, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 16, z + 4, log, 4, 3);
		world.setBlock(x + 6, y + 16, z + 5, log, 4, 3);
		world.setBlock(x + 6, y + 16, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 16, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 16, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 16, z + 9, id, 0, 3);
		world.setBlock(x + 6, y + 17, z + 1, id, 0, 3);
		world.setBlock(x + 6, y + 17, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 17, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 17, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 17, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 17, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 17, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 17, z + 8, id, 0, 3);
		world.setBlock(x + 6, y + 18, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 18, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 18, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 18, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 18, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 18, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 19, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 19, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 19, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 19, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 19, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 19, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 20, z + 2, id, 0, 3);
		world.setBlock(x + 6, y + 20, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 20, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 20, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 20, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 20, z + 7, id, 0, 3);
		world.setBlock(x + 6, y + 21, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 21, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 21, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 21, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 22, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 22, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 22, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 22, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 23, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 23, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 23, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 23, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 24, z + 3, id, 0, 3);
		world.setBlock(x + 6, y + 24, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 24, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 24, z + 6, id, 0, 3);
		world.setBlock(x + 6, y + 25, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 25, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 26, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 26, z + 5, id, 0, 3);
		world.setBlock(x + 6, y + 27, z + 4, id, 0, 3);
		world.setBlock(x + 6, y + 27, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 7, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 3, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 2, Block.grass.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 3, Block.grass.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 6, Block.grass.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 7, Block.grass.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 7, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 7, y + 2, z + 4, log, 4, 3);
		world.setBlock(x + 7, y + 2, z + 5, log, 4, 3);
		world.setBlock(x + 7, y + 7, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 7, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 8, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 8, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 8, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 8, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 9, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 9, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 9, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 9, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 9, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 9, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 10, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 10, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 10, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 10, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 10, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 10, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 11, z + 1, id, 0, 3);
		world.setBlock(x + 7, y + 11, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 11, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 11, z + 4, log, 4, 3);
		world.setBlock(x + 7, y + 11, z + 5, log, 4, 3);
		world.setBlock(x + 7, y + 11, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 11, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 11, z + 8, id, 0, 3);
		world.setBlock(x + 7, y + 12, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 12, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 12, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 12, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 12, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 12, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 13, z + 1, id, 0, 3);
		world.setBlock(x + 7, y + 13, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 13, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 13, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 13, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 13, z + 8, id, 0, 3);
		world.setBlock(x + 7, y + 14, z + 1, id, 0, 3);
		world.setBlock(x + 7, y + 14, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 14, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 14, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 14, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 14, z + 8, id, 0, 3);
		world.setBlock(x + 7, y + 15, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 15, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 15, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 15, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 15, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 15, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 16, z + 1, id, 0, 3);
		world.setBlock(x + 7, y + 16, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 16, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 16, z + 4, log, 4, 3);
		world.setBlock(x + 7, y + 16, z + 5, log, 4, 3);
		world.setBlock(x + 7, y + 16, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 16, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 16, z + 8, id, 0, 3);
		world.setBlock(x + 7, y + 17, z + 2, id, 0, 3);
		world.setBlock(x + 7, y + 17, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 17, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 17, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 17, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 17, z + 7, id, 0, 3);
		world.setBlock(x + 7, y + 18, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 18, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 18, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 18, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 19, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 19, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 19, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 19, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 20, z + 3, id, 0, 3);
		world.setBlock(x + 7, y + 20, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 20, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 20, z + 6, id, 0, 3);
		world.setBlock(x + 7, y + 21, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 21, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 22, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 22, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 23, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 23, z + 5, id, 0, 3);
		world.setBlock(x + 7, y + 24, z + 4, id, 0, 3);
		world.setBlock(x + 7, y + 24, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 8, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 3, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 2, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 3, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 4, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 5, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 6, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 7, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 8, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 8, y + 8, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 8, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 9, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 9, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 9, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 9, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 10, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 10, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 10, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 10, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 11, z + 2, id, 0, 3);
		world.setBlock(x + 8, y + 11, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 11, z + 4, log, 4, 3);
		world.setBlock(x + 8, y + 11, z + 5, log, 4, 3);
		world.setBlock(x + 8, y + 11, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 11, z + 7, id, 0, 3);
		world.setBlock(x + 8, y + 12, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 12, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 12, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 12, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 13, z + 1, id, 0, 3);
		world.setBlock(x + 8, y + 13, z + 2, id, 0, 3);
		world.setBlock(x + 8, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 13, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 13, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 13, z + 7, id, 0, 3);
		world.setBlock(x + 8, y + 13, z + 8, id, 0, 3);
		world.setBlock(x + 8, y + 14, z + 1, id, 0, 3);
		world.setBlock(x + 8, y + 14, z + 2, id, 0, 3);
		world.setBlock(x + 8, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 14, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 14, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 14, z + 7, id, 0, 3);
		world.setBlock(x + 8, y + 14, z + 8, id, 0, 3);
		world.setBlock(x + 8, y + 15, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 15, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 15, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 15, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 16, z + 2, id, 0, 3);
		world.setBlock(x + 8, y + 16, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 16, z + 4, log, 4, 3);
		world.setBlock(x + 8, y + 16, z + 5, log, 4, 3);
		world.setBlock(x + 8, y + 16, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 16, z + 7, id, 0, 3);
		world.setBlock(x + 8, y + 17, z + 3, id, 0, 3);
		world.setBlock(x + 8, y + 17, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 17, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 17, z + 6, id, 0, 3);
		world.setBlock(x + 8, y + 18, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 18, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 19, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 19, z + 5, id, 0, 3);
		world.setBlock(x + 8, y + 20, z + 4, id, 0, 3);
		world.setBlock(x + 8, y + 20, z + 5, id, 0, 3);
		/*
		world.setBlock(x + 9, y + 0, z + 0, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 1, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 2, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 3, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 4, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 5, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 6, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 7, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 8, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 0, z + 9, Block.dirt.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 0, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 1, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 2, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 3, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 4, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 5, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 6, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 7, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 8, Block.grass.blockID, 3);
		world.setBlock(x + 9, y + 1, z + 9, Block.grass.blockID, 3);
		 */
		world.setBlock(x + 9, y + 10, z + 4, id, 0, 3);
		world.setBlock(x + 9, y + 10, z + 5, id, 0, 3);
		world.setBlock(x + 9, y + 11, z + 3, id, 0, 3);
		world.setBlock(x + 9, y + 11, z + 4, id, 0, 3);
		world.setBlock(x + 9, y + 11, z + 5, id, 0, 3);
		world.setBlock(x + 9, y + 11, z + 6, id, 0, 3);
		world.setBlock(x + 9, y + 12, z + 4, id, 0, 3);
		world.setBlock(x + 9, y + 12, z + 5, id, 0, 3);
		world.setBlock(x + 9, y + 13, z + 3, id, 0, 3);
		world.setBlock(x + 9, y + 13, z + 4, id, 0, 3);
		world.setBlock(x + 9, y + 13, z + 5, id, 0, 3);
		world.setBlock(x + 9, y + 13, z + 6, id, 0, 3);
		world.setBlock(x + 9, y + 14, z + 3, id, 0, 3);
		world.setBlock(x + 9, y + 14, z + 4, id, 0, 3);
		world.setBlock(x + 9, y + 14, z + 5, id, 0, 3);
		world.setBlock(x + 9, y + 14, z + 6, id, 0, 3);
		world.setBlock(x + 9, y + 15, z + 4, id, 0, 3);
		world.setBlock(x + 9, y + 15, z + 5, id, 0, 3);
		world.setBlock(x + 9, y + 16, z + 3, id, 0, 3);
		world.setBlock(x + 9, y + 16, z + 4, id, 0, 3);
		world.setBlock(x + 9, y + 16, z + 5, id, 0, 3);
		world.setBlock(x + 9, y + 16, z + 6, id, 0, 3);
		world.setBlock(x + 9, y + 17, z + 4, id, 0, 3);
		world.setBlock(x + 9, y + 17, z + 5, id, 0, 3);
	}

}
