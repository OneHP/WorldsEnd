package domain;

import util.Constants;
import util.ManagerAccess;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import drawing.Circle;

public class Planet implements Drawable, Destructable {

	private final int maxHealth;
	private int currentHealth;
	private final float size;
	private final Vector3f location;
	private final Geometry geometry;
	private int gold = 10;
	private int score = 0;

	public Planet(Vector3f location, float size) {
		this.maxHealth = 100;
		this.currentHealth = 100;
		this.size = size;
		this.location = location;
		this.geometry = new Geometry("planet", new Circle(this.size, 32));
		this.geometry.setMaterial(getMaterial());
		this.geometry.rotate(FastMath.HALF_PI, 0, 0);
		this.geometry.setLocalTranslation(location);
	}

	@Override
	public Vector3f getLocation() {
		return this.location;
	}

	@Override
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
		material.setColor("Color", new ColorRGBA(0, 255, 255, 1));
		return material;
	}

	@Override
	public int getMaxHealth() {
		return this.maxHealth;
	}

	@Override
	public int getCurrentHealth() {
		return this.currentHealth;
	}

	@Override
	public float getHealthBarSize() {
		return Constants.PLANET_HEALTH_BAR_SIZE;
	}

	@Override
	public void takeDamage(int damage) {
		this.currentHealth -= damage;
	}

	public void scoreDamage(int damage) {
		this.gold += damage;
		this.score += damage;
	}

	public int getGold() {
		return this.gold;
	}

	public int getScore() {
		return this.score;
	}

}
