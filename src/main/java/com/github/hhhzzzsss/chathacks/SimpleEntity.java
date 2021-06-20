package com.github.hhhzzzsss.chathacks;

import java.util.UUID;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.registry.Registry;

public class SimpleEntity extends Entity{
	public int type;
	public UUID uuid;
	public double x;
	public double y;
	public double z;
	
	public SimpleEntity(int type, int entityID, UUID uuid, double x, double y, double z) {
		super((EntityType)Registry.ENTITY_TYPE.get(type), MinecraftClient.getInstance().player.world);
		this.type = type;
		this.setEntityId(entityID);
		this.uuid = uuid;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	protected void initDataTracker() {
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return null;
	}
}