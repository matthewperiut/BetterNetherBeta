package paulevs.bnb.mixin.common;

import net.minecraft.entity.projectile.FireballEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.world.BNBExplosion;

@Mixin(FireballEntity.class)
public class FireballEntityMixin {
	@Inject(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/level/Explosion;",
		shift = Shift.BEFORE
	))
	private void bnb_tick(CallbackInfo info) {
		BNBExplosion.setBlockSafe();
	}
}
