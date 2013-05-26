package resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Enemy {
	protected static float defaultVelocity = 50;
	protected static int chaseOffset = 100;
	protected EnemyState currentState;
	protected Rectangle pos;
	protected Rectangle randomDestination;
	protected float chasingAcceleration, velocity;
	protected Vector3 direction;

	protected Enemy(Rectangle pos) {
		this.currentState = EnemyState.WANDERING;
		this.pos = pos;
		this.randomDestination = null;
		this.chasingAcceleration = 50;
		this.direction = new Vector3();
		this.velocity = defaultVelocity;
	}

	public Rectangle getPos() {
		return this.pos;
	}

	public EnemyState getCurrentState() {
		return this.currentState;
	}

	public void changeState(EnemyState newState) {
		if (newState == EnemyState.WANDERING)
			this.velocity = defaultVelocity;
		this.currentState = newState;
	}

	public void move(Player player) {

		switch (this.currentState) {
		case WANDERING:
			wander();
			break;
		case BLOCKING:
			break;
		case RUNNING_AWAY:
			flee(player);
			break;
		case CHASING:
			chase(player);
			break;
		case WAITING:
			break;
		default:
			break;
		}
	}

	public static void setDefaultVelocity(float newVelocity) {
		if (newVelocity >= 0)
			defaultVelocity = newVelocity;
	}

	public static void setChaseOffset(int newOffset) {
		if (newOffset >= 0)
			chaseOffset = newOffset;
	}

	public static float getDefaultVelocity() {
		return defaultVelocity;
	}

	public static int getChaseOffset() {
		return chaseOffset;
	}

	protected void chase(Player player) {

		this.direction.set(player.getPosition().x - this.pos.x,
				player.getPosition().y - this.pos.y, 0);
		this.direction.nor();
		this.pos.x += this.velocity * Gdx.graphics.getDeltaTime()
				* this.direction.x;
		this.pos.y += this.velocity * Gdx.graphics.getDeltaTime()
				* this.direction.y;
	}

	protected void flee(Player player) {
		Vector3.tmp.set(this.pos.x - player.getPosition().x,
				this.pos.y - player.getPosition().y, 0);
		this.pos.x += this.velocity * Gdx.graphics.getDeltaTime()
				* Vector3.tmp.nor().x;
		this.pos.y += this.velocity * Gdx.graphics.getDeltaTime()
				* Vector3.tmp.nor().y;
	}

	protected void wander() {
		if (this.randomDestination == null) {
			this.randomDestination = new Rectangle();
			this.randomDestination.x = MathUtils.random(800);
			this.randomDestination.y = MathUtils.random(480);
		}

		if (Math.abs(this.pos.x - this.randomDestination.getX()) <= 1
				&& Math.abs(this.pos.y - this.randomDestination.getY()) <= 1) {
			this.randomDestination = null;
		} else {

			Vector3.tmp.set(this.randomDestination.x - this.pos.x,
					this.randomDestination.y - this.pos.y, 0);
			this.pos.x += this.velocity * Gdx.graphics.getDeltaTime()
					* Vector3.tmp.nor().x;
			this.pos.y += this.velocity * Gdx.graphics.getDeltaTime()
					* Vector3.tmp.nor().y;
		}
	}

	public float getVelocity() {
		return this.velocity;
	}

	public Vector3 getDirection() {
		return this.direction;
	}
}
