package domain;

import com.jme3.math.Vector3f;

import display.HealthBar;

public interface Destructable {

	float getHealthBarSize();

	float getSize();

	Vector3f getLocation();

	int getMaxHealth();

	int getCurrentHealth();

	void takeDamage(int damage, Planet source);

	boolean getDead();

	void setHealthBar(HealthBar healthBar);

	HealthBar getHealthBar();
}
