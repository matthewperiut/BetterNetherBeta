package paulevs.bnb.mixin.common;

import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.NetherDimension;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.world.biome.BNBBiomes;

import java.util.Collection;

@Mixin(NetherDimension.class)
public class NetherDimensionMixin extends Dimension implements StationDimension {
	@Override
	public int getHeight() {
		return 256;
	}
	
	@Inject(method = "pregenLight", at = @At("HEAD"), cancellable = true)
	private void bnb_pregenLight(CallbackInfo info) {
		info.cancel();
		for (byte i = 0; i < 16; i++) {
			float delta = i / 15F;
			this.lightCurve[i] = MathHelper.lerp(delta, 0.3F, 1.0F);
		}
	}
	
	@Override
	public Collection<Biome> getBiomes() {
		return BNBBiomes.BIOMES;
	}
}
