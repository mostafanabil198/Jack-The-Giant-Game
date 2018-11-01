package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.MainMenu;

public class OptionBtns {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;
    private ImageButton backBtn, easyBtn, mediumBtn, hardBtn;
    private Image sign;

    public OptionBtns(GameMain game) {
        this.game = game;
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        createAndPositionAndAddActors();
        addAllListeners();
    }

    private void createAndPositionAndAddActors() {
        backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Buttons/Back.png"))));
        easyBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Buttons/Easy.png"))));
        hardBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Buttons/Hard.png"))));
        mediumBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Buttons/Medium.png"))));
        sign = new Image(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Buttons/Check Sign.png"))));
        backBtn.setPosition(17, 17, Align.bottomLeft);
        easyBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 + 40, Align.center);
        mediumBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 - 40, Align.center);
        hardBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 - 120, Align.center);

        stage.addActor(backBtn);
        stage.addActor(easyBtn);
        stage.addActor(hardBtn);
        stage.addActor(mediumBtn);
        stage.addActor(sign);

        positionTheSign();
    }

    public void positionTheSign() {
        if (GameManager.getInstance().gameData.isEasy()) {
            sign.setPosition(GameInfo.WIDTH / 2 + 76, easyBtn.getY() + 13, Align.bottomLeft);
        } else if (GameManager.getInstance().gameData.isMed()) {
            sign.setPosition(GameInfo.WIDTH / 2 + 76, mediumBtn.getY() + 13, Align.bottomLeft);
        } else if (GameManager.getInstance().gameData.isHard()) {
            sign.setPosition(GameInfo.WIDTH / 2 + 76, hardBtn.getY() + 13, Align.bottomLeft);
        }
    }

    void changeDifficulty(int difficulty) {
        switch (difficulty) {
            case 0:
                GameManager.getInstance().gameData.setEasy(true);
                GameManager.getInstance().gameData.setMed(false);
                GameManager.getInstance().gameData.setHard(false);
                break;
            case 1:
                GameManager.getInstance().gameData.setEasy(false);
                GameManager.getInstance().gameData.setMed(true);
                GameManager.getInstance().gameData.setHard(false);
                break;
            case 2:
                GameManager.getInstance().gameData.setEasy(false);
                GameManager.getInstance().gameData.setMed(false);
                GameManager.getInstance().gameData.setHard(true);
                break;
        }
        GameManager.getInstance().saveData();
    }

    private void addAllListeners() {
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MainMenu(game));
                    }
                });
                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);
                stage.addAction(sa);
            }
        });
        easyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(0);
                sign.setY(easyBtn.getY() + 13);
            }
        });
        mediumBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(1);
                sign.setY(mediumBtn.getY() + 13);
            }
        });
        hardBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(2);
                sign.setY(hardBtn.getY() + 13);
            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }
}
