package display;

import java.util.List;

import util.StaticAccess;

import com.google.common.collect.Lists;

import domain.Planet;

public class Menu {

	private MenuItem root;
	private boolean launchAttack;
	private Planet target;

	public Menu() {

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
				return false;
			}
		};

		MenuItem sendShip = new MenuItem("Send Ship",
				Lists.newArrayList(smallShip)) {
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
}
