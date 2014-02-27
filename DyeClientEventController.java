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
import java.util.Random;

import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.ForgeSubscribe;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper;
import Reika.DyeTrees.Registry.DyeOptions;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DyeClientEventController {

	public static final DyeClientEventController instance = new DyeClientEventController();

	protected static final Random rand = new Random();

	private boolean editedSlimeModel = false;

	private DyeClientEventController() {

	}

	@ForgeSubscribe
	public void slimeColorizer(RenderLivingEvent.Pre ev) {
		if (DyeOptions.COLORSLIMES.getState()) {
			EntityLivingBase e = ev.entity;
			RendererLivingEntity r = ev.renderer;
			if (e.getClass() == EntitySlime.class && !editedSlimeModel) {
				try {
					Field f = ReikaObfuscationHelper.getField("mainModel");
					//f.setAccessible(true);
					ModelSlime mainModel = (ModelSlime)f.get(r);
					f.set(r, new ColorizableSlimeModel(16));
					editedSlimeModel = true;
					DyeTrees.instance.logger.log("Overriding Slime Renderer Core Model.");
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
