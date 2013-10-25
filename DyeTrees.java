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

import java.net.URL;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.RetroGenController;
import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Instantiable.ControlledConfig;
import Reika.DragonAPI.Instantiable.ModLogger;
import Reika.DragonAPI.Libraries.ReikaRegistryHelper;
import Reika.DragonAPI.Libraries.World.ReikaBiomeHelper;
import Reika.DyeTrees.Registry.DyeBlocks;
import Reika.DyeTrees.Registry.DyeOptions;
import Reika.DyeTrees.World.BiomeRainbowForest;
import Reika.DyeTrees.World.ColorTreeGenerator;
import Reika.DyeTrees.World.RetroDyeTreeGen;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "DyeTrees", name="Dye Trees", version="beta", certificateFingerprint = "@GET_FINGERPRINT@", dependencies="after:DragonAPI")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class DyeTrees extends DragonAPIMod {

	//@Instance
	public static DyeTrees instance = new DyeTrees();

	public static final ControlledConfig config = new ControlledConfig(instance, DyeOptions.optionList, DyeBlocks.blockList, null, null, 0);

	public static ModLogger logger;

	public static Block[] blocks = new Block[DyeBlocks.blockList.length];

	public static CreativeTabs dyeTreeTab = new DyeTreeTab(CreativeTabs.getNextID(), "Dye Trees");

	protected static final Random rand = new Random();

	@Override
	@EventHandler
	public void preload(FMLPreInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(this);

		config.loadSubfolderedConfigFile(evt);
		config.initProps(evt);
		logger = new ModLogger(instance, DyeOptions.LOGLOADING.getState(), DyeOptions.DEBUGMODE.getState(), false);
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
			BiomeGenBase forest = new BiomeRainbowForest(ReikaBiomeHelper.getFirstEmptyBiomeIndex());
			GameRegistry.addBiome(forest);
			BiomeDictionary.registerBiomeType(forest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.HILLS);
		}
	}

	@Override
	@EventHandler
	public void postload(FMLPostInitializationEvent evt) {

	}

	@ForgeSubscribe
	public void bonemealEvent (BonemealEvent event)
	{
		if (!event.world.isRemote)  {
			if (event.ID == DyeBlocks.SAPLING.getBlockID()) {
				World world = event.world;
				int x = event.X;
				int y = event.Y;
				int z = event.Z;
				event.setResult(Event.Result.DENY);
			}
		}
	}

	@ForgeSubscribe
	public void colorSheep(LivingSpawnEvent ev) {
		World world = ev.world;
		int x = (int)Math.floor(ev.x);
		int y = (int)Math.floor(ev.y);
		int z = (int)Math.floor(ev.z);
		EntityLivingBase e = ev.entityLiving;
		BiomeGenBase b = world.getBiomeGenForCoords(x, z);
		if (b instanceof BiomeRainbowForest) {
			if (e instanceof EntitySheep) {
				EntitySheep es = (EntitySheep)e;
				es.setFleeceColor(rand.nextInt(16));
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
