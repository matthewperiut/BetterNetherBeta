package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;

public class ShardsBlock extends TemplateBlock {
	public ShardsBlock(Identifier id) {
		this(id, Material.GLASS);
		this.setSounds(GLASS_SOUNDS);
		setLightOpacity(0);
	}
	
	public ShardsBlock(Identifier id, Material material) {
		super(id, material);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.DIRECTION);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Direction face = context.getSide().getOpposite();
		return getDefaultState().with(BNBBlockProperties.DIRECTION, face);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		super.onAdjacentBlockUpdate(level, x, y, z, id);
		tick(level, x, y, z);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		super.onScheduledTick(level, x, y, z, rand);
		tick(level, x, y, z);
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Box getOutlineShape(Level level, int x, int y, int z) {
		Direction direction = level.getBlockState(x, y, z).get(BNBBlockProperties.DIRECTION);
		Axis axis = direction.getAxis();
		setBoundingBox(
			axis == Axis.X ? (direction.getOffsetX() < 0 ? 0.0F : 0.125F) : 0.125F,
			axis == Axis.Y ? (direction.getOffsetY() < 0 ? 0.0F : 0.125F) : 0.125F,
			axis == Axis.Z ? (direction.getOffsetZ() < 0 ? 0.0F : 0.125F) : 0.125F,
			axis == Axis.X ? (direction.getOffsetX() < 0 ? 0.875F : 1.0F) : 0.875F,
			axis == Axis.Y ? (direction.getOffsetY() < 0 ? 0.875F : 1.0F) : 0.875F,
			axis == Axis.Z ? (direction.getOffsetZ() < 0 ? 0.875F : 1.0F) : 0.875F
		);
		return super.getOutlineShape(level, x, y, z);
	}
	
	protected void tick(Level level, int x, int y, int z) {
		if (!this.canStay(level, x, y, z)) {
			this.drop(level, x, y, z, 0);
			level.setBlockState(x, y, z, States.AIR.get());
		}
	}
	
	private boolean canStay(Level level, int x, int y, int z) {
		Direction direction = level.getBlockState(x, y, z).get(BNBBlockProperties.DIRECTION);
		return isSupport(level, x, y, z, direction);
	}
	
	public boolean isSupport(Level level, int x, int y, int z, Direction direction) {
		x += direction.getOffsetX();
		y += direction.getOffsetY();
		z += direction.getOffsetZ();
		
		BlockState state = level.getBlockState(x, y, z);
		if (state.isAir()) return false;
		
		Block block = state.getBlock();
		if (block.isFullCube()) return true;
		
		Box shape = block.getCollisionShape(level, x, y, z);
		if (shape == null) {
			shape = Box.createAndCache(block.minX, block.minY, block.minZ, block.maxX, block.maxY, block.maxZ);
		}
		else {
			shape.move(-x, -y, -z);
		}
		
		switch (direction) {
			case DOWN -> { if (shape.maxY < 1) return false; }
			case UP -> { if (shape.minY > 0) return false; }
			case EAST -> { if (shape.maxZ < 1) return false; }
			case WEST -> { if (shape.minZ > 0) return false; }
			case NORTH -> { if (shape.maxX < 1) return false; }
			case SOUTH -> { if (shape.minX > 0) return false; }
		}
		
		float minA = 0;
		float minB = 0;
		float maxA = 1;
		float maxB = 1;
		
		switch (direction) {
			case DOWN, UP -> {
				minA = (float) shape.minX;
				minB = (float) shape.minZ;
				maxA = (float) shape.maxX;
				maxB = (float) shape.maxZ;
			}
			case EAST, WEST -> {
				minA = (float) shape.minX;
				minB = (float) shape.minY;
				maxA = (float) shape.maxX;
				maxB = (float) shape.maxY;
			}
			case NORTH, SOUTH -> {
				minA = (float) shape.minZ;
				minB = (float) shape.minY;
				maxA = (float) shape.maxZ;
				maxB = (float) shape.maxY;
			}
		}
		
		return minA <= 0.375F && minB <= 0.375F && maxA >= 0.625F && maxB >= 0.625F;
	}
}
