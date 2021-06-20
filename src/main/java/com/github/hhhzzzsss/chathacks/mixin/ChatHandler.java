package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.github.hhhzzzsss.chathacks.ChatHacks;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ChatHandler {
	
	@Shadow
	public final ClientPlayNetworkHandler networkHandler;
	
	public ChatHandler(ClientPlayNetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}
	
	@Overwrite
	public void sendChatMessage(String message) {
		ChatHacks.convertChatMessages(message, networkHandler);
	}
}
