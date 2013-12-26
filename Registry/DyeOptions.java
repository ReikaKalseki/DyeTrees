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
	MODLOGS("Allow mod logs in Dye Trees", true),
	WHITEMEAL("Allow bonemeal on White Trees", true);

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
		return (Integer)DyeTrees.config.getControl(this.ordinal());
	}

	public boolean isDummiedOut() {
		return type == null;
	}

}
