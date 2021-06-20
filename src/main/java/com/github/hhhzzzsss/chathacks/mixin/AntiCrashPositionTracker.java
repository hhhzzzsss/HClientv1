package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.github.hhhzzzsss.chathacks.ChatHacks;

import net.minecraft.nbt.PositionTracker;

@Mixin(PositionTracker.class)
public class AntiCrashPositionTracker {
	@Shadow
	private long pos;
	
	@Shadow
	private final long max;
	
	public AntiCrashPositionTracker(long max) {
		this.max = max;
	}
	
	@Overwrite
	public void add(long bits ) {
	      this.pos += bits / 8L;
	      if (this.pos > this.max) {
	         ChatHacks.addChatMessage("Crash prevented: Tried to read NBT tag that was too big; tried to allocate: " + this.pos + "bytes where max allowed: " + this.max);
	      }
	}
}