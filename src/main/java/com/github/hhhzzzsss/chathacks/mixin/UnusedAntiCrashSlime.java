package com.github.hhhzzzsss.chathacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.github.hhhzzzsss.chathacks.ChatHacks;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;

@Mixin(SlimeEntity.class)
public class UnusedAntiCrashSlime extends MobEntity implements Monster {
	/*@Shadow
	private static final TrackedData<Integer> SLIME_SIZE;
	
	static {
		SLIME_SIZE = DataTracker.registerData(SlimeEntity.class, TrackedDataHandlerRegistry.INTEGER);
	}*/
	
	public UnusedAntiCrashSlime(EntityType<? extends SlimeEntity> entityType, World world) {
		super(entityType, world);
	}
	
	/*@Overwrite
	public void setSize(int size, boolean heal) {
		this.dataTracker.set(SLIME_SIZE, size);
		this.refreshPosition();
		this.calculateDimensions();
		this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue((double)(size * size));
		this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)size));
		this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue((double)size);
		if (heal) {
			this.setHealth(this.getMaxHealth());
		}

		this.experiencePoints = size;
	}*/
}
