package helpers;

public class GameData {
    private int highScore;
    private int highCoinScore;
    private boolean easy;
    private boolean med;
    private boolean hard;
    private boolean musicOn;

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getHighCoinScore() {
        return highCoinScore;
    }

    public void setHighCoinScore(int highCoinScore) {
        this.highCoinScore = highCoinScore;
    }

    public boolean isEasy() {
        return easy;
    }

    public void setEasy(boolean easy) {
        this.easy = easy;
    }

    public boolean isMed() {
        return med;
    }

    public void setMed(boolean med) {
        this.med = med;
    }

    public boolean isHard() {
        return hard;
    }

    public void setHard(boolean hard) {
        this.hard = hard;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }
}
