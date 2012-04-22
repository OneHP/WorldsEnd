package domain;

public interface Ship extends Destructable, Updatable, Drawable {

	Planet getOwner();

	boolean getTargetHit();

	Planet getTarget();

	int getCost();

	int getLaserDamage();

}
