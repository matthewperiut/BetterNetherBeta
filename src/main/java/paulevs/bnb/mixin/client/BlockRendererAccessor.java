package paulevs.bnb.mixin.client;

import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockRenderer.class)
public interface BlockRendererAccessor {
	@Accessor("shadeTopFace") void bnb_setSmoothShading(boolean value);
}
