package display;

import java.util.List;

import util.StaticAccess;

import com.google.common.collect.Lists;

import domain.BomberShip;
import domain.DestroyerShip;
import domain.Planet;
import domain.Ship;
import domain.SmallShip;

public class Menu {

	private MenuItem root;
	private boolean launchAttack;
	private Planet target;
	private Class<? extends Ship> shipType;
	private int repeatCount;

	public Menu() {

		this.repeatCount = 1;
		List<MenuItem> empty = Lists.newArrayList();

		List<Planet> planets = StaticAccess.getPlanets();
		List<MenuItem> planetsMenuItems = Lists.newArrayList();
		for (final Planet planet : planets) {
			planetsMenuItems.add(new MenuItem(planet.toString(), empty) {
				@Override
				public boolean action() {
					Menu.this.target = planet;
					Menu.this.launchAttack = true;
					return true;
				}
			});
		}

		MenuItem smallShip = new MenuItem("Small Ship", planetsMenuItems) {
			@Override
			public boolean action() {
				Menu.this.root = getSelectedItem();
				Menu.this.shipType = SmallShip.class;
				return false;
			}
		};

		MenuItem bomberShip = new MenuItem("Bomber Ship", planetsMenuItems) {
			@Override
			public boolean action() {
				Menu.this.root = getSelectedItem();
				Menu.this.shipType = BomberShip.class;
				return false;
			}
		};

		MenuItem destroyerShip = new MenuItem("Destroyer Ship", planetsMenuItems) {
			@Override
			public boolean action() {
				Menu.this.root = getSelectedItem();
				Menu.this.shipType = DestroyerShip.class;
				return false;
			}
		};

		MenuItem sendShip = new MenuItem("Send Ship", Lists.newArrayList(smallShip, bomberShip, destroyerShip)) {
			@Override
			public boolean action() {
				Menu.this.root = getSelectedItem();
				return false;
			}
		};

		this.root = new MenuItem("Main", Lists.newArrayList(sendShip)) {
			@Override
			public boolean action() {
				Menu.this.root = getSelectedItem();
				return false;
			}
		};

	}

	public void upALevel() {
		if (null != this.root.getParent()) {
			this.root = this.root.getParent();
		}
	}

	public void navigateUp() {
		if (this.root.getSelectedIndex() > 0) {
			this.root.decreaseSelection();
		}
	}

	public void navigateDown() {

		if (this.root.getSelectedIndex() < (this.root.getSubMenu().size() - 1)) {
			this.root.increaseSelection();
		}
	}

	public MenuItem getRoot() {
		return this.root;
	}

	public boolean getLaunchAttack() {
		return this.launchAttack;
	}

	public Planet getTarget() {
		return this.target;
	}

	public Class<? extends Ship> getShipType() {
		return this.shipType;
	}

	public void increaseRepeatCount() {
		this.repeatCount = Math.min(10, this.repeatCount + 1);
	}

	public void decreaseRepeatCount() {
		this.repeatCount = Math.max(1, this.repeatCount - 1);
	}

	public int getRepeatCount() {
		return this.repeatCount;
	}
}
