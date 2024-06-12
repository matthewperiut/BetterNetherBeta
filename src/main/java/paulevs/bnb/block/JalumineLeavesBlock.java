package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.noise.PerlinNoise;

public class JalumineLeavesBlock extends BNBLeavesBlock {
	private static final PerlinNoise NOISE = new PerlinNoise();
	
	public JalumineLeavesBlock(Identifier id) {
		super(id, 8);
		setLightOpacity(4);
		setLightEmittance(1.0F);
		//setLuminance(JalumineLeavesBlock::lightEmission);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.FLOWERS_4);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		BlockPos pos = context.getBlockPos();
		return state.with(BNBBlockProperties.FLOWERS_4, getFlowers(pos.x, pos.y, pos.z));
	}
	
	private static int lightEmission(BlockState state) {
		int flowers = state.get(BNBBlockProperties.FLOWERS_4);
		return flowers == 0 ? 0 : flowers * 3 + 6;
	}
	
	private static int getFlowers(int x, int y, int z) {
		float noise = NOISE.get(x * 0.5, y * 0.5, z * 0.5);
		noise = MathHelper.clamp(noise * 2.0F - 1.0F, 0.0F, 1.0F);
		return Math.round(noise * 3);
	}
	
	static {
		NOISE.setSeed(756);
	}
}
