package paulevs.bnb.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer;
import net.modificationstation.stationapi.mixin.arsenic.client.BlockRenderManagerAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.rendering.BNBConnectedTextures;

@Mixin(ArsenicBlockRenderer.class)
public class ArsenicBlockRenderMixin {
	@Shadow @Final private BlockRenderManagerAccessor blockRendererAccessor;
	@Shadow @Final private BlockRenderer blockRenderer;
	
	@Inject(method = "renderWorld", at = @At("TAIL"))
	public void bnb_renderCTM(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> originalInfo, CallbackInfo info) {
		BNBConnectedTextures.renderCTM(block, x, y, z, blockRendererAccessor.getBlockView(), blockRenderer);
	}
}
