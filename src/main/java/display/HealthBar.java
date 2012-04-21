package display;

import java.util.List;

import util.ManagerAccess;

import com.google.common.collect.Lists;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

import domain.Destructable;

public class HealthBar {

	private final Vector3f center;
	private final int max;
	private final int current;

	private final List<Geometry> parts = Lists.newArrayList();

	public HealthBar(Destructable destructable) {
		this(destructable.getLocation().add(destructable.getSize(),
				destructable.getSize(), 0), destructable.getMaxHealth(),
				destructable.getCurrentHealth(), destructable
						.getHealthBarSize());
	}

	public HealthBar(Vector3f center, int max, int current, float barSize) {

		this.center = center;
		this.max = max;
		this.current = current;

		float health = (float) current / (float) max;
		float greenSize = health * barSize;
		float greenStart = center.x - (barSize / 2);
		float redStart = greenStart + greenSize;
		float end = greenStart + barSize;

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
