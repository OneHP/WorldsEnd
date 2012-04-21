package util;

import java.util.List;

import com.google.common.collect.Lists;
import com.jme3.asset.AssetManager;

import domain.Planet;

public class StaticAccess {

	private static AssetManager ASSET_MANAGER = null;
	private static List<Planet> PLANETS = null;

	public static void setAssetManager(AssetManager assetManager) {
		ASSET_MANAGER = assetManager;
	}

	public static AssetManager getAssetManager() {
		return ASSET_MANAGER;
	}

	public static List<Planet> getPlanets() {
		return Lists.newArrayList(PLANETS);
	}

	public static void setPlanets(List<Planet> planets) {
		PLANETS = planets;
	}
}
