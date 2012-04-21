package util;

import com.jme3.asset.AssetManager;

public class ManagerAccess {

	private static AssetManager ASSET_MANAGER = null;

	public static void setAssetManager(AssetManager assetManager) {
		ASSET_MANAGER = assetManager;
	}

	public static AssetManager getAssetManager() {
		return ASSET_MANAGER;
	}
}
