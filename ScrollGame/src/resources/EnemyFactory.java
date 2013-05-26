package resources;

import com.badlogic.gdx.math.Rectangle;

public class EnemyFactory {
	private static EnemyFactory instance = new EnemyFactory();

	private EnemyFactory() {

	}

	public static EnemyFactory getInstance() {
		return instance;
	}

	public Enemy getNewEnemy(Rectangle pos) {
		Enemy enemy = new Enemy(pos);
		return enemy;
	}
	
	public Enemy getNewSmartEnemy(Rectangle pos) {
		Enemy enemy = new SmartEnemy(pos);
		return enemy;
	}
	
}
