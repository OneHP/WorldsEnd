package util;

public class Constants {

	public static float SHIP_SPEED_MULTIPLIER = 1.3f;

	public static float PLANET_HEALTH_BAR_SIZE = 3.5f;
	public static float SHIP_HEALTH_BAR_SIZE = 1.5f;

	public static float SMALL_SHIP_SPEED = SHIP_SPEED_MULTIPLIER * 1.5f;
	public static int SMALL_SHIP_COST = 7;
	public static int SMALL_SHIP_HEALTH = 10;

	public static float BOMBER_SHIP_SPEED = SHIP_SPEED_MULTIPLIER * 1.0f;
	public static int BOMBER_SHIP_COST = 14;
	public static int BOMBER_SHIP_HEALTH = 20;
	public static int BOMBER_DAMAGE_MULTIPLIER = 3;

	public static float DESTROYER_SHIP_SPEED = SHIP_SPEED_MULTIPLIER * 0.8f;
	public static int DESTROYER_SHIP_COST = 30;
	public static int DESTROYER_SHIP_HEALTH = 50;

	public static float MISSILE_SHIP_SPEED = SHIP_SPEED_MULTIPLIER * 2.0f;
	public static int MISSILE_SHIP_COST = 27;
	public static int MISSILE_SHIP_HEALTH = 1;

	public static float GUI_FONT_SIZE = 1.5f;

	public static float GOLD_RATE = 1.0f;
	public static float ACTION_RATE = 1.0f;

	public static float ENGAGEMENT_RATE = 0.2f;
	public static float ENGAGEMENT_DISTANCE = SHIP_SPEED_MULTIPLIER * 0.8f;

	public static float REVENGE_LIMIT = 29;
	public static int HOME_PLANET_ATTACK_RATE = 20;

	public static float GOLD_RESERVE_RATE = ACTION_RATE * 5;
	public static float GOLD_RESERVE_RESET_RATE = ACTION_RATE * 30;

	public static float BOMBER_PREFERENCE_RATE = 7;
	public static float MISSILE_PREFERENCE_RATE = 3;
	public static float DESTROYER_PREFERENCE_RATE = 2;
}
