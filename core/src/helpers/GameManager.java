package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;


public class GameManager {
    private static final GameManager ourInstance = new GameManager();
    public boolean gameStartedFromMainMenu, isPaused = true;
    public int lifeScore, coinScore, score;
    public GameData gameData;
    private Json json = new Json();
    private FileHandle fileHandle = Gdx.files.local("bin/gameData.json");
    private Music music;
    public int screen; // 1- mainMenu 2-game 3-highScore 4-options
    public Object screenObj;
    private GameManager() {

    }

    public void initializeGameData() {
        if (!fileHandle.exists()) {
            gameData = new GameData();
            gameData.setHighCoinScore(0);
            gameData.setHighScore(0);
            gameData.setEasy(false);
            gameData.setMed(true);
            gameData.setHard(false);
            gameData.setMusicOn(true);
            saveData();
        } else {
            loadData();
        }
    }

    public void saveData() {
        if (gameData != null) {
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false);
        }
    }

    public void loadData() {
        gameData = json.fromJson(GameData.class, Base64Coder.decodeString(fileHandle.readString()));
    }

    public void checkForNewHighScore() {
        int oldHighScore = gameData.getHighScore();
        int oldCoinScore = gameData.getHighCoinScore();

        if (oldHighScore < score) {
            gameData.setHighScore(score);
            saveData();
        }

        if (oldCoinScore < coinScore) {
            gameData.setHighCoinScore(coinScore);
            saveData();
        }
    }

    public void playMusic() {
        if (music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal("Music/Background.mp3"));
        }
        if (!music.isPlaying()) {
            music.play();
            music.setLooping(true);
        }
    }

    public void stopMusic() {
        if (music.isPlaying()) {
            music.stop();
            music.dispose();
        }
    }

    public static GameManager getInstance() {
        return ourInstance;
    }


}
