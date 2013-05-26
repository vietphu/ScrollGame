package resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class SmartEnemy extends Enemy {

	protected float estimatedTime;

	protected SmartEnemy(Rectangle pos) {
		super(pos);
		this.estimatedTime = 10f;
	}

	@Override
	protected void chase(Player player) {

		this.direction.set(player.getVelocity() * player.getDirection().x
				* this.estimatedTime + player.getPosition().x - this.pos.x,
				player.getVelocity() * player.getDirection().y
						* this.estimatedTime + player.getPosition().y
						- this.pos.y, 0);

		/*
		 * this.direction .set((player.getVelocity() * player.getDirection().x
		 * this.estCatchTime + deltaX) / (this.chasingAcceleration (float)
		 * Math.pow(estCatchTime, 2) + this.velocity this.estCatchTime),
		 * (player.getVelocity() * player.getDirection().y this.estCatchTime +
		 * deltaY) / (this.chasingAcceleration (float) Math.pow(estCatchTime, 2)
		 * + this.velocity this.estCatchTime), 0);
		 */

		this.direction.nor();

		/*
		 * this.direction.set(player.getVelocity() * player.getDirection().x
		 * estCatchTime + player.getPosition().x - this.pos.x,
		 * player.getVelocity() * player.getDirection().y * estCatchTime +
		 * player.getPosition().y - this.pos.y, 0); this.direction.nor();
		 */
		this.velocity += this.chasingAcceleration * Gdx.graphics.getDeltaTime();

		this.pos.x += this.velocity * Gdx.graphics.getDeltaTime()
				* this.direction.x;
		this.pos.y += this.velocity * Gdx.graphics.getDeltaTime()
				* this.direction.y;
		// System.out.printf("Velocity: %.3f\n", this.velocity);

	}

}
