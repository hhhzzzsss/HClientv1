package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.hhhzzzsss.chathacks.ChatHacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.thread.ThreadExecutor;

@Mixin(ClientPlayNetworkHandler.class)
public class SpamChecker {
	@Shadow
	private MinecraftClient client;
	
	@Inject(method = "onGameMessage", at = @At("HEAD"), cancellable=true)
	public void onOnGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
		if (ChatHacks.isSpam(packet.getMessage())) {
			ci.cancel();
		}
	}
}
