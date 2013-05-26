package com.kayimhalil.scrollgame;

import java.util.Iterator;

import resources.Enemy;
import resources.EnemyFactory;
import resources.EnemyState;
import resources.GameState;
import resources.Player;
import resources.PowerUp.PowerUpType;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class ScrollGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture playerImage, enemyImage, lifeImage, goalImage;
	private Rectangle goal;
	private Vector3 touch;
	private float width, height;
	private BitmapFont comicBookFont, gameOverFont;
	private ShapeRenderer shapeRenderer;
	private Actor easy, hard;

	private Array<Enemy> enemies;
	private Player player;
	private Music guileTheme;

	private float enemyVelocity;
	private int score, enemyChaseOffset, numInitialEnemies;
	private String scoreString;
	private boolean isBurned, first;

	private GameState state, prevState;

	private final String gameOverText = "GAME OVER!";

	private class ButtonHandler implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			switch (state) {
			case GAME_OVER:
				enemies.clear();
				initParameters();
				break;
			case MENU:
				Rectangle eR = new Rectangle(easy.getX(), easy.getY(),
						easy.getWidth(), easy.getHeight());
				Rectangle hR = new Rectangle(hard.getX(), hard.getY(),
						hard.getWidth(), hard.getHeight());
				if (eR.contains(screenX * width / Gdx.graphics.getWidth(),
						screenY * height / Gdx.graphics.getHeight())) {
					difficulty = Difficulty.HARD;
					changeGameState(GameState.TRANSITION);
					for (int i = 0; i < numInitialEnemies; i++) {
						spawnNewEnemy();
					}
				} else if (hR.contains(
						screenX * width / Gdx.graphics.getWidth(), screenY
								* height / Gdx.graphics.getHeight())) {
					difficulty = Difficulty.EASY;
					changeGameState(GameState.TRANSITION);
					for (int i = 0; i < numInitialEnemies; i++) {
						spawnNewEnemy();
					}
				}

				break;
			case PAUSED:
				break;
			case PLAYING:
				break;
			case TRANSITION:
				break;
			default:
				break;

			}
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			return false;
		}

	}

	private long touchReleaseTime, transitionTime;

	private Difficulty difficulty;

	enum Difficulty {
		EASY, HARD
	}

	@Override
	public void create() {
		Gdx.input.setInputProcessor(new ButtonHandler());
		player = new Player();
		initParameters();

		width = 800;// Gdx.graphics.getWidth();
		height = 480; // Gdx.graphics.getHeight();

		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(false, width, height);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.WHITE);

		loadFiles();

		touch = null;
		goal = new Rectangle();
		spawnNewGoal();

		easy = new Actor();
		easy.setWidth(comicBookFont.getBounds("EASY").width);
		easy.setHeight(comicBookFont.getCapHeight());
		easy.setPosition((width - easy.getWidth()) / 2,
				height / 2 + easy.getHeight() + 10);

		hard = new Actor();
		hard.setWidth(comicBookFont.getBounds("HARD").width);
		hard.setHeight(comicBookFont.getCapHeight());
		hard.setPosition((width - hard.getWidth()) / 2,
				height / 2 - hard.getHeight() - 10);

		enemies = new Array<Enemy>();

		Enemy.setDefaultVelocity(enemyVelocity);
		Enemy.setChaseOffset(enemyChaseOffset);

		guileTheme.setLooping(true);
	}

	private void loadFiles() {
		playerImage = new Texture(Gdx.files.internal("player.png"));
		enemyImage = new Texture(Gdx.files.internal("enemy.png"));
		lifeImage = new Texture(Gdx.files.internal("heart.png"));
		goalImage = new Texture(Gdx.files.internal("goal.png"));

		guileTheme = Gdx.audio.newMusic(Gdx.files
				.internal("sound/guile_theme.mp3"));

		comicBookFont = new BitmapFont(
				Gdx.files.internal("fonts/comic_book.fnt"),
				Gdx.files.internal("fonts/comic_book_0.png"), false);

		gameOverFont = new BitmapFont(
				Gdx.files.internal("fonts/game_over.fnt"),
				Gdx.files.internal("fonts/game_over_0.png"), false);

		gameOverFont.setScale(2);
	}

	private void initParameters() {
		state = GameState.MENU;
		score = 0;
		enemyVelocity = 100.0f;
		enemyChaseOffset = 200;
		scoreString = "0";
		touchReleaseTime = -1;
		transitionTime = -1;
		first = true;
		numInitialEnemies = 3;
		difficulty = Difficulty.HARD;
		player.reset();
	}

	private void spawnNewEnemy() {
		Rectangle enemyPos = new Rectangle();
		Enemy enemy = null;
		switch (difficulty) {
		case EASY:
			enemy = EnemyFactory.getInstance().getNewEnemy(enemyPos);
			break;
		case HARD:
			enemy = EnemyFactory.getInstance().getNewSmartEnemy(enemyPos);
			break;
		}
		enemyPos.set(MathUtils.random(64, width - 32),
				MathUtils.random(64, height - 32), 32, 32);
		enemies.add(enemy);
	}

	private void spawnNewGoal() {
		goal.set(MathUtils.random(96, width - 48),
				MathUtils.random(96, height - 48), 48, 48);
	}

	private void removeBurned() {
		if (isBurned) {
			isBurned = false;
		}
	}

	private void changeGameState(GameState newState) {
		this.prevState = this.state;
		this.state = newState;
	}

	private void endGame() {
		changeGameState(GameState.TRANSITION);
		guileTheme.stop();
	}

	private void setBurned() {
		if (!isBurned) {
			isBurned = true;
			Gdx.input.vibrate(200);
			player.loseLife();
		}
	}

	private void countDown() {
		if (touchReleaseTime != -1) {
			float delta = ((3000 - (TimeUtils.millis() - touchReleaseTime)) / 1000.0f);
			if (TimeUtils.millis() - touchReleaseTime >= 3000) {
				Gdx.input.vibrate(200);
				player.loseLife();
				touchReleaseTime = TimeUtils.millis();
			} else {
				String counter = String.format("%d", MathUtils.ceil(delta));
				comicBookFont.setScale(1 + (delta % 1.0f));
				comicBookFont.draw(batch, counter,
						width / 2 - comicBookFont.getBounds(counter).width / 2,
						height / 2 + comicBookFont.getCapHeight() / 2);
				comicBookFont.setScale(1);
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
		playerImage.dispose();
		enemyImage.dispose();
		comicBookFont.dispose();
		gameOverFont.dispose();
		guileTheme.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		loop();

		draw();

		handleInput();
	}

	private void loop() {
		switch (state) {
		case PLAYING:
			if (player.isInGame()) {
				Iterator<Enemy> iterator = enemies.iterator();
				boolean safe = true;
				while (iterator.hasNext()) {
					Enemy enemy = iterator.next();

					if (player.getPosition().overlaps(enemy.getPos())) {
						safe = false;
						setBurned();
						break;
					}
					Vector3.tmp.set(player.getPosition().x,
							player.getPosition().y, 0);
					if (Vector3.tmp.dst(enemy.getPos().x, enemy.getPos().y, 0) < Enemy
							.getChaseOffset()) {
						if (player.getActivePowerUpType() == PowerUpType.REPEL) {
							enemy.changeState(EnemyState.RUNNING_AWAY);
						} else {
							enemy.changeState(EnemyState.CHASING);
						}
					} else {
						enemy.changeState(EnemyState.WANDERING);
					}
				}
				if (safe)
					removeBurned();

				if (player.getPosition().overlaps(goal)) {
					score++;
					scoreString = String.format("%d", score);
					spawnNewGoal();
				}

				if (score % 20 == 0
						&& enemies.size != MathUtils.floor(score / 20)
								+ numInitialEnemies) {
					spawnNewEnemy();
				}
			}
			if (player.getLifeCount() <= 0 && player.isInGame()) {
				player.setInGame(false);
				endGame();
			}
		case GAME_OVER:
			break;
		case MENU:
			break;
		case PAUSED:
			break;
		case TRANSITION:
			if (transitionTime == -1) {
				transitionTime = TimeUtils.millis() + 1000;
			}
			if (transitionTime - TimeUtils.millis() <= 0) {
				if (prevState == GameState.MENU)
					changeGameState(GameState.PLAYING);
				else if (prevState == GameState.PLAYING)
					changeGameState(GameState.GAME_OVER);
			}
			break;
		default:
			break;
		}
	}

	private void draw() {
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		shapeRenderer.begin(ShapeType.Line);
		switch (state) {
		case MENU:
			comicBookFont.draw(batch, "EASY", easy.getX(), easy.getY());
			comicBookFont.draw(batch, "HARD", hard.getX(), hard.getY());
			break;
		case GAME_OVER:
			comicBookFont.draw(batch, gameOverText,
					(width - comicBookFont.getBounds(gameOverText).width) / 2,
					(height + comicBookFont.getCapHeight()) / 2 + 10);
			comicBookFont.draw(
					batch,
					"Your score: " + scoreString,
					(width - comicBookFont.getBounds("Your score: "
							+ scoreString).width) / 2,
					(height - comicBookFont.getCapHeight()) / 2 - 10);
			break;
		case PLAYING:
			countDown();

			batch.draw(goalImage, goal.x, goal.y);
			if (player.isInGame()) {
				batch.draw(playerImage, player.getPosition().x,
						player.getPosition().y);
			}
			for (Enemy enemy : enemies) {
				batch.draw(enemyImage, enemy.getPos().x, enemy.getPos().y);
				shapeRenderer.line(enemy.getPos().x * Gdx.graphics.getWidth()
						/ width + 16,
						enemy.getPos().y * Gdx.graphics.getHeight() / height
								+ 16,
						enemy.getPos().x * Gdx.graphics.getWidth() / width
								+ enemy.getDirection().x * 50,
						enemy.getPos().y * Gdx.graphics.getHeight() / height
								+ enemy.getDirection().y * 50);
			}
			for (int i = 0; i < player.getLifeCount(); i++) {
				batch.draw(lifeImage, i * 32, 0);
			}
			gameOverFont.setColor(Color.WHITE);
			gameOverFont.draw(batch, scoreString,
					width - gameOverFont.getBounds(scoreString).width - 1,
					gameOverFont.getCapHeight() + 1);
			break;
		case PAUSED:
			break;
		case TRANSITION:
			break;
		default:
			break;
		}
		batch.end();
		shapeRenderer.end();
	}

	private void handleInput() {
		switch (state) {
		case MENU:
			break;
		case PLAYING:
			if (Gdx.input.isTouched()) {
				if (touch == null) {
					touch = new Vector3();
					touchReleaseTime = -1;
				}
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touch);

				if (!player.isInGame()) {
					player.setInGame(true);
				}
				player.setPosition(touch.x - 48 / 2, touch.y - 48 / 2, 48, 48);

				for (Enemy enemy : enemies) {
					enemy.move(player);
				}
				if (!guileTheme.isPlaying()) {
					guileTheme.play();
				}
			} else {
				touch = null;
				player.setInGame(false);
				if (guileTheme.isPlaying()) {
					guileTheme.pause();
				}
				if (touchReleaseTime == -1) {
					touchReleaseTime = TimeUtils.millis();
					if (!first) {
						player.loseLife();
					}
					first = false;
				}
			}
			break;
		case GAME_OVER:
			// if (Gdx.input.isTouched()) {
			// changeGameState(GameState.MENU);
			// }
			break;
		case PAUSED:
			break;
		case TRANSITION:
			break;
		default:
			break;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		changeGameState(GameState.PAUSED);
	}

	@Override
	public void resume() {
		changeGameState(this.prevState);
	}
}
