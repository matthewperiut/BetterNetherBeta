package paulevs.bnb.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StairsBlock.class)
public interface StairsBlockAccessor {
	@Accessor("template")
	Block bnb_getTemplate();
}
