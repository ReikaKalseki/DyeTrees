/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DyeTrees.Registry;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.Configuration;
import Reika.DragonAPI.Exception.RegistrationException;
import Reika.DragonAPI.Interfaces.ConfigList;
import Reika.DyeTrees.DyeTrees;

public enum DyeOptions implements ConfigList {

	LOGLOADING("Console Loading Info", true),
	DEBUGMODE("Debug Mode", false),
	BIOME("Rainbow Forest Biome", true),
	NORMAL("Generate Dye Trees in Normal Biomes", true),
	RETROGEN("Retrogen Trees", false),
	BIOMEID("Rainbow Forest Biome ID", 48),
	COLORSLIMES("Rainbow Slimes", true),
	OVERWORLD("Overworld Rainbow Forest", true),
	BLOCKPARTICLES("Dye Block Particles", true),
	ETHEREAL("Generate Anti-Taint plants in Rainbow Forest", true),
	DENSITY("Dye Tree Density in Normal Biomes", 2),
	GENRAINBOW("Generate Rainbow Trees", true),
	DYEFRAC("Vanilla Dye Drop Percentage", 100);

	private String label;
	private boolean defaultState;
	private int defaultValue;
	private float defaultFloat;
	private Class type;

	public static final DyeOptions[] optionList = DyeOptions.values();

	private DyeOptions(String l, boolean d) {
		label = l;
		defaultState = d;
		type = boolean.class;
	}

	private DyeOptions(String l, int d) {
		label = l;
		defaultValue = d;
		type = int.class;
	}

	public boolean isBoolean() {
		return type == boolean.class;
	}

	public boolean isNumeric() {
		return type == int.class;
	}

	public boolean isDecimal() {
		return type == float.class;
	}

	public float setDecimal(Configuration config) {
		if (!this.isDecimal())
			throw new RegistrationException(DyeTrees.instance, "Config Property \""+this.getLabel()+"\" is not decimal!");
		return (float)config.get("Control Setup", this.getLabel(), defaultFloat).getDouble(defaultFloat);
	}

	public float getFloat() {
		return (Float)DyeTrees.config.getControl(this.ordinal());
	}

	public Class getPropertyType() {
		return type;
	}

	public int setValue(Configuration config) {
		if (!this.isNumeric())
			throw new RegistrationException(DyeTrees.instance, "Config Property \""+this.getLabel()+"\" is not numerical!");
		return config.get("Control Setup", this.getLabel(), defaultValue).getInt();
	}

	public String getLabel() {
		return label;
	}

	public boolean setState(Configuration config) {
		if (!this.isBoolean())
			throw new RegistrationException(DyeTrees.instance, "Config Property \""+this.getLabel()+"\" is not boolean!");
		return config.get("Control Setup", this.getLabel(), defaultState).getBoolean(defaultState);
	}

	public boolean getState() {
		return (Boolean)DyeTrees.config.getControl(this.ordinal());
	}

	public int getValue() {
		int num = (Integer)DyeTrees.config.getControl(this.ordinal());
		if (this == DYEFRAC) {
			MathHelper.clamp_int(num, 0, 100);
		}
		return num;
	}

	public boolean isDummiedOut() {
		return type == null;
	}

	public static boolean doesVanillaDyeDrop() {
		return DYEFRAC.getValue() > 0;
	}

	public static boolean doesTreeDyeDrop() {
		return DYEFRAC.getValue() < 100;
	}

	public static boolean isVanillaDyeMoreCommon() {
		return DYEFRAC.getValue() > 50;
	}

}
