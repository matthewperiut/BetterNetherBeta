package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.rendering.LavaRenderer;

@Mixin(BlockRenderer.class)
public class BlockRendererMixin {
	@Unique private static final FloatList BNB_UVS = new FloatArrayList(8);
	@Unique private boolean bnb_isLava;
	
	@Inject(method = "renderFluid", at = @At("HEAD"))
	private void bnb_renderFluidStart(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		LavaRenderer.POS.set(x, y, z);
	}
	
	@Inject(method = "renderFluid", at = @At("HEAD"))
	private void bnb_lavaCheck(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> infoReturnable) {
		bnb_isLava = block == Block.STILL_LAVA || block == Block.FLOWING_LAVA;
	}
	
	@Inject(method = "renderFluid", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
		ordinal = 3,
		shift = Shift.AFTER
	))
	private void bnb_fixLavaTop(
		Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> infoReturnable,
		@Local(index = 24) float f11, @Local(index = 25) float f12, @Local(index = 26) float f13, @Local(index = 27) float f14
	) {
		if (!bnb_isLava) return;
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.vertex(x + 1, y + f14, z, BNB_UVS.getFloat(0), BNB_UVS.getFloat(1));
		tessellator.vertex(x + 1, y + f13, z + 1, BNB_UVS.getFloat(2), BNB_UVS.getFloat(3));
		tessellator.vertex(x, y + f12, z + 1, BNB_UVS.getFloat(4), BNB_UVS.getFloat(5));
		tessellator.vertex(x, y + f11, z, BNB_UVS.getFloat(6), BNB_UVS.getFloat(7));
		BNB_UVS.clear();
	}
	
	@Inject(method = "renderFluid", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
		ordinal = 7,
		shift = Shift.AFTER
	))
	private void bnb_fixLavaSides(
		Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> infoReturnable,
		@Local(index = 37) float f2, @Local(index = 39) float f18,
		@Local(index = 35) float f19, @Local(index = 36) float f3,
		@Local(index = 38) float f, @Local(index = 40) float f17
	) {
		if (!bnb_isLava) return;
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.vertex(f2, y, f, BNB_UVS.getFloat(0), BNB_UVS.getFloat(1));
		tessellator.vertex(f18, y, f17, BNB_UVS.getFloat(2), BNB_UVS.getFloat(3));
		tessellator.vertex(f18, y + f3, f17, BNB_UVS.getFloat(4), BNB_UVS.getFloat(5));
		tessellator.vertex(f2, y + f19, f, BNB_UVS.getFloat(6), BNB_UVS.getFloat(7));
		BNB_UVS.clear();
	}
	
	@WrapOperation(method = "renderFluid", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V")
	)
	private void bnb_captureUV(Tessellator instance, double x, double y, double z, double u, double v, Operation<Void> original) {
		original.call(instance, x, y, z, u, v);
		if (!bnb_isLava) return;
		BNB_UVS.add((float) u);
		BNB_UVS.add((float) v);
	}
	
	@WrapOperation(method = "renderFluid", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/block/BlockRenderer;renderBottomFace(Lnet/minecraft/block/Block;DDDI)V")
	)
	private void bnb_renderLavaBottom(BlockRenderer renderer, Block block, double x, double y, double z, int textureID, Operation<Void> original) {
		original.call(renderer, block, x, y, z, textureID);
		if (!bnb_isLava) return;
		renderer.renderTopFace(block, x, y - 1, z, textureID);
	}
}
