package paulevs.bnb.mixin.client;

import net.minecraft.client.sound.SoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SoundMap.class)
@SuppressWarnings("rawtypes")
public interface SoundMapAccessor {
	@Accessor("soundList")
	List bnb_getSoundList();
}
