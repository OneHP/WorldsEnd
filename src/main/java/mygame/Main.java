package mygame;

import java.util.List;
import java.util.Set;

import util.Constants;
import util.StaticAccess;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

import display.HealthBar;
import display.Menu;
import display.MenuItem;
import domain.Destructable;
import domain.Planet;
import domain.SmallShip;

public class Main extends SimpleApplication {

	private List<Planet> planets;
	private List<SmallShip> ships;
	private List<Geometry> display;
	private Planet homePlanet;

	private float displayLimiter = 0.0f;
	private float battleLimiter = 0.0f;
	private float goldTimer = 0.0f;

	private BitmapText score;
	private BitmapText gold;

	private List<BitmapText> menuText;

	public static void main(String[] args) {
		Main app = new Main();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		StaticAccess.setAssetManager(this.assetManager);

		setupWindow();

		this.homePlanet = new Planet(new Vector3f(0, 0, 0), 2);
		Planet enemyPlanet1 = new Planet(new Vector3f(40, 15, 0), 8);
		Planet enemyPlanet2 = new Planet(new Vector3f(-10, 30, 0), 7);
		Planet enemyPlanet3 = new Planet(new Vector3f(40, -40, 0), 9);
		Planet enemyPlanet4 = new Planet(new Vector3f(-30, -10, 0), 5);
		Planet enemyPlanet5 = new Planet(new Vector3f(-55, 20, 0), 8);

		this.planets = Lists.newArrayList(enemyPlanet1, enemyPlanet2,
				enemyPlanet3, enemyPlanet4, enemyPlanet5);

		StaticAccess.setPlanets(this.planets);

		this.display = Lists.newArrayList();
		this.ships = Lists.newArrayList();

		this.rootNode.attachChild(this.homePlanet.getView());
		for (Planet planet : this.planets) {
			this.rootNode.attachChild(planet.getView());
		}

		BitmapFont font = this.assetManager
				.loadFont("Interface/Fonts/Default.fnt");
		this.score = new BitmapText(font, false);
		this.score.setSize(Constants.GUI_FONT_SIZE);
		this.score.setColor(ColorRGBA.Green);
		this.score.setLocalTranslation(new Vector3f(65, 40, 0));
		this.gold = new BitmapText(font, false);
		this.gold.setSize(Constants.GUI_FONT_SIZE);
		this.gold.setColor(ColorRGBA.Yellow);
		this.gold.setLocalTranslation(new Vector3f(65, 38, 0));
		this.rootNode.attachChild(this.score);
		this.rootNode.attachChild(this.gold);

		displayMenu();
	}

	@Override
	public void simpleUpdate(float tpf) {

		this.goldTimer += tpf;
		if (this.goldTimer > Constants.GOLD_RATE) {
			this.goldTimer = 0.0f;
			for (Planet planet : this.planets) {
				planet.goldTick();
			}
			this.homePlanet.goldTick();
		}

		this.displayLimiter += tpf;
		if (this.displayLimiter > 0.1f) {
			this.displayLimiter = 0.0f;
			redrawDisplay();
		}

		this.battleLimiter += tpf;
		if (this.battleLimiter > Constants.ENGAGEMENT_RATE) {
			this.battleLimiter = 0.0f;
			for (final SmallShip ship : this.ships) {
				List<SmallShip> otherShips = Lists.newArrayList(Iterables
						.filter(this.ships, new Predicate<SmallShip>() {
							@Override
							public boolean apply(SmallShip input) {
								return (input.getOwner() != ship.getOwner())
										&& input.getLocation().distance(
												ship.getLocation()) < Constants.ENGAGEMENT_DISTANCE;
							}
						}));
				for (SmallShip otherShip : otherShips) {
					otherShip.takeDamage(1, ship.getOwner());
				}
			}
			Set<SmallShip> deadShips = Sets.newHashSet();
			for (SmallShip ship : this.ships) {
				if (ship.getDead()) {
					deadShips.add(ship);
				}
			}
			removeDeadShips(deadShips);
		}

		updateShips(tpf);
		updatePlanets(tpf);
	}

	private void displayMenu() {
		this.menuText = Lists.newArrayList();
		Menu menu = new Menu();
		MenuItem root = menu.getRoot();
		this.menuText.add(menuText(root, 0, 0));
		for (int i = 0; i < root.getSubMenu().size(); i++) {
			this.menuText.add(menuText(root.getSubMenu().get(i), 1, 1 + i));
		}
		for (BitmapText text : this.menuText) {
			this.rootNode.attachChild(text);
		}
	}

	private BitmapText menuText(MenuItem menuItem, int depth, int bredth) {
		BitmapFont font = this.assetManager
				.loadFont("Interface/Fonts/Default.fnt");
		BitmapText text = new BitmapText(font, false);
		text.setText(menuItem.getItem());
		text.setSize(Constants.GUI_FONT_SIZE);
		text.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 0.5f));
		text.setLocalTranslation(new Vector3f(-72.5f + (depth * 2),
				40 - (bredth * 2), 0));
		return text;
	}

	private void updatePlanets(float tpf) {
		for (Planet planet : this.planets) {
			planet.update(tpf);
			if (planet.getSendAttack()) {
				SmallShip smallShip = new SmallShip(planet,
						planet.getAttackTarget(), planet.getLocation());
				this.ships.add(smallShip);
				this.rootNode.attachChild(smallShip.getView());
				planet.confirmAttack();
			}
		}
	}

	private void updateShips(float tpf) {
		Set<SmallShip> deadShips = Sets.newHashSet();

		for (SmallShip ship : this.ships) {
			ship.update(tpf);
			if (ship.getTargetHit()) {
				int currentHealth = ship.getCurrentHealth();
				Planet owner = ship.getOwner();
				Planet target = ship.getTarget();
				target.takeDamage(currentHealth, owner);
				owner.scoreDamage(currentHealth, target);
				deadShips.add(ship);
			}
		}

		removeDeadShips(deadShips);
	}

	private void removeDeadShips(Set<SmallShip> deadShips) {
		for (SmallShip ship : deadShips) {
			this.rootNode.detachChild(ship.getView());
		}
		this.ships.removeAll(deadShips);
	}

	private void redrawDisplay() {
		for (Geometry geometry : this.display) {
			this.rootNode.detachChild(geometry);
		}

		for (Planet planet : this.planets) {
			drawDestructableDisplay(planet);
		}
		drawDestructableDisplay(this.homePlanet);

		for (SmallShip ship : this.ships) {
			drawDestructableDisplay(ship);
		}
		this.score.setText(String.valueOf(this.homePlanet.getScore()));
		this.gold.setText(String.valueOf(this.homePlanet.getGold()));
	}

	private void drawDestructableDisplay(Destructable destructable) {
		for (Geometry geometry : new HealthBar(destructable).getParts()) {
			this.display.add(geometry);
			this.rootNode.attachChild(geometry);
		}
	}

	@Override
	public void simpleRender(RenderManager rm) {
		// TODO: add render code
	}

	private void setupWindow() {
		this.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		this.getCamera().setLocation(new Vector3f(0, 0, 100));
		this.mouseInput.setCursorVisible(true);
		this.inputManager.clearMappings();
	}

}
