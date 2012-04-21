package display;

import java.util.List;

import util.Constants;
import util.ManagerAccess;

import com.google.common.collect.Lists;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

import domain.Planet;

public class HealthBar {

	private final Vector3f center;
	private final int max;
	private final int current;

	private final List<Geometry> parts = Lists.newArrayList();

	public HealthBar(Planet planet) {
		this(planet.getLocation().add(planet.getSize(), planet.getSize(), 0),
				planet.getMaxHealth(), planet.getCurrentHealth());
	}

	public HealthBar(Vector3f center, int max, int current) {

		this.center = center;
		this.max = max;
		this.current = current;

		float health = (float) current / (float) max;
		float greenSize = health * Constants.HEALTH_BAR_SIZE;
		float greenStart = center.x - (Constants.HEALTH_BAR_SIZE / 2);
		float redStart = greenStart + greenSize;
		float end = greenStart + Constants.HEALTH_BAR_SIZE;

		Geometry green = new Geometry("healthBar", centeredBar(greenStart,
				redStart));
		green.setMaterial(getGreenMaterial());
		Geometry red = new Geometry("healthBar", centeredBar(redStart, end));
		red.setMaterial(getRedMaterial());
		this.parts.add(green);
		this.parts.add(red);
	}

	private Line centeredBar(float start, float end) {
		return new Line(new Vector3f(start, this.center.y, this.center.z),
				new Vector3f(end, this.center.y, this.center.z));
	}

	private Material getRedMaterial() {
		Material material = new Material(ManagerAccess.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Red);
		return material;
	}

	private Material getGreenMaterial() {
		Material material = new Material(ManagerAccess.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Green);
		return material;
	}

	public List<Geometry> getParts() {
		return this.parts;
	}

}
