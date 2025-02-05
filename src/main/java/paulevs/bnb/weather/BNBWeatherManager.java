package paulevs.bnb.weather;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.mixin.common.EntityAccessor;
import paulevs.bnb.packet.BNBWeatherPacket;
import paulevs.vbe.utils.CreativeUtil;

import java.util.List;
import java.util.Random;

public class BNBWeatherManager {
	private static final WeatherType[] WEATHER_SEQUENCE = new WeatherType[16];
	private static final WeatherType[] WEATHER_TYPES = WeatherType.values();
	private static final LongSet CHUNKS = new LongOpenHashSet(4096);
	private static final Random RANDOM = new Random();
	
	private static WeatherType currentWeather = WeatherType.CLEAR;
	private static int weatherLength = 1200;
	private static int weatherIndex;
	
	public static void tick(Level level) {
		updateWeather();
		processBlocksAndEntities(level);
	}
	
	private static void updateWeather() {
		if (weatherLength-- > 0) return;
		currentWeather = WEATHER_SEQUENCE[weatherIndex];
		if (currentWeather == null) {
			currentWeather = WEATHER_TYPES[RANDOM.nextInt(WEATHER_TYPES.length)];
			fillSequence();
		}
		if (++weatherIndex == WEATHER_SEQUENCE.length) {
			fillSequence();
		}
		weatherLength = currentWeather.getTime(RANDOM);
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BNB.LOGGER.info("Weather " + currentWeather + " for " + weatherLength + " ticks");
		}
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			PacketHelper.send(new BNBWeatherPacket(currentWeather));
		}
	}
	
	private static WeatherType getWeather(WeatherType prev1, WeatherType prev2) {
		if (prev1 == WeatherType.LAVA_RAIN) return WeatherType.FOG;
		if (prev1 == WeatherType.FOG) {
			return prev2 == null || prev2 == WeatherType.LAVA_RAIN ? WeatherType.CLEAR : WeatherType.LAVA_RAIN;
		}
		int index = RANDOM.nextInt(WEATHER_TYPES.length);
		WeatherType weather = WEATHER_TYPES[index];
		if (weather == prev1) weather = WEATHER_TYPES[(index + 1) % WEATHER_TYPES.length];
		return weather;
	}
	
	private static void fillSequence() {
		WeatherType prev2 = WEATHER_SEQUENCE[WEATHER_SEQUENCE.length - 2];
		WeatherType prev1 = currentWeather;
		for (int i = 0; i < WEATHER_SEQUENCE.length; i++) {
			WEATHER_SEQUENCE[i] = getWeather(prev1, prev2);
			prev2 = prev1;
			prev1 = WEATHER_SEQUENCE[i];
		}
		weatherIndex = 0;
	}
	
	private static void processBlocksAndEntities(Level level) {
		if (currentWeather != WeatherType.LAVA_RAIN) return;
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			updateOnClient(level);
		}
		else {
			updateOnServer(level);
		}
		
		for (long pos : CHUNKS) {
			int x = (int) (pos >> 32);
			int z = (int) (pos & 0xFFFFFFFFL);
			Chunk chunk = level.getChunkFromCache(x, z);
			
			for (int i = 0; i < 256; i++) {
				if (RANDOM.nextInt(2000) > 0) continue;
				x = i & 15;
				z = i >> 4;
				int y = getWeatherBottom(level, chunk, x, z);
				BlockState state = chunk.getBlockState(x, y, z);
				if (state.getMaterial().isBurnable() && chunk.getBlockState(x, y + 1, z).isAir()) {
					chunk.setBlockState(x, y + 1, z, Block.FIRE.getDefaultState());
				}
			}
			
			for (Object list : chunk.entities) {
				for (Object obj : (List<?>) list) {
					EntityAccessor accessor = (EntityAccessor) obj;
					if (accessor.bnb_immuneToFire()) continue;
					Entity entity = (Entity) obj;
					if (entity.fire > 0) continue;
					if (obj instanceof PlayerEntity player) {
						if (CreativeUtil.isCreative(player)) continue;
					}
					x = MCMath.floor(entity.x) & 15;
					z = MCMath.floor(entity.z) & 15;
					int y = getWeatherBottom(level, chunk, x, z);
					if (y > entity.y + entity.height) continue;
					accessor.bnb_setOnFire();
				}
			}
		}
		
		CHUNKS.clear();
	}
	
	@Environment(EnvType.CLIENT)
	private static void updateOnClient(Level level) {
		PlayerEntity player = BNBClient.getMinecraft().player;
		for (int x = -7; x <= 7; x++) {
			int px = player.chunkX + x;
			for (int z = -7; z <= 7; z++) {
				int pz = player.chunkZ + z;
				if (level.getChunkFromCache(px, pz) != null) {
					CHUNKS.add(((long) px & 0xFFFFFFFFL) << 32L | (long) pz & 0xFFFFFFFFL);
				}
			}
		}
	}
	
	@Environment(EnvType.SERVER)
	private static void updateOnServer(Level level) {
		for (Object obj : level.players) {
			PlayerEntity player = (PlayerEntity) obj;
			for (int x = -7; x <= 7; x++) {
				int px = player.chunkX + x;
				for (int z = -7; z <= 7; z++) {
					int pz = player.chunkZ + z;
					if (level.getChunkFromCache(px, pz) != null) {
						CHUNKS.add(((long) px & 0xFFFFFFFFL) << 32L | (long) pz & 0xFFFFFFFFL);
					}
				}
			}
		}
	}
	
	public static WeatherType getCurrentWeather() {
		return currentWeather;
	}
	
	@Environment(EnvType.CLIENT)
	public static void setWeather(WeatherType weather) {
		currentWeather = weather;
	}
	
	public static void setWeather(WeatherType weather, int length) {
		if (currentWeather != weather || WEATHER_SEQUENCE[0] == null) fillSequence();
		currentWeather = weather;
		weatherLength = length;
	}
	
	public static int getWeatherTop(Level level, int x, int z) {
		int y = level.getTopY() - 1;
		int minY = level.getBottomY();
		Chunk chunk = level.getChunkFromCache(x >> 4, z >> 4);
		x &= 15;
		z &= 15;
		BlockState state = chunk.getBlockState(x, y, z);
		while (!state.isAir() && y > minY) state = chunk.getBlockState(x, --y, z);
		return y;
	}
	
	public static int getWeatherBottom(Level level, int x, int y, int z) {
		int minY = level.getBottomY();
		Chunk chunk = level.getChunkFromCache(x >> 4, z >> 4);
		x &= 15;
		z &= 15;
		BlockState state = chunk.getBlockState(x, y, z);
		while (state.isAir() && y > minY) state = chunk.getBlockState(x, --y, z);
		return y;
	}
	
	public static int getWeatherBottom(Level level, int x, int z) {
		return getWeatherBottom(level, level.getChunkFromCache(x >> 4, z >> 4), x & 15, z & 15);
	}
	
	private static int getWeatherBottom(Level level, Chunk chunk, int x, int z) {
		int y = level.getTopY() - 1;
		int minY = level.getBottomY();
		BlockState state = chunk.getBlockState(x, y, z);
		while (!state.isAir() && y > minY) state = chunk.getBlockState(x, --y, z);
		while (state.isAir() && y > minY) state = chunk.getBlockState(x, --y, z);
		return y;
	}
	
	public static int getCurrentWeatherLength() {
		return weatherLength;
	}
}
