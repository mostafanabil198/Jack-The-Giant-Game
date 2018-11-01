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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.GameMain;

import helpers.GameData;
import helpers.GameInfo;
import helpers.GameManager;
import scenes.HighScore;
import scenes.MainMenu;

public class HighScoreBtns {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;
    private Label scoreLbl, coinLbl;
    private ImageButton backbtn;

    public HighScoreBtns(GameMain game) {
        this.game = game;
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        createAndPositionAndAddActor();


    }

    private void createAndPositionAndAddActor() {
        backbtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Buttons/Back.png"))));
        backbtn.setPosition(GameInfo.WIDTH - 17, 17, Align.bottomRight);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        BitmapFont scoreFont = generator.generateFont(parameter);
        BitmapFont coinFont = generator.generateFont(parameter);
        scoreLbl = new Label(String.valueOf(GameManager.getInstance().gameData.getHighScore()), new Label.LabelStyle(scoreFont, Color.WHITE));
        coinLbl = new Label(String.valueOf(GameManager.getInstance().gameData.getHighCoinScore()), new Label.LabelStyle(coinFont, Color.WHITE));
        scoreLbl.setPosition(GameInfo.WIDTH / 2 - 33, GameInfo.HEIGHT / 2 - 125);
        coinLbl.setPosition(GameInfo.WIDTH / 2 - 20, GameInfo.HEIGHT / 2 - 215);
        stage.addActor(backbtn);
        stage.addActor(scoreLbl);
        stage.addActor(coinLbl);
        backbtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });

    }

    public Stage getStage() {
        return this.stage;
    }
}
