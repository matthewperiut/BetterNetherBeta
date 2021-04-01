package paulevs.bnb.listeners;

import java.util.Random;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationloader.api.common.event.level.gen.ChunkPopulator;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.world.biome.NetherBiome;

public class ChunkListener implements ChunkPopulator {
	@Override
	public void populate(Level level, LevelSource levelSource, Biome biome, int startX, int startZ, Random random) {
		if (level.dimension instanceof Nether) {
			Chunk chunk = level.getChunk(startX, startZ);
			Biome[] biomes = level.getBiomeSource().getBiomes(startX, startZ, 16, 16);
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Biome bio = biomes[x << 4 | z];
					if (bio instanceof NetherBiome) {
						BlockState top = ((NetherBiome) bio).getTopBlock();
						boolean fire = ((NetherBiome) bio).hasFire();
						for (int y = 0; y < 127; y++) {
							int tile = chunk.getTileId(x, y, z);
							if (tile == BlockBase.NETHERRACK.id) {
								tile = chunk.getTileId(x, y + 1, z);
								if (tile == 0 || BlockBase.BY_ID[tile] == null || !BlockBase.BY_ID[tile].isFullOpaque()) {
									top.setBlock(chunk, x, y, z);
								}
							}
							else if (!fire && tile == BlockBase.FIRE.id) {
								chunk.setTileWithMetadata(x, y, z, 0, 0);
							}
						}
					}
				}
			}
			
			if (biome instanceof NetherBiome) {
				NetherBiome bio = (NetherBiome) biome;
				int countTrees = bio.getMaxTreeCount();
				int countPlants = bio.getMaxPlantCount();
				int countCeilPlants = bio.getMaxCeilPlantCount();
				if (countTrees > 0 || countPlants > 0 || countCeilPlants > 0) {
					Structure structure;
					
					for (int sect = 0; sect < 8; sect++) {
						int sy = sect << 4;
						int minY = sect == 0 ? 1 : 0;
						int maxY = sect == 7 ? 15 : 16;
						
						int count = random.nextInt(countTrees);
						for (int i = 0; i < count; i++) {
							int px = random.nextInt(16);
							int pz = random.nextInt(16);
							for (int y = minY; y < maxY; y++) {
								int py = sy | y;
								int tile = chunk.getTileId(px, py, pz);
								if (BlockUtil.isTerrain(tile)) {
									tile = chunk.getTileId(px, py + 1, pz);
									if (BlockUtil.isNonSolidNoLava(tile)) {
										structure = bio.getTree(random);
										structure.generate(level, random, px | startX, py + 1, pz | startZ);
										break;
									}
								}
							}
						}
						
						count = random.nextInt(countPlants);
						for (int i = 0; i < count; i++) {
							int px = random.nextInt(16);
							int pz = random.nextInt(16);
							for (int y = minY; y < maxY; y++) {
								int py = sy | y;
								int tile = chunk.getTileId(px, py, pz);
								if (BlockUtil.isTerrain(tile)) {
									tile = chunk.getTileId(px, py + 1, pz);
									if (BlockUtil.isNonSolidNoLava(tile)) {
										structure = bio.getPlant(random);
										structure.generate(level, random, px | startX, py + 1, pz | startZ);
										break;
									}
								}
							}
						}
						
						count = random.nextInt(countCeilPlants);
						for (int i = 0; i < count; i++) {
							int px = random.nextInt(16);
							int pz = random.nextInt(16);
							for (int y = minY; y < maxY; y++) {
								int py = sy | y;
								int tile = chunk.getTileId(px, py, pz);
								if (BlockBase.BY_ID[tile] != null && BlockBase.BY_ID[tile].isFullCube()) {
									tile = chunk.getTileId(px, py - 1, pz);
									if (BlockUtil.isNonSolidNoLava(tile)) {
										structure = bio.getCeilPlant(random);
										structure.generate(level, random, px | startX, py - 1, pz | startZ);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
