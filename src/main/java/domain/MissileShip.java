package domain;

import meshes.MissileShipMesh;
import util.Constants;
import util.StaticAccess;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class MissileShip extends AbstractShip {

	public MissileShip(Planet owner, Planet target, Vector3f location) {
		super(owner, target, location);
		Geometry geo = new Geometry("missileShip", new MissileShipMesh());
		geo.setMaterial(getMaterial());
		geo.setLocalTranslation(location);
		super.initGeometry(geo);
		super.initHealth(Constants.MISSILE_SHIP_HEALTH);
	}

	private Material getMaterial() {
		Material material = new Material(StaticAccess.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.White);
		return material;
	}

	@Override
	public int getCost() {
		return Constants.MISSILE_SHIP_COST;
	}

	@Override
	public int getLaserDamage() {
		return 0;
	}

	@Override
	public float getSize() {
		return 1;
	}

	@Override
	public void update(float tpf) {
		Vector3f targetHeading = this.getTarget().getLocation().subtract(this.getLocation()).normalize();
		Vector3f movement = targetHeading.mult(tpf * Constants.MISSILE_SHIP_SPEED);
		getView().move(movement);
		if (this.getLocation().distance(this.getTarget().getLocation()) < this.getTarget().getSize()) {
			setTargetHit(true);
		}
	}

	@Override
	public float getPlanetDamageMultiplier() {
		return 30;
	}

}
