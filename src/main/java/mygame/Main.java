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
		this.getCamera().setLocation(new Vector3f(0, 0, 100));
		this.mouseInput.setCursorVisible(true);
		this.inputManager.clearMappings();

		Planet homePlanet = new Planet(new Vector3f(0, 0, 0), 2);
		Planet enemyPlanet1 = new Planet(new Vector3f(40, 15, 0), 8);
		Planet enemyPlanet2 = new Planet(new Vector3f(-10, 35, 0), 7);
		Planet enemyPlanet3 = new Planet(new Vector3f(40, -40, 0), 9);
		Planet enemyPlanet4 = new Planet(new Vector3f(-30, -10, 0), 5);
		Planet enemyPlanet5 = new Planet(new Vector3f(-55, 20, 0), 8);

		this.planets = Lists.newArrayList(homePlanet, enemyPlanet1,
				enemyPlanet2, enemyPlanet3, enemyPlanet4, enemyPlanet5);

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
