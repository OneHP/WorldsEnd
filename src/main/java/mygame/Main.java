package mygame;

import java.util.List;

import util.ManagerAccess;

import com.google.common.collect.Lists;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

import domain.Planet;

public class Main extends SimpleApplication {

	private List<Planet> planets;

	public static void main(String[] args) {
		Main app = new Main();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		ManagerAccess.setAssetManager(this.assetManager);

		this.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		this.getCamera().setLocation(new Vector3f(0, 0, 15));
		this.mouseInput.setCursorVisible(true);
		this.inputManager.clearMappings();

		this.planets = Lists.newArrayList();

		this.planets.add(new Planet(new Vector3f(0, 0, 0)));

		for (Planet planet : this.planets) {
			this.rootNode.attachChild(planet.getView());
		}

	}

	@Override
	public void simpleUpdate(float tpf) {

	}

	@Override
	public void simpleRender(RenderManager rm) {
		// TODO: add render code
	}

}
