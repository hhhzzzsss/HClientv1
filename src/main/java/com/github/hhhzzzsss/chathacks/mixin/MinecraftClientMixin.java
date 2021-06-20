package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.hhhzzzsss.chathacks.ChatHacks;
import com.github.hhhzzzsss.chathacks.Chatter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	private int itemUseCooldown;
	
	@Shadow
	public ClientWorld world;
	
	@Shadow
	public ClientPlayerEntity player;
	
	@Inject(at = {@At("HEAD")}, method = {"getSession()Lnet/minecraft/client/util/Session;"}, cancellable = true)
	private void onGetSession(CallbackInfoReturnable<Session> cir)
	{
		if (ChatHacks.username != null) cir.setReturnValue(ChatHacks.session);
	}
	
	@Inject(method = "doItemUse", at = @At("RETURN"))
	public void doItemUse(CallbackInfo info) {
		itemUseCooldown = ChatHacks.clickTime;
		ChatHacks.rightClick();
	}
	
	@Inject(method = "doAttack", at = @At("RETURN"))
	private void doAttack(CallbackInfo info) {
		ChatHacks.swingArm();
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info) {
		if (world != null) {
			ChatHacks.tick();
			Chatter.update(player);
		}
	}
}
