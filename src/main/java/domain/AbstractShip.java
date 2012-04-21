package domain;

import util.Constants;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public abstract class AbstractShip implements Ship {

	private final int maxHealth;
	private int currentHealth;
	private final Vector3f location;
	private Geometry geometry;
	private final Planet owner;
	private final Planet target;
	private boolean targetHit = false;

	public AbstractShip(Planet owner, Planet target, Vector3f location) {
		this.owner = owner;
		this.target = target;
		this.maxHealth = 10;
		this.currentHealth = 10;
		this.location = location;
		this.geometry = null;
	}

	@Override
	public Vector3f getLocation() {
		return this.geometry.getLocalTranslation();
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
	public void takeDamage(int damage, Planet source) {
		this.currentHealth -= damage;
	}

	@Override
	public boolean getDead() {
		return this.currentHealth <= 0;
	}

	@Override
	public Geometry getView() {
		return this.geometry;
	}

	@Override
	public Planet getOwner() {
		return this.owner;
	}

	@Override
	public boolean getTargetHit() {
		return this.targetHit;
	}

	@Override
	public Planet getTarget() {
		return this.target;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public void setTargetHit(boolean targetHit) {
		this.targetHit = targetHit;
	}

	@Override
	public float getHealthBarSize() {
		return Constants.SHIP_HEALTH_BAR_SIZE;
	}

}
