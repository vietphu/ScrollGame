package resources;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.OrderedMap;

public class GameData implements Serializable {

	private int highScore;

	@Override
	public void write(Json json) {

	}

	@Override
	public void read(Json json, OrderedMap<String, Object> jsonData) {

	}

	public int getHighScore() {
		return this.highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

}
