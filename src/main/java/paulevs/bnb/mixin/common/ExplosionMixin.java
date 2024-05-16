package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.level.Explosion;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.world.BNBExplosion;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@Unique boolean bnb_isBlockSafe;
	@Inject(method = "phase2", at = @At("HEAD"))
	private void bnb_checkBlockRemove(boolean par1, CallbackInfo info) {
		bnb_isBlockSafe = BNBExplosion.isBlockSafe();
	}
	
	@WrapOperation(method = "phase2", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/Level;getBlockID(III)I"))
	private int bnb_disableBlockRemove(Level level, int x, int y, int z, Operation<Integer> original) {
		return bnb_isBlockSafe ? -1 : original.call(level, x, y, z);
	}
}
