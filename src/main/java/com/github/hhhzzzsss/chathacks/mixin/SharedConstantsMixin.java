package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.SharedConstants;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {
	@Overwrite
	public static String stripInvalidChars(String s) {
		return s;
	}
}
