package paulevs.bnb.mixin.server;

import net.minecraft.entity.living.player.ServerPlayer;
import net.minecraft.server.network.TrackedEntity;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.packet.BNBWeatherPacket;
import paulevs.bnb.weather.BNBWeatherManager;

@Mixin(TrackedEntity.class)
public class TrackedEntityMixin {
	@Inject(method = "sync", at = @At("HEAD"))
	private void pumpkin_moon_sync(ServerPlayer player, CallbackInfo info) {
		PacketHelper.sendTo(player, new BNBWeatherPacket(BNBWeatherManager.getCurrentWeather()));
	}
}
