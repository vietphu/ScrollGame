package resources;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PowerUp extends Actor{

	private PowerUpType type;

	public enum PowerUpType {
		REPEL, REMOVE_TOUCH, NONE
	}

	protected PowerUp() {
		super();
		if (MathUtils.random(0, 1) == 0) {
			type = PowerUpType.REPEL;
		} else {
			type = PowerUpType.REMOVE_TOUCH;
		}
	}

}
