package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.entity.technical.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.achievement.BNBAchievements;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.item.BNBItems;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
	@Shadow public ItemStack stack;
	
	@Shadow private int health;
	
	public ItemEntityMixin(Level arg) {
		super(arg);
	}
	
	@Inject(method = "<init>(Lnet/minecraft/level/Level;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void bnb_makeFireproofInit(Level level, double x, double y, double z, ItemStack stack, CallbackInfo info) {
		if (stack != null && stack.getType() == BNBItems.OBSIDIAN_BOAT) {
			immuneToFire = true;
			health = Integer.MAX_VALUE;
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void bnb_makeFireproofReadData(CompoundTag tag, CallbackInfo info) {
		if (stack != null && stack.getType() == BNBItems.OBSIDIAN_BOAT) {
			immuneToFire = true;
			health = Integer.MAX_VALUE;
		}
	}
	
	@Inject(method = "onPlayerCollision", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;playSound(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V",
		shift = Shift.BEFORE
	))
	private void bnb_onCollision(PlayerEntity player, CallbackInfo info) {
		if (!(stack.getType() instanceof BlockItem item)) return;
		Block block = item.getBlock();
		
		if (block == BNBBlocks.FALURIAN_LOG) player.incrementStat(BNBAchievements.COLLECT_FALURIAN_LOG);
		if (block == BNBBlocks.PIROZEN_LOG) player.incrementStat(BNBAchievements.COLLECT_PIROZEN_LOG);
		if (block == BNBBlocks.POISON_LOG) player.incrementStat(BNBAchievements.COLLECT_POISON_LOG);
		
		int summ = BNBAchievements.readStat(BNBAchievements.COLLECT_FALURIAN_LOG) > 0 ? 1 : 0;
		summ += BNBAchievements.readStat(BNBAchievements.COLLECT_PIROZEN_LOG) > 0 ? 1 : 0;
		summ += BNBAchievements.readStat(BNBAchievements.COLLECT_POISON_LOG) > 0 ? 1 : 0;
		
		if (summ == 3) {
			player.incrementStat(BNBAchievements.RGB);
		}
	}
	
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;getMaterial(III)Lnet/minecraft/block/material/Material;"
	))
	private Material bnb_disableLavaVelocity(Level level, int x, int y, int z, Operation<Material> original) {
		Material material = original.call(level, x, y, z);
		if (immuneToFire && material == Material.LAVA) {
			float dy = (float) ((y + 0.6F) - this.y);
			if (level.getBlockState(x, y + 1, z).getMaterial() == Material.LAVA) {
				dy = 1.0F;
			}
			if (dy > 0) velocityY = dy * 0.5F;
			velocityX *= 0.9;
			velocityZ *= 0.9;
			return Material.WATER;
		}
		return material;
	}
	
	/*@ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.2F, ordinal = 0))
	private float bnb_disableLavaVelocity(float constant) {
		return 0.0F;
	}*/
}
