package paulevs.bnb.mixin.common;

import net.minecraft.entity.living.monster.GhastEntity;
import net.minecraft.entity.living.monster.ZombieEntity;
import net.minecraft.entity.living.monster.ZombiePigmanEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombiePigmanEntity.class)
public abstract class ZombiePigmanEntityMixin extends ZombieEntity {
	public ZombiePigmanEntityMixin(Level arg) {
		super(arg);
	}
	
	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private void bnb_canSpawn(CallbackInfoReturnable<Boolean> info) {
		Box bounds = Box.createAndCache(x - 64, y - 64, z - 64, x + 64, y + 64, z + 64);
		if (level.getEntities(GhastEntity.class, bounds).size() > 8) {
			info.setReturnValue(false);
		}
	}
}
