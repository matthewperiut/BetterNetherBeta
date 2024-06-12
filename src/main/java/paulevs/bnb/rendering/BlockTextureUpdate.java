package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;

public interface BlockTextureUpdate {
	@Environment(EnvType.CLIENT)
	void updateTextures(ExpandableAtlas blockAtlas);
}
