package huds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.MainMenu;

public class UIHud {

    private GameMain game;
    private Viewport gameViewport;
    private Stage stage;
    private Image coinImg, lifeImg, scoreImg, pausePanel;
    private Label scoreLbl, lifeLbl, coingLbl;
    private ImageButton pauseBtn, resumeBtn, quitBtn;

    public UIHud(GameMain game) {
        this.game = game;
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        if (GameManager.getInstance().gameStartedFromMainMenu) {
            GameManager.getInstance().gameStartedFromMainMenu = false;
            GameManager.getInstance().lifeScore = 2;
            GameManager.getInstance().coinScore = 0;
            GameManager.getInstance().score = 0;

        }
        createAndPosition();
    }


    public void createAndPosition() {
        //Creating the font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        BitmapFont font = generator.generateFont(parameter);

        //Creating labels
        scoreLbl = new Label("" + GameManager.getInstance().score, new Label.LabelStyle(font, Color.WHITE));
        lifeLbl = new Label("x" + GameManager.getInstance().lifeScore, new Label.LabelStyle(font, Color.WHITE));
        coingLbl = new Label("x" + GameManager.getInstance().coinScore, new Label.LabelStyle(font, Color.WHITE));

        //Creating images
        coinImg = new Image(new SpriteDrawable(new Sprite(new Texture("Collectables/Coin.png"))));
        lifeImg = new Image(new SpriteDrawable(new Sprite(new Texture("Collectables/Life.png"))));
        scoreImg = new Image(new SpriteDrawable(new Sprite(new Texture("Buttons/Gameplay Buttons/Score.png"))));


        //Positioning the labels and images in tables
        //crating the table and putting it in the top left
        Table coinAndLifeTable = new Table();
        Table scoreTable = new Table();
        coinAndLifeTable.top().left();
        coinAndLifeTable.setFillParent(true);
        scoreTable.top().right();
        scoreTable.setFillParent(true);

        //adding the images and labels to the table
        coinAndLifeTable.add(lifeImg).padLeft(10).padTop(10);
        coinAndLifeTable.add(lifeLbl).padLeft(5);
        coinAndLifeTable.row();
        coinAndLifeTable.add(coinImg).padLeft(10).padTop(10);
        coinAndLifeTable.add(coingLbl).padLeft(5);
        scoreTable.add(scoreImg).padRight(10).padTop(10);
        scoreTable.row();
        scoreTable.add(scoreLbl).padRight(20).padTop(15);

        //Creating button and its action and position it
        pauseBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Gameplay Buttons/Pause.png"))));
        pauseBtn.setPosition(470, 17, Align.bottomRight);
        pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().isPaused = true;
                createPausePanel();
            }
        });
        //adding actors to stage
        stage.addActor(pauseBtn);
        stage.addActor(coinAndLifeTable);
        stage.addActor(scoreTable);
    }

    public void createPausePanel() {
        pausePanel = new Image(new Texture("Pause Panel And Buttons/Pause Panel.png"));
        resumeBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Pause Panel And Buttons/Resume.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Pause Panel And Buttons/Quit 2.png"))));
        pausePanel.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2, Align.center);
        resumeBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 + 50, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 - 80, Align.center);

        stage.addActor(pausePanel);
        stage.addActor(resumeBtn);
        stage.addActor(quitBtn);

        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().isPaused = false;
                removePausePanel();

            }
        });
        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });


    }

    public void removePausePanel() {
        pausePanel.remove();
        resumeBtn.remove();
        quitBtn.remove();
    }

    public void createGameOverPanel() {
        Image gameOverPanel = new Image(new Texture("Pause Panel And Buttons/Show Score.png"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        BitmapFont font = generator.generateFont(parameter);
        Label endScore = new Label(String.valueOf(GameManager.getInstance().score), new Label.LabelStyle(font, Color.WHITE));
        Label endCoinScore = new Label(String.valueOf(GameManager.getInstance().coinScore), new Label.LabelStyle(font, Color.WHITE));
        gameOverPanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center);
        endScore.setPosition(GameInfo.WIDTH / 2f - 30, GameInfo.HEIGHT / 2f + 20, Align.center);
        endCoinScore.setPosition(GameInfo.WIDTH / 2f - 30, GameInfo.HEIGHT / 2f - 90, Align.center);
        stage.addActor(gameOverPanel);
        stage.addActor(endCoinScore);
        stage.addActor(endScore);
    }

    public void incrementScore(int score) {
        GameManager.getInstance().score += score;
        scoreLbl.setText("" + GameManager.getInstance().score);
    }

    public void decrementLife() {
        GameManager.getInstance().lifeScore--;
        if (GameManager.getInstance().lifeScore >= 0) {
            lifeLbl.setText("x" + GameManager.getInstance().lifeScore);
        }
    }

    public void incrementCoins() {
        GameManager.getInstance().coinScore++;
        coingLbl.setText("x" + GameManager.getInstance().coinScore);
        incrementScore(200);
    }

    public void incrementLife() {
        GameManager.getInstance().lifeScore++;
        lifeLbl.setText("x" + GameManager.getInstance().lifeScore);
        incrementScore(300);
    }

    public Stage getStage() {
        return this.stage;
    }
}
