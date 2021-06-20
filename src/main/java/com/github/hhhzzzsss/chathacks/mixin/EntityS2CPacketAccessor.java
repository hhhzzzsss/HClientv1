package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.s2c.play.EntityS2CPacket;

@Mixin(EntityS2CPacket.class)
public interface EntityS2CPacketAccessor {
	@Accessor
	int getId();
}
