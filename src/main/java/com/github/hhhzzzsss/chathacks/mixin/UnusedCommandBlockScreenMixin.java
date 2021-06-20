package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import net.minecraft.client.gui.screen.ingame.CommandBlockScreen;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.CommandBlockExecutor;

@Mixin(CommandBlockScreen.class)
public abstract class UnusedCommandBlockScreenMixin extends AbstractCommandBlockScreen {

	@Shadow
	private CommandBlockBlockEntity.Type mode;
	
	@Shadow
	private boolean conditional;
	
	@Shadow
	private boolean autoActivate;
	
	@Inject(method = "syncSettingsToServer", at = @At("HEAD"), cancellable=true)
	protected void onSyncSettingsToServer(CommandBlockExecutor commandExecutor, CallbackInfo ci) {
		MinecraftClient.getInstance().getNetworkHandler().sendPacket(new UpdateCommandBlockC2SPacket(new BlockPos(commandExecutor.getPos()), this.consoleCommandTextField.getText().replaceAll("&", "§"), this.mode, commandExecutor.isTrackingOutput(), this.conditional, this.autoActivate));
		ci.cancel();
	}
}
