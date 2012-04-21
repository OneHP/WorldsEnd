package domain;

import meshes.SmallShipMesh;
import util.Constants;
import util.ManagerAccess;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class SmallShip implements Drawable, Destructable {

	private final int maxHealth;
	private final int currentHealth;
	private final Vector3f location;
	private final Geometry geometry;
	private final Planet owner;
	private final Planet target;

	public SmallShip(Planet owner, Planet target, Vector3f location) {
		this.owner = owner;
		this.target = target;
		this.maxHealth = 10;
		this.currentHealth = 7;
		this.location = location;
		this.geometry = new Geometry("smallShip", new SmallShipMesh());
		this.geometry.setMaterial(getMaterial());
		this.geometry.setLocalTranslation(location);
	}

	@Override
	public Vector3f getLocation() {
		return this.location;
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
		return Constants.SHIP_HEALTH_BAR_SIZE;
	}

	@Override
	public float getSize() {
		return 1.0f;
	}

}
