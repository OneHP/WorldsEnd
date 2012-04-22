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
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

import display.HealthBar;
import display.Menu;
import display.MenuItem;
import domain.BomberShip;
import domain.DestroyerShip;
import domain.Destructable;
import domain.Planet;
import domain.Ship;
import domain.SmallShip;

public class Main extends SimpleApplication {

	private boolean gameStopped = true;

	private List<Planet> planets;
	private List<Ship> ships;
	private List<Geometry> display;
	private Planet homePlanet;

	private List<Ship> launchQueue;

	private float displayLimiter = 0.0f;
	private float battleLimiter = 0.0f;
	private float goldTimer = 0.0f;
	private float actionLimiter = 0.0f;

	private BitmapText score;
	private BitmapText gold;

	private boolean menuDisplayed = false;
	private Menu menu;
	private List<BitmapText> menuText;

	private AudioNode bomberDeath;
	private AudioNode laser;
	private AudioNode menuAction;
	private AudioNode menuBack;
	private AudioNode menuSelect;
	private AudioNode planetDeath;
	private AudioNode planetHit;
	private AudioNode smallDeath;
	private AudioNode music;

	public static void main(String[] args) {
		Main app = new Main();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		StaticAccess.setAssetManager(this.assetManager);

		this.bomberDeath = new AudioNode(this.assetManager, "Sounds/bomber_death.wav");
		this.laser = new AudioNode(this.assetManager, "Sounds/laser.wav");
		this.menuAction = new AudioNode(this.assetManager, "Sounds/menu_action.wav");
		this.menuAction.setVolume(0.3f);
		this.menuBack = new AudioNode(this.assetManager, "Sounds/menu_back.wav");
		this.menuBack.setVolume(0.3f);
		this.menuSelect = new AudioNode(this.assetManager, "Sounds/menu_select.wav");
		this.menuSelect.setVolume(0.3f);
		this.planetDeath = new AudioNode(this.assetManager, "Sounds/planet_death.wav");
		this.planetHit = new AudioNode(this.assetManager, "Sounds/planet_hit.wav");
		this.smallDeath = new AudioNode(this.assetManager, "Sounds/small_death.wav");
		this.music = new AudioNode(this.assetManager, "Sounds/music_1.ogg");
		this.music.setLooping(true);
		this.music.setVolume(0.7f);
		this.music.setTimeOffset(2.0f);

		setupWindow();

		this.music.play();

		setupIntro();
	}

	private void setupIntro() {
		BitmapText instructions = text(
				"Worlds End\n\nWar rages around your tiny homeworld, but being small has its benefits.\n"
						+ "The larger planets don't consider you much of a threat. Take advantage of this and hit them with all you've got.\n\n"
						+ "Ship Costs:\n" + "Fighter:   7g\n" + "Bomber:    15g\n" + "Destroyer: 30g\n\n"
						+ "Controls:\n" + "Bring up your ship launch menu with [Return]\n"
						+ "[Arrow] keys to navigate the menu\n" + "Confirm with [Return]\n"
						+ "Go back with [Backspace]\n"
						+ "You can launch multiple ships once you've confirmed the target, press [Right Arrow]\n\n\n"
						+ "Press [Return] to Start the Game\n", ColorRGBA.Green, new Vector3f(-30, 20, 0));

		this.rootNode.attachChild(instructions);

		this.inputManager.addMapping("Start", new KeyTrigger(KeyInput.KEY_RETURN));
		this.inputManager.addListener(this.startActionListener, new String[] { "Start" });
	}

