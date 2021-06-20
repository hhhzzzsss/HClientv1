package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.hhhzzzsss.chathacks.Chatter;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ChatterUsernameUpdater {
	@Inject(method = "onPlayerList", at = @At("TAIL"))
	public void onPlayerList(PlayerListS2CPacket packet, CallbackInfo info) {
		Chatter.updateUsernameList();
	}
}
