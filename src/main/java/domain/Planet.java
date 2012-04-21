package domain;

import util.ManagerAccess;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import drawing.Circle;

public class Planet implements Drawable {

	private final int maxHealth;
	private final int currentHealth;
	private final float size;
	private final Vector3f location;
	private final Geometry geometry;

	public Planet(Vector3f location, float size) {
		this.maxHealth = 100;
		this.currentHealth = 70;
		this.size = size;
		this.location = location;
		this.geometry = new Geometry("planet", new Circle(this.size, 32));
		this.geometry.setMaterial(getMaterial());
		this.geometry.rotate(FastMath.HALF_PI, 0, 0);
		this.geometry.setLocalTranslation(location);
	}

	public Vector3f getLocation() {
		return this.location;
	}

	public float getSize() {
		return this.size;
	}

	@Override
	public Geometry getView() {
		return this.geometry;
	}

	private Material getMaterial() {
		Material material = new Material(ManagerAccess.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.White);
		return material;
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}

	public int getCurrentHealth() {
		return this.currentHealth;
	}
}