	private final ActionListener startActionListener = new ActionListener() {
		@Override
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (name.equals("Start") && !keyPressed) {
				Main.this.inputManager.clearMappings();
				Main.this.inputManager.removeListener(Main.this.startActionListener);
				startGame();
			}
		}
	};

	private void startGame() {
		this.gameStopped = false;
		this.rootNode.detachAllChildren();
		this.homePlanet = new Planet(new Vector3f(0, 0, 0), 2);
		Planet enemyPlanet1 = new Planet(new Vector3f(40, 15, 0), 8);
		Planet enemyPlanet2 = new Planet(new Vector3f(-10, 30, 0), 7);
		Planet enemyPlanet3 = new Planet(new Vector3f(40, -40, 0), 9);
		Planet enemyPlanet4 = new Planet(new Vector3f(-20, -20, 0), 5);
		Planet enemyPlanet5 = new Planet(new Vector3f(-55, 20, 0), 8);

		this.planets = Lists.newArrayList(enemyPlanet1, enemyPlanet2, enemyPlanet3, enemyPlanet4, enemyPlanet5);

		StaticAccess.setHomePlanet(this.homePlanet);
		StaticAccess.setPlanets(this.planets);

		this.display = Lists.newArrayList();
		this.ships = Lists.newArrayList();
		this.launchQueue = Lists.newArrayList();

		this.rootNode.attachChild(this.homePlanet.getView());
		for (Planet planet : this.planets) {
			this.rootNode.attachChild(planet.getView());
			BitmapText nameTag = this.text(planet.getName(), ColorRGBA.Orange,
					planet.getLocation().add(-1 * planet.getSize(), planet.getSize() * 1.5f, 0));
			this.rootNode.attachChild(nameTag);
			planet.setNameTag(nameTag);
		}

		setupScoreDisplay();

		initKeys();
	}

	private BitmapText text(String content, ColorRGBA colour, Vector3f position) {
		BitmapFont font = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText text = new BitmapText(font, false);
		text.setSize(Constants.GUI_FONT_SIZE);
		text.setColor(colour);
		text.setLocalTranslation(position);
		text.setText(content);
		return text;
	}

	private void endGame() {
		this.gameStopped = true;
		this.rootNode.detachAllChildren();

		BitmapText instructions = text("Game Over\n\n"
				+ (this.homePlanet.getDead() ? "You died, but managed to score "
						: "You are the last planet standing! You scored ") + this.homePlanet.getScore()
				+ " points\n\n\n" + "Press [Return] to Play Again\n", ColorRGBA.Green, new Vector3f(-30, 20, 0));

		this.rootNode.attachChild(instructions);

		this.inputManager.clearMappings();
		this.inputManager.removeListener(this.actionListener);
		this.inputManager.addMapping("Restart", new KeyTrigger(KeyInput.KEY_RETURN));
		this.inputManager.addListener(this.restartActionListener, new String[] { "Restart" });

	}

	private final ActionListener restartActionListener = new ActionListener() {
		@Override
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (name.equals("Restart") && !keyPressed) {
				Main.this.inputManager.clearMappings();
				Main.this.inputManager.removeListener(Main.this.restartActionListener);
				startGame();
			}
		}
	};

	@Override
	public void simpleUpdate(float tpf) {

		if (this.gameStopped) {
			return;
		}

		if (this.planets.isEmpty() || this.homePlanet.getDead()) {
			endGame();
			return;
		}

		distributeGold(tpf);

		this.displayLimiter += tpf;
		if (this.displayLimiter > 0.1f) {
			this.displayLimiter = 0.0f;
			redrawDisplay();
		}

		engageBattle(tpf);
		updateShips(tpf);
		updatePlanets(tpf);
		updateHomePlanet(tpf);
	}

	private void distributeGold(float tpf) {
		this.goldTimer += tpf;
		if (this.goldTimer > Constants.GOLD_RATE) {
			this.goldTimer = 0.0f;
			for (Planet planet : this.planets) {
				planet.goldTick();
			}
			this.homePlanet.goldTick();
		}
	}

	private void engageBattle(float tpf) {
		this.battleLimiter += tpf;
		if (this.battleLimiter > Constants.ENGAGEMENT_RATE) {
			this.battleLimiter = 0.0f;

			List<Ship> engagedShips = getShipsWithinEngagementRange();

			for (final Ship ship : engagedShips) {
				if (!(ship instanceof BomberShip)) {
					List<Ship> otherShips = Lists.newArrayList(Iterables.filter(engagedShips, new Predicate<Ship>() {
						@Override
						public boolean apply(Ship input) {
							return (input.getOwner() != ship.getOwner())
									&& input.getLocation().distance(ship.getLocation()) < Constants.ENGAGEMENT_DISTANCE;
						}
					}));
					for (Ship otherShip : otherShips) {
						otherShip.takeDamage(ship.getLaserDamage(), ship.getOwner());
						this.laser.play();
					}
				}
			}
			Set<Ship> deadShips = Sets.newHashSet();
			for (Ship ship : this.ships) {
				if (ship.getDead()) {
					deadShips.add(ship);
					if (ship instanceof SmallShip) {
						this.smallDeath.play();
					} else if (ship instanceof BomberShip) {
						this.bomberDeath.play();
					} else if (ship instanceof DestroyerShip) {
						this.bomberDeath.play();
					}
				}
			}

			Set<Planet> deadPlanets = Sets.newHashSet();
			for (Planet planet : this.planets) {
				if (planet.getDead()) {
					deadPlanets.add(planet);
					this.planetDeath.play();
				}
			}
			for (Planet planet : deadPlanets) {
				this.rootNode.detachChild(planet.getView());
				this.rootNode.detachChild(planet.getNameTag());
			}
			this.planets.removeAll(deadPlanets);
			for (Ship ship : this.ships) {
				if (deadPlanets.contains(ship.getTarget())) {
					deadShips.add(ship);
				}
			}

			removeDeadShips(deadShips);
		}
	}

	private List<Ship> getShipsWithinEngagementRange() {
		List<Ship> engagedShips = Lists.newArrayList(Iterables.filter(this.ships, new Predicate<Ship>() {
			@Override
			public boolean apply(final Ship outer) {
				return Iterables.any(Main.this.ships, new Predicate<Ship>() {
					@Override
					public boolean apply(Ship inner) {
						return inner.getLocation().distance(outer.getLocation()) < Constants.ENGAGEMENT_DISTANCE;
					}
				});
			}
		}));
		return engagedShips;
	}

	private void initKeys() {
		this.inputManager.addMapping("Action", new KeyTrigger(KeyInput.KEY_RETURN));
		this.inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
		this.inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
		this.inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_BACK));
		this.inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
		this.inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
		this.inputManager.addListener(this.actionListener, new String[] { "Action", "Up", "Down", "Back", "Right",
				"Left" });
	}

	private final ActionListener actionListener = new ActionListener() {
		@Override
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (name.equals("Action") && !keyPressed) {
				if (Main.this.menuDisplayed) {
					removeMenu();
					if (Main.this.menu.getRoot().action()) {
						if (Main.this.menu.getLaunchAttack()) {
							launchAttack(Main.this.menu.getShipType(), Main.this.menu.getRepeatCount());
						}
						Main.this.menuDisplayed = false;
					} else {
						drawMenu();
					}
				} else {
					displayMenu();
				}
				Main.this.menuAction.play();
			}
			if (name.equals("Back") && !keyPressed) {
				if (Main.this.menuDisplayed) {
					removeMenu();
					if (null == Main.this.menu.getRoot().getParent()) {
						Main.this.menuDisplayed = false;
					} else {
						Main.this.menu.upALevel();
						drawMenu();
					}
					Main.this.menuBack.play();
				}
			}
			if (name.equals("Up") && !keyPressed) {
				if (Main.this.menuDisplayed) {
					Main.this.menu.navigateUp();
					removeMenu();
					drawMenu();
					Main.this.menuSelect.play();
				}
			}
			if (name.equals("Down") && !keyPressed) {
				if (Main.this.menuDisplayed) {
					Main.this.menu.navigateDown();
					removeMenu();
					drawMenu();
					Main.this.menuSelect.play();
				}
			}
			if (name.equals("Right") && !keyPressed) {
				if (Main.this.menuDisplayed && -1 == Main.this.menu.getRoot().getSelectedIndex()) {
					Main.this.menu.increaseRepeatCount();
					removeMenu();
					drawMenu();
					Main.this.menuSelect.play();
				}
			}
			if (name.equals("Left") && !keyPressed) {
				if (Main.this.menuDisplayed && -1 == Main.this.menu.getRoot().getSelectedIndex()) {
					Main.this.menu.decreaseRepeatCount();
					removeMenu();
					drawMenu();
					Main.this.menuSelect.play();
				}
			}
		}

		private void launchAttack(Class<? extends Ship> clazz, int times) {
			for (int i = 0; i < times; i++) {
				Ship ship = null;
				if (clazz == SmallShip.class) {
					ship = new SmallShip(Main.this.homePlanet, Main.this.menu.getTarget(),
							Main.this.homePlanet.getLocation());
				} else if (clazz == BomberShip.class) {
					ship = new BomberShip(Main.this.homePlanet, Main.this.menu.getTarget(),
							Main.this.homePlanet.getLocation());
				} else if (clazz == DestroyerShip.class) {
					ship = new DestroyerShip(Main.this.homePlanet, Main.this.menu.getTarget(),
							Main.this.homePlanet.getLocation());
				}
				Main.this.launchQueue.add(ship);
			}
		}
	};

	private void displayMenu() {
		this.menuDisplayed = true;
		this.menu = new Menu();
		drawMenu();
	}

	private void drawMenu() {
		this.menuText = Lists.newArrayList();
		MenuItem root = this.menu.getRoot();
		List<MenuItem> subMenu = root.getSubMenu();
		this.menuText.add(menuText(root, false, 0, 0, -1 == root.getSelectedIndex()));
		for (int i = 0; i < subMenu.size(); i++) {
			MenuItem menuItem = subMenu.get(i);
			this.menuText.add(menuText(menuItem, menuItem == root.getSelectedItem(), 1, 1 + i, false));
		}
		for (BitmapText text : this.menuText) {
			this.rootNode.attachChild(text);
		}
	}

	private void removeMenu() {
		for (BitmapText text : this.menuText) {
			this.rootNode.detachChild(text);
		}
	}

	private BitmapText menuText(MenuItem menuItem, boolean selected, int depth, int bredth, boolean leaf) {
		BitmapText text = text(menuItem.getItem() + (leaf ? " x " + this.menu.getRepeatCount() : ""), new ColorRGBA(
				selected || leaf ? 0.0f : 1.0f, 1.0f, selected || leaf ? 0.0f : 1.0f, 0.5f), new Vector3f(-72.5f
				+ (depth * 2), 40 - (bredth * 2), 0));
		return text;
	}

	private void updatePlanets(float tpf) {
		for (Planet planet : this.planets) {
			planet.update(tpf);
			if (planet.getSendAttack()) {
				Ship ship = null;
				Class<? extends Ship> clazz = planet.getShipType();
				if (clazz == SmallShip.class) {
					ship = new SmallShip(planet, planet.getAttackTarget(), planet.getLocation());
				} else if (clazz == BomberShip.class) {
					ship = new BomberShip(planet, planet.getAttackTarget(), planet.getLocation());
				} else if (clazz == DestroyerShip.class) {
					ship = new DestroyerShip(planet, planet.getAttackTarget(), planet.getLocation());
				}
				this.ships.add(ship);
				this.rootNode.attachChild(ship.getView());
				planet.confirmAttack(planet.getShipType());
			}
		}
	}

	private void updateHomePlanet(float tpf) {

		this.actionLimiter += tpf;
		if (this.actionLimiter > Constants.ACTION_RATE) {
			this.actionLimiter = 0.0f;
			if (!this.launchQueue.isEmpty()) {
				Ship ship = this.launchQueue.remove(0);
				if (this.planets.contains(ship.getTarget())) {
					if (ship.getCost() <= this.homePlanet.getGold()) {
						this.ships.add(ship);
						Main.this.rootNode.attachChild(ship.getView());
						Main.this.homePlanet.confirmAttack(ship.getClass());
					} else {
						this.launchQueue.add(ship);
					}
				}
			}
		}

	}

	private void updateShips(float tpf) {
		Set<Ship> deadShips = Sets.newHashSet();

		for (Ship ship : this.ships) {
			ship.update(tpf);
			if (ship.getTargetHit()) {
				int currentHealth = ship.getCurrentHealth();
				Planet owner = ship.getOwner();
				Planet target = ship.getTarget();
				if (!(ship instanceof DestroyerShip)) {
					target.takeDamage(currentHealth, owner);
					owner.scoreDamage(currentHealth, target);
					this.planetHit.play();
				}
				deadShips.add(ship);
			}
		}

		removeDeadShips(deadShips);
	}

	private void removeDeadShips(Set<Ship> deadShips) {
		for (Ship ship : deadShips) {
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

		for (Ship ship : this.ships) {
			drawDestructableDisplay(ship);
		}
		this.score.setText(String.valueOf(this.homePlanet.getScore() + " Points"));
		this.gold.setText(String.valueOf(this.homePlanet.getGold() + " Gold"));
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

	private void setupScoreDisplay() {
		this.score = text("", ColorRGBA.Green, new Vector3f(62, 40, 0));
		this.gold = text("", ColorRGBA.Yellow, new Vector3f(62, 38, 0));
		this.rootNode.attachChild(this.score);
		this.rootNode.attachChild(this.gold);
	}

	private void setupWindow() {
		this.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		this.getCamera().setLocation(new Vector3f(0, 0, 100));
		this.mouseInput.setCursorVisible(true);
		this.inputManager.clearMappings();
	}

}
