package domain;

import util.Constants;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import display.HealthBar;

public abstract class AbstractShip implements Ship {

	private int maxHealth = 0;
	private int currentHealth = 0;
	private final Vector3f location;
	private Geometry geometry = null;
	private final Planet owner;
	private final Planet target;
	private boolean targetHit = false;
	private HealthBar healthBar;

	public AbstractShip(Planet owner, Planet target, Vector3f location) {
		this.owner = owner;
		this.target = target;
		this.location = location;
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

	public void initGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public void setTargetHit(boolean targetHit) {
		this.targetHit = targetHit;
	}

	public void initHealth(int health) {
		this.maxHealth = health;
		this.currentHealth = health;
	}

	@Override
	public float getHealthBarSize() {
		return Constants.SHIP_HEALTH_BAR_SIZE;
	}

	@Override
	public void setHealthBar(HealthBar healthBar) {
		this.healthBar = healthBar;

	}

	@Override
	public HealthBar getHealthBar() {
		return this.healthBar;
	}

}
