package com.kag.mapgenerator;

import com.kag.common.data.GameMap;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IMapGenerator;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=IMapGenerator.class)
public class MapGenerator implements IMapGenerator {

	@Override
	public GameMap generateMap(int width, int height) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		GameMap map = new GameMap(width, height);
		map.setSpriteSheet(assetManager.createAsset(getClass().getClassLoader().getResourceAsStream("tilesheet.png")));
		for (int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++){
				map.getTile(x, y).setLayer(0, 12);
			}
		}
		return map;
	}

}
