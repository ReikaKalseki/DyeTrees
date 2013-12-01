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

import java.lang.reflect.Field;
import java.net.URL;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.Auxiliary.BiomeCollisionTracker;
import Reika.DragonAPI.Auxiliary.RetroGenController;
import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Instantiable.IO.ControlledConfig;
import Reika.DragonAPI.Instantiable.IO.ModLogger;
import Reika.DragonAPI.Libraries.ReikaRegistryHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DyeTrees.Registry.DyeBlocks;
import Reika.DyeTrees.Registry.DyeOptions;
import Reika.DyeTrees.World.BiomeRainbowForest;
import Reika.DyeTrees.World.ColorTreeGenerator;
import Reika.DyeTrees.World.RetroDyeTreeGen;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "DyeTrees", name="Dye Trees", version="beta", certificateFingerprint = "@GET_FINGERPRINT@", dependencies="after:DragonAPI")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class DyeTrees extends DragonAPIMod {

	@Instance("DyeTrees")
	public static DyeTrees instance = new DyeTrees();

	public static final ControlledConfig config = new ControlledConfig(instance, DyeOptions.optionList, DyeBlocks.blockList, null, null, 0);

	public static ModLogger logger;

	public static Block[] blocks = new Block[DyeBlocks.blockList.length];

	public static CreativeTabs dyeTreeTab = new DyeTreeTab(CreativeTabs.getNextID(), "Dye Trees");

	public static BiomeGenBase forest;
	private static RenderSlime slimeRenderer;

	@Override
	@EventHandler
	public void preload(FMLPreInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(DyeEventController.instance);

		config.loadSubfolderedConfigFile(evt);
		config.initProps(evt);
		logger = new ModLogger(instance, DyeOptions.LOGLOADING.getState(), DyeOptions.DEBUGMODE.getState(), false);

		ReikaRegistryHelper.setupModData(instance, evt);
		ReikaRegistryHelper.setupVersionChecking(evt);

		BiomeCollisionTracker.instance.addBiomeID(ModList.DYETREES, DyeOptions.BIOMEID.getValue(), BiomeRainbowForest.class);
	}

	@Override
	@EventHandler
	public void load(FMLInitializationEvent event) {
		this.loadClasses();
		GameRegistry.registerWorldGenerator(new ColorTreeGenerator());
		if (DyeOptions.RETROGEN.getState()) {
			RetroGenController.getInstance().addRetroGenerator(new RetroDyeTreeGen());
			//Set state back
		}
		if (DyeOptions.BIOME.getState()) {
			forest = new BiomeRainbowForest(DyeOptions.BIOMEID.getValue());
			GameRegistry.addBiome(forest);
			BiomeDictionary.registerBiomeType(forest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.HILLS);
		}

		if (!DyeOptions.BIOME.getState() && !DyeOptions.NORMAL.getState()) {
			logger.log("Both rainbow forest biomes and normal dye tree generation disabled.");
			logger.log("To ensure sapling and tree obtainability, dye saplings are now craftable.");
			for (int i = 0; i < 16; i++) {
				GameRegistry.addShapelessRecipe(new ItemStack(DyeBlocks.SAPLING.getBlockID(), 1, i), Block.sapling, ReikaDyeHelper.dyes[i].getStackOf());
			}
		}

		this.addCompat();
	}

	private void addCompat() {
		if (ModList.TREECAPITATOR.isLoaded()) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("modID", "DyeTrees");
			NBTTagList treeList = new NBTTagList();
			NBTTagCompound tree = new NBTTagCompound();
			tree.setString("treeName", "DyeTree");
			tree.setString("logs", String.format("%d", Block.wood.blockID));
			tree.setString("leaves", String.format("%d; %d", DyeBlocks.LEAF.getID(), DyeBlocks.DECAY.getID()));
			tree.setInteger("maxHorLeafBreakDist", 8);
			tree.setBoolean("requireLeafDecayCheck", false);
			treeList.appendTag(tree);
			nbt.setTag("trees", treeList);
			FMLInterModComms.sendMessage("TreeCapitator", "ThirdPartyModConfig", nbt);
			logger.log("Adding TreeCapitator support");
		}
	}

	@Override
	@EventHandler
	public void postload(FMLPostInitializationEvent evt) {
		if (DyeOptions.COLORSLIMES.getState() && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			slimeRenderer = (RenderSlime)RenderManager.instance.entityRenderMap.get(EntitySlime.class);
			Field f;
			try {
				f = slimeRenderer.getClass().getDeclaredField("scaleAmount");
				f.setAccessible(true);
				f.set(slimeRenderer, new ColorizableSlimeModel(0));
				logger.log("Overriding Slime Renderer Edge Model.");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadClasses() {
		ReikaRegistryHelper.instantiateAndRegisterBlocks(instance, DyeBlocks.blockList, blocks);
	}

	@Override
	public String getDisplayName() {
		return "Dye Trees";
	}

	@Override
	public String getModAuthorName() {
		return "Reika";
	}

	@Override
	public URL getDocumentationSite() {
		return DragonAPICore.getReikaForumPage(instance);
	}

	@Override
	public boolean hasWiki() {
		return false;
	}

	@Override
	public URL getWiki() {
		return null;
	}

	@Override
	public boolean hasVersion() {
		return false;
	}

	@Override
	public String getVersionName() {
		return null;
	}

	@Override
	public ModLogger getModLogger() {
		return logger;
	}

}
