package com.github.hhhzzzsss.chathacks.mixin;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.hhhzzzsss.chathacks.ChatHacks;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
	
	@Shadow
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Shadow
	public static final Identifier MISSING_IDENTIFIER = new Identifier("");

	@Shadow
	private final ResourceManager resourceContainer;
	
	private TextureManagerMixin(ResourceManager resourceContainer) {
		this.resourceContainer = resourceContainer;
	}
	
	@Inject(at = {@At("HEAD")}, method = "method_24303(Lnet/minecraft/util/Identifier;Lnet/minecraft/client/texture/AbstractTexture;)Lnet/minecraft/client/texture/AbstractTexture;", cancellable=true)
	private void onMethod_24303(Identifier identifier, AbstractTexture abstractTexture, CallbackInfoReturnable<AbstractTexture> cir) {
		try {
	         abstractTexture.load(this.resourceContainer);
	         cir.setReturnValue(abstractTexture);
	      } catch (IOException var6) {
	         if (identifier != MISSING_IDENTIFIER) {
	            LOGGER.warn("Failed to load texture: {}", identifier, var6);
	         }

	         cir.setReturnValue(MissingSprite.getMissingSpriteTexture());
	      } catch (Throwable var7) {
	    	 ChatHacks.addChatMessage("Suppressed head texture crash");
	    	 cir.setReturnValue(MissingSprite.getMissingSpriteTexture());
	      }
	}
}
