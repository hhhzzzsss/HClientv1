package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.CompoundTag;

@Mixin(SkullBlockEntity.class)
public class SkullBlockEntityMixin {
	/*@Inject(at = {@At("HEAD")}, method = "fromTag(Lnet/minecraft/nbt/CompoundTag;)V")
	public void fromTag(CompoundTag tag, CallbackInfo ci) {
		 if (tag.contains("Owner", 10)) {
			 CompoundTag ownerTag = (CompoundTag) tag.get("Owner");
			 if (ownerTag.contains("Name", 8)) {
		         String nameTag = ownerTag.getString("Name");
		         if (nameTag.length() > 16) {
		        	 ownerTag.putString("Name", "MissingTexture");
		         }
		     }
		 }
	}*/

	@Inject(at = {@At("HEAD")}, method = "loadProperties(Lcom/mojang/authlib/GameProfile;)Lcom/mojang/authlib/GameProfile;", cancellable=true)
	private static void onLoadProperties(GameProfile profile, CallbackInfoReturnable<GameProfile> cir) {
		if (profile == null) return;
		if (profile.getName() != null) {
			if (profile.getName().length() > 16 || profile.getName().contains(" ") || profile.getName().contains("§")) {
				cir.setReturnValue(SkullBlockEntity.loadProperties(new GameProfile(null, "MissingTexture")));
				return;
			}
		}
	}
}
