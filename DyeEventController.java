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

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DyeTrees.Registry.DyeBlocks;
import Reika.DyeTrees.Registry.DyeOptions;
import Reika.DyeTrees.World.BiomeRainbowForest;

public class DyeEventController {

	public static final DyeEventController instance = new DyeEventController();

	protected static final Random rand = new Random();

	private DyeEventController() {

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
		if (world.isRemote)
			return;
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

	/** Not functional due to BlockLeaves being the <i>only</i> block not to fire the event */
	@ForgeSubscribe
	public void addLeafColors(HarvestDropsEvent evt) {
		World world = evt.world;
		if (DyeOptions.BIOME.getState() || DyeOptions.NORMAL.getState())
			return;
		int x = evt.x;
		int y = evt.y;
		int z = evt.z;
		ArrayList<ItemStack> li = evt.drops;
		int id = world.getBlockId(x, y, z);
		if (id == Block.leaves.blockID) {
			int meta = rand.nextInt(16);
			ItemStack sapling = new ItemStack(DyeBlocks.SAPLING.getBlockID(), 1, meta);
			if (ReikaRandomHelper.doWithChance(0.04)) { //4% chance per leaf block
				li.add(sapling);
			}
		}
	}

	@ForgeSubscribe
	public void controlSlimes(CheckSpawn ev) {
		World world = ev.world;
		if (world.isRemote)
			return;
		int x = (int)Math.floor(ev.x);
		int y = (int)Math.floor(ev.y);
		int z = (int)Math.floor(ev.z);
		EntityLivingBase e = ev.entityLiving;
		BiomeGenBase b = world.getBiomeGenForCoords(x, z);
		if (b instanceof BiomeRainbowForest) {
			if (e instanceof EntitySlime) {
				EntitySlime es = (EntitySlime)e;
				ev.setResult(es.getSlimeSize() > 1 ? Result.DENY : Result.DEFAULT);
			}
		}
	}

	@ForgeSubscribe
	public void dyeSlimes(LivingDropsEvent ev) {
		if (DyeOptions.COLORSLIMES.getState()) {
			EntityLivingBase e = ev.entityLiving;
			ArrayList<EntityItem> drops = ev.drops;
			if (e instanceof EntitySlime) {
				int x = (int)Math.floor(e.posX);
				int y = (int)Math.floor(e.posY);
				int z = (int)Math.floor(e.posZ);
				World world = e.worldObj;
				BiomeGenBase b = world.getBiomeGenForCoords(x, z);
				if (b instanceof BiomeRainbowForest) {
					int dmg = e.entityId%16;
					int size = 1+rand.nextInt(3);
					ItemStack dye = new ItemStack(Item.dyePowder.itemID, size, dmg);
					ReikaItemHelper.dropItem(world, e.posX, e.posY, e.posZ, dye);
				}
			}
		}
	}

}
