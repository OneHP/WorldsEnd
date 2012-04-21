package domain;

import meshes.BomberShipMesh;
import util.Constants;
import util.StaticAccess;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class BomberShip extends AbstractShip {

	public BomberShip(Planet owner, Planet target, Vector3f location) {
		super(owner, target, location);
		Geometry geo = new Geometry("bomberShip", new BomberShipMesh());
		geo.setMaterial(getMaterial());
		geo.setLocalTranslation(location);
		super.initGeometry(geo);
		super.initHealth(Constants.BOMBER_SHIP_HEALTH);
	}

	private Material getMaterial() {
		Material material = new Material(StaticAccess.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.White);
		return material;
	}

	@Override
	public float getSize() {
		return 1.0f;
	}

	@Override
	public void update(float tpf) {
		Vector3f targetHeading = this.getTarget().getLocation()
				.subtract(this.getLocation()).normalize();
		Vector3f movement = targetHeading
				.mult(tpf * Constants.SMALL_SHIP_SPEED);
		getView().move(movement);
		if (this.getLocation().distance(this.getTarget().getLocation()) < this
				.getTarget().getSize()) {
			setTargetHit(true);
		}
	}

	@Override
	public int cost() {
		return Constants.BOMBER_SHIP_COST;
	}

}
