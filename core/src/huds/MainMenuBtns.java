package huds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.GamePlay;
import scenes.HighScore;
import scenes.OptionMenu;

public class MainMenuBtns {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private ImageButton playBtn;
    private ImageButton optionBtn;
    private ImageButton musicBtn;
    private ImageButton highscoreBtn;
    private ImageButton quitBtn;

    public MainMenuBtns(GameMain game) {
        this.game = game;
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        createAndPositionAndAddActorsBtns();
        addAllListeners();
        checkMusic();
    }

    void createAndPositionAndAddActorsBtns() {
        playBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu Buttons/Start Game.png"))));
        optionBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu Buttons/Options.png"))));
        musicBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu Buttons/Music On.png"))));
        highscoreBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu Buttons/Highscore.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Main Menu Buttons/Quit.png"))));
        playBtn.setPosition(GameInfo.WIDTH / 2 - 80, GameInfo.HEIGHT / 2 + 50, Align.center);
        optionBtn.setPosition(GameInfo.WIDTH / 2 - 40, GameInfo.HEIGHT / 2 - 90, Align.center);
        musicBtn.setPosition(GameInfo.WIDTH - 13, 13, Align.bottomRight);
        highscoreBtn.setPosition(GameInfo.WIDTH / 2 - 60, GameInfo.HEIGHT / 2 - 20, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2 - 20, GameInfo.HEIGHT / 2 - 160, Align.center);
        stage.addActor(playBtn);
        stage.addActor(optionBtn);
        stage.addActor(musicBtn);
        stage.addActor(highscoreBtn);
        stage.addActor(quitBtn);
    }

    void addAllListeners() {
        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().gameStartedFromMainMenu = true;
                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GamePlay(game));
                    }
                });
                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);
                stage.addAction(sa);
            }
        });
        highscoreBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new HighScore(game));
                    }
                });
                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);
                stage.addAction(sa);
            }
        });

        optionBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new OptionMenu(game));
                    }
                });
                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);
                stage.addAction(sa);
            }
        });
        musicBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (GameManager.getInstance().gameData.isMusicOn()) {
                    GameManager.getInstance().gameData.setMusicOn(false);
                    GameManager.getInstance().stopMusic();
                } else {
                    GameManager.getInstance().gameData.setMusicOn(true);
                    GameManager.getInstance().playMusic();
                }
                GameManager.getInstance().saveData();
            }
        });
        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.exit(0);
            }
        });
    }

    void checkMusic(){
        if(GameManager.getInstance().gameData.isMusicOn()){
            GameManager.getInstance().playMusic();
        }
    }
    public Stage getStage() {
        return this.stage;
    }
}
