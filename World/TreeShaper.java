package Reika.DyeTrees.World;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import Reika.DragonAPI.Instantiable.MultiBlockBlueprint;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.Registry.DyeBlocks;

public class TreeShaper {

	private final MultiBlockBlueprint tree = new MultiBlockBlueprint(5, 8, 5);
	private final MultiBlockBlueprint tallTree = new MultiBlockBlueprint(5, 16, 5); //think fake silverwoods
	private final MultiBlockBlueprint lumpyTree = new MultiBlockBlueprint(8, 16, 8); //think large sakuras

	public static final int LUMPY_CHANCE = 10;
	public static final int TALL_CHANCE = 30;
	public static final int NORMAL_CHANCE = 60;

	private static final int leafID = DyeBlocks.DECAY.getBlockID();

	private static final TreeShaper instance = new TreeShaper();

	private TreeShaper() {
		Random rand = new Random();
		this.setupMainTree(rand);
	}

	public static TreeShaper getInstance() {
		return instance;
	}

	private void setupMainTree(Random r) {
		ReikaDyeHelper color = ReikaDyeHelper.dyes[r.nextInt(16)];
		int h = 5+r.nextInt(3);
		tree.addOverwriteableID(Block.leaves.blockID);
		int x = 0;
		int y = 0;
		int z = 0;
		int meta = color.ordinal();
		int log = r.nextInt(4);
		int w = 2;
		for (int i = 0; i < h; i++) {
			tree.addBlockAt(x, y+i, z, Block.wood.blockID, log);
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				tree.addBlockAt(x+i, y+h-3, z+j, leafID, meta);
			}
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				tree.addBlockAt(x+i, y+h-2, z+j, leafID, meta);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				tree.addBlockAt(x+i, y+h-1, z+j, leafID, meta);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i*j == 0)
					tree.addBlockAt(x+i, y+h, z+j, leafID, meta);
			}
		}
	}

	private void setupTallTree(Random r) {
		ReikaDyeHelper color = ReikaDyeHelper.dyes[r.nextInt(16)];
		int h = 16;
		int x = 0;
		int y = 0;
		int z = 0;
		int log = r.nextInt(4);
		int w = 2;
		int meta = color.ordinal();
		for (int i = 0; i < h; i++) {
			tallTree.addBlockAt(x, y+i, z, Block.wood.blockID, log);
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i*j == 0)
					tallTree.addBlockAt(x+i, y+3, z+j, leafID, meta);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				tallTree.addBlockAt(x+i, y+4, z+j, leafID, meta);
			}
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				tallTree.addBlockAt(x+i, y+5, z+j, leafID, meta);
			}
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				tallTree.addBlockAt(x+i, y+6, z+j, leafID, meta);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				tallTree.addBlockAt(x+i, y+7, z+j, leafID, meta);
			}
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				tallTree.addBlockAt(x+i, y+8, z+j, leafID, meta);
			}
		}
		for (int i = -w; i <= w; i++) {
			for (int j = -w; j <= w; j++) {
				tallTree.addBlockAt(x+i, y+9, z+j, leafID, meta);
			}
		}
	}

	public void generateNormalTree(World world, int x, int y, int z) {
		if (ColorTreeGenerator.canGenerateTree(world, x, z))
			tree.createInWorld(world, x, y, z);
	}

	public void generateTallTree(World world, int x, int y, int z) {
		if (ColorTreeGenerator.canGenerateTree(world, x, z))
			tallTree.createInWorld(world, x, y, z);
	}

	public void generateLumpyTree(World world, int x, int y, int z) {
		if (ColorTreeGenerator.canGenerateTree(world, x, z))
			lumpyTree.createInWorld(world, x, y, z);
	}

}
