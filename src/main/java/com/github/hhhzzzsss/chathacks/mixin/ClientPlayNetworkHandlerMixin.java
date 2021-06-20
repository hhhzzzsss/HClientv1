package com.github.hhhzzzsss.chathacks.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Shadow
	private Random random;

	@Shadow
	private ClientWorld world;

	@Inject(method = "onParticle(Lnet/minecraft/network/packet/s2c/play/ParticleS2CPacket;)V", at = @At("HEAD"), cancellable = true)
	public void onOnParticle(ParticleS2CPacket packet, CallbackInfo ci) {
		if (packet.getCount() > 10000) {
			for (int i = 0; i < 10; ++i) {
				double g = this.random.nextGaussian() * (double) packet.getOffsetX();
				double h = this.random.nextGaussian() * (double) packet.getOffsetY();
				double j = this.random.nextGaussian() * (double) packet.getOffsetZ();
				double k = this.random.nextGaussian() * (double) packet.getSpeed();
				double l = this.random.nextGaussian() * (double) packet.getSpeed();
				double m = this.random.nextGaussian() * (double) packet.getSpeed();

				try {
					this.world.addParticle(packet.getParameters(), packet.isLongDistance(), packet.getX() + g,
							packet.getY() + h, packet.getZ() + j, k, l, m);
				} catch (Throwable var16) {}
			}
			ci.cancel();
		}
	}
	
	@Inject(method = "onBossBar(Lnet/minecraft/network/packet/s2c/play/BossBarS2CPacket;)V", at = @At("TAIL"))
	public void onOnBossBar(BossBarS2CPacket packet, CallbackInfo ci) {
		Color color = packet.getColor();
		if (color != null && color == Color.GREEN) {
			MinecraftClient.getInstance().player.sendChatMessage(packet.getName().getString());
		}
	}
}
