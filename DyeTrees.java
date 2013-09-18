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

import java.net.MalformedURLException;
import java.net.URL;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Exception.RegistrationException;
import Reika.DragonAPI.Instantiable.ControlledConfig;
import Reika.DragonAPI.Instantiable.ModLogger;
import Reika.DragonAPI.Libraries.ReikaRegistryHelper;
import Reika.DyeTrees.Registry.DyeBlocks;
import Reika.DyeTrees.Registry.DyeOptions;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "DyeTrees", name="Dye Trees", version="beta", certificateFingerprint = "@GET_FINGERPRINT@", dependencies="after:DragonAPI")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class DyeTrees extends DragonAPIMod {

	//@Instance
	public static DyeTrees instance = new DyeTrees();

	public static final ControlledConfig config = new ControlledConfig(instance, DyeOptions.optionList, DyeBlocks.blockList, null, null, 0);

	public static ModLogger logger;

	public static Block[] blocks = new Block[DyeBlocks.blockList.length];

	public static CreativeTabs dyeTreeTab = new DyeTreeTab(CreativeTabs.getNextID(), "Dye Trees");

	@Override
	@PreInit
	public void preload(FMLPreInitializationEvent evt) {
		config.loadSubfolderedConfigFile(evt);
		config.initProps(evt);
		logger = new ModLogger(instance, DyeOptions.LOGLOADING.getState(), DyeOptions.DEBUGMODE.getState(), false);
	}

	@Override
	@Init
	public void load(FMLInitializationEvent event) {
		this.loadClasses();
	}

	@Override
	@PostInit
	public void postload(FMLPostInitializationEvent evt) {

	}

	public static void loadClasses() {
		ReikaRegistryHelper.instantiateAndRegisterBlocks(instance, DyeBlocks.blockList, blocks, logger.shouldLog());
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
		try {
			return new URL("http://www.minecraftforum.net/topic/1969694-");
		}
		catch (MalformedURLException e) {
			throw new RegistrationException(instance, "The mod provided a malformed URL for its documentation site!");
		}
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

}
