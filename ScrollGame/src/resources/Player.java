package resources;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import resources.PowerUp.PowerUpType;

public class Player extends Actor {
	private PowerUpType activePowerUp;
	private Rectangle pos;
	private Vector3 direction;

	private int lifeCount;
	private boolean inGame;
	private float velocity;

	private List<Enemy> chasingEnemies;

	public Player() {
		super();
		this.setOrigin(24, 24);
		this.activePowerUp = PowerUpType.NONE;
		this.pos = new Rectangle();
		this.lifeCount = 3;
		this.inGame = false;
		this.velocity = 0;
		this.direction = new Vector3(0, 0, 0);
		this.chasingEnemies = new ArrayList<Enemy>();
	}

	public int getLifeCount() {
		return this.lifeCount;
	}

	public void lifeUp() {
		this.lifeCount++;
	}

	public void loseLife() {
		this.lifeCount--;
	}

	public Rectangle getPosition() {
		if (this.inGame)
			return this.pos;
		return null;
	}

	public void setPosition(float x, float y, int w, int h) {
		this.direction.x = x - this.pos.x;
		this.direction.y = y - this.pos.y;
		this.direction.nor();

		this.setRotation((float) Math.atan(this.direction.y / this.direction.x));

		this.velocity = (float) Math.sqrt(Math.pow(x - this.pos.x, 2)
				+ Math.pow(y - this.pos.y, 2));

		this.pos.x = x;
		this.pos.y = y;
		this.pos.width = w;
		this.pos.height = h;
	}

	public boolean isInGame() {
		return this.inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public PowerUpType getActivePowerUpType() {
		return this.activePowerUp;
	}

	public void setActivePowerUpType(PowerUpType activePowerUpType) {
		this.activePowerUp = activePowerUpType;
	}

	public float getVelocity() {
		return this.velocity;
	}

	public Vector3 getDirection() {
		return this.direction;
	}

	public int getNumOfChasingEnemies() {
		return this.chasingEnemies.size();
	}

	public void reset() {
		this.setOrigin(24, 24);
		this.activePowerUp = PowerUpType.NONE;
		this.pos = new Rectangle();
		this.lifeCount = 3;
		this.inGame = false;
		this.velocity = 0;
		this.direction = new Vector3(0, 0, 0);
		this.chasingEnemies = new ArrayList<Enemy>();
	}
}
