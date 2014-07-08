/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.Aspect;
import ttftcuts.atg.api.ATGBiomes;
import ttftcuts.atg.api.ATGBiomes.BiomeType;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.Auxiliary.BiomeCollisionTracker;
import Reika.DragonAPI.Auxiliary.CommandableUpdateChecker;
import Reika.DragonAPI.Auxiliary.RetroGenController;
import Reika.DragonAPI.Auxiliary.SuggestedModsTracker;
import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Instantiable.IO.ModLogger;
import Reika.DragonAPI.Libraries.ReikaRegistryHelper;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DragonAPI.ModInteract.ReikaThaumHelper;
import Reika.DyeTrees.Registry.DyeBlocks;
import Reika.DyeTrees.Registry.DyeItems;
import Reika.DyeTrees.Registry.DyeOptions;
import Reika.DyeTrees.World.BiomeRainbowForest;
import Reika.DyeTrees.World.ColorTreeGenerator;
import Reika.DyeTrees.World.RetroDyeTreeGen;
import Reika.RotaryCraft.API.BlockColorInterface;
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

@Mod(modid = "DyeTrees", name="Dye Trees", version="beta", certificateFingerprint = "@GET_FINGERPRINT@", dependencies="required-after:DragonAPI")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class DyeTrees extends DragonAPIMod {

	@Instance("DyeTrees")
	public static DyeTrees instance = new DyeTrees();

	public static final DyeConfig config = new DyeConfig(instance, DyeOptions.optionList, DyeBlocks.blockList, DyeItems.itemList, null, 0);

	public static ModLogger logger;

	public static Block[] blocks = new Block[DyeBlocks.blockList.length];
	public static Item[] items = new Item[DyeItems.itemList.length];

	public static CreativeTabs dyeTreeTab = new DyeTreeTab(CreativeTabs.getNextID(), "Dye Trees");

	public static BiomeGenBase forest;

	@Override
	@EventHandler
	public void preload(FMLPreInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(DyeEventController.instance);
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			MinecraftForge.EVENT_BUS.register(DyeClientEventController.instance);

		config.loadSubfolderedConfigFile(evt);
		config.initProps(evt);
		logger = new ModLogger(instance, DyeOptions.LOGLOADING.getState(), DyeOptions.DEBUGMODE.getState(), false);

		BiomeCollisionTracker.instance.addBiomeID(instance, DyeOptions.BIOMEID.getValue(), BiomeRainbowForest.class);

		this.basicSetup(evt);
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
		forest = new BiomeRainbowForest(DyeOptions.BIOMEID.getValue());
		if (DyeOptions.OVERWORLD.getState())
			GameRegistry.addBiome(forest);
		BiomeDictionary.registerBiomeType(forest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.HILLS);

		if ((!DyeOptions.OVERWORLD.getState()) && !DyeOptions.NORMAL.getState()) {
			logger.log("Both rainbow forest biomes and normal dye tree generation disabled.");
			logger.log("To ensure sapling and tree obtainability, dye saplings are now craftable.");
			for (int i = 0; i < 16; i++) {
				GameRegistry.addShapelessRecipe(new ItemStack(DyeBlocks.SAPLING.getBlockID(), 1, i), Block.sapling, ReikaDyeHelper.dyes[i].getStackOf());
			}
		}

		for (int i = 0; i < 16; i++) {
			ReikaDyeHelper dye = ReikaDyeHelper.dyes[i];
			ItemStack used = DyeOptions.isVanillaDyeMoreCommon() ? dye.getStackOf() : DyeItems.DYE.getStackOfMetadata(i);
			ItemStack sapling = new ItemStack(DyeBlocks.SAPLING.getBlockID(), 1, i);
			ItemStack flower = new ItemStack(DyeBlocks.FLOWER.getBlockID(), 1, i);
			GameRegistry.addRecipe(new ItemStack(DyeBlocks.DYE.getBlockID(), 1, i), "ddd", "ddd", "ddd", 'd', used);
			GameRegistry.addShapelessRecipe(used, flower);
			OreDictionary.registerOre(dye.getOreDictName(), DyeItems.DYE.getStackOfMetadata(i));
			OreDictionary.registerOre("treeSapling", sapling);
			OreDictionary.registerOre("plant"+dye.colorNameNoSpaces, flower);
			OreDictionary.registerOre("flower"+dye.colorNameNoSpaces, flower);
		}

		if (DyeOptions.doesVanillaDyeDrop()) {

		}
		else {
			logger.log("Configs were set such that trees do not drop vanilla dyes! Loading interface recipes to ensure farmability!");
			for (int i = 0; i < 16; i++) {
				ReikaDyeHelper dye = ReikaDyeHelper.dyes[i];
				Object[] in = this.getIntercraft(dye);
				Object[] sub = new Object[in.length-1];
				System.arraycopy(in, 1, sub, 0, sub.length);
				boolean shaped = (Boolean)in[0];
				if (shaped) {
					GameRegistry.addRecipe(dye.getStackOf(), sub);
				}
				else {
					GameRegistry.addShapelessRecipe(dye.getStackOf(), sub);
				}
			}
		}

		this.addCompat();

		ATGBiomes.addBiome(BiomeType.LAND, "Forest", forest, 1.0);

		SuggestedModsTracker.instance.addSuggestedMod(instance, ModList.THAUMCRAFT, "Worldgen integration");
	}

	private Object[] getIntercraft(ReikaDyeHelper dye) {
		switch(dye) {
		//case BLACK:
		//	break;
		case BLUE:
			return new Object[]{true, "DDD", "DSD", "DDD", 'D', DyeItems.DYE.getStackOfMetadata(ReikaDyeHelper.BLUE.ordinal()), 'S', Block.stone};
		case BROWN:
			return new Object[]{false, Item.seeds, DyeItems.DYE.getStackOfMetadata(dye.ordinal())};
			//case GREEN:
			//	return new Object[]{false, Item.paper, DyeItems.DYE.getStackOfMetadata(dye.ordinal())};
		case WHITE:
			return new Object[]{false, Item.sugar, DyeItems.DYE.getStackOfMetadata(ReikaDyeHelper.WHITE.ordinal()), Item.wheat};
		default:
			return new Object[]{false, DyeItems.DYE.getStackOfMetadata(dye.ordinal())};
		}
	}

	private void addCompat() {
		if (ModList.TREECAPITATOR.isLoaded()) {
			try {
				Class c = Class.forName("bspkrs.treecapitator.Strings");
				String[] fields = {"OAK", "SPRUCE", "BIRCH" , "JUNGLE"};
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("modID", "DyeTrees");
				NBTTagList treeList = new NBTTagList();
				for (int i = 0; i < fields.length; i++) {
					NBTTagCompound tree = new NBTTagCompound();
					Field f = c.getField(fields[i]);
					String sg = (String)f.get(null);
					tree.setString("treeName", sg);
					//tree.setString("logs", String.format("%d", Block.wood.blockID));
					tree.setString("leaves", String.format("%d; %d", DyeBlocks.LEAF.getID(), DyeBlocks.DECAY.getID()));
					tree.setInteger("maxHorLeafBreakDist", 5);
					tree.setBoolean("requireLeafDecayCheck", false);
					treeList.appendTag(tree);
				}
				nbt.setTag("trees", treeList);
				FMLInterModComms.sendMessage("TreeCapitator", "ThirdPartyModConfig", nbt);
				logger.log("Adding "+ModList.TREECAPITATOR.getDisplayName()+" support");
			}
			catch (Exception e) {
				logger.logError("Could not interface with "+ModList.TREECAPITATOR.getDisplayName()+"!");
				e.printStackTrace();
			}
		}

		if (ModList.ROTARYCRAFT.isLoaded()) {
			for (int i = 0; i < ReikaDyeHelper.dyes.length; i++) {
				ReikaDyeHelper dye = ReikaDyeHelper.dyes[i];
				BlockColorInterface.addGPRBlockColor(DyeBlocks.DECAY.getBlockID(), i, dye.color);
				BlockColorInterface.addGPRBlockColor(DyeBlocks.LEAF.getBlockID(), i, dye.color);
				BlockColorInterface.addGPRBlockColor(DyeBlocks.DYE.getBlockID(), i, dye.color);
				BlockColorInterface.addGPRBlockColor(DyeBlocks.SAPLING.getBlockID(), i, dye.color);
			}
		}
	}

	@Override
	@EventHandler
	public void postload(FMLPostInitializationEvent evt) {
		if (DyeOptions.COLORSLIMES.getState() && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			RenderSlime slimeRenderer = (RenderSlime)RenderManager.instance.entityRenderMap.get(EntitySlime.class);
			Field f;
			try {
				//f = slimeRenderer.getClass().getDeclaredField("scaleAmount");
				f = ReikaObfuscationHelper.getField("scaleAmount");
				//f.setAccessible(true);
				f.set(slimeRenderer, new ColorizableSlimeModel(0));
				logger.log("Overriding Slime Renderer Edge Model.");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (ModList.THAUMCRAFT.isLoaded()) {
			Object[] asp = new Object[]{
					Aspect.AURA, 10, Aspect.AIR, 3, Aspect.CROP, 4, Aspect.CRYSTAL, 1, Aspect.EARTH, 4, Aspect.TREE, 10,
					Aspect.EXCHANGE, 5, Aspect.HEAL, 10, Aspect.LIFE, 10, Aspect.LIGHT, 2, Aspect.MAGIC, 10, Aspect.ORDER, 10,
					Aspect.PLANT, 10, Aspect.SEED, 2, Aspect.VOID, 1
			};
			ReikaThaumHelper.addAspects(DyeBlocks.RAINBOW.getBlockInstance(), asp);
			ReikaThaumHelper.addAspects(DyeBlocks.RAINBOWSAPLING.getBlockInstance(), asp);

			Aspect[] flowers = {
					Aspect.DARKNESS,
					Aspect.EXCHANGE,
					Aspect.SLIME,
					Aspect.BEAST,
					Aspect.WATER,
					Aspect.TAINT,
					Aspect.MOTION,
					Aspect.SOUL,
					Aspect.ENTROPY,
					Aspect.FLESH,
					Aspect.SLIME,
					Aspect.GREED,
					Aspect.ICE,
					Aspect.MAGIC,
					Aspect.FIRE,
					Aspect.ORDER
			};
			for (int i = 0; i < 16; i++)
				ReikaThaumHelper.addAspects(DyeBlocks.FLOWER.getBlockID(), i, flowers[i], 1, Aspect.PLANT, 2);
		}
	}

	public static void loadClasses() {
		ReikaRegistryHelper.instantiateAndRegisterBlocks(instance, DyeBlocks.blockList, blocks);
		ReikaRegistryHelper.instantiateAndRegisterItems(instance, DyeItems.itemList, items);
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
		return DragonAPICore.getReikaForumPage();
	}

	@Override
	public String getWiki() {
		return null;
	}

	@Override
	public String getUpdateCheckURL() {
		return CommandableUpdateChecker.reikaURL;
	}

	@Override
	public ModLogger getModLogger() {
		return logger;
	}

	public static boolean isRainbowForest(BiomeGenBase b) {
		return b instanceof BiomeRainbowForest || b.biomeName.equals("Rainbow Forest");
	}

}
