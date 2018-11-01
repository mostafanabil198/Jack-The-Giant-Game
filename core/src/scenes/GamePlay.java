package scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.GameMain;

import clouds.CloudController;
import collectables.Collectables;
import helpers.GameInfo;
import helpers.GameManager;
import huds.UIHud;
import player.Player;

public class GamePlay implements Screen, ContactListener {

    private GameMain game;
    private Sprite[] bgs;
    private float lastYPosition;
    private OrthographicCamera mainCamera;
    private Viewport gameViewPort;
    private World world;
    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;
    private CloudController cloudController;
    private Player player;
    public UIHud hud;
    private boolean touchedFor1StTime = false;
    private float lastPlayerY;
    private float cameraSpeed = 10, cameraMaxSpeed = 10, acceleration = 10;
    private Sound coinSound, lifeSound, diedSound;

    public GamePlay(GameMain game) {
        GameManager.getInstance().screen = 2;
        GameManager.getInstance().screenObj = (GamePlay) this;
        this.game = game;
        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2, 0);
        gameViewPort = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(this);
        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM, GameInfo.HEIGHT / GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        debugRenderer = new Box2DDebugRenderer();
        cloudController = new CloudController(world);
        player = cloudController.positionThePlayer(player);
        hud = new UIHud(game);
        createBackgrounds();
        setCameraSpeed();
        coinSound = Gdx.audio.newSound(Gdx.files.internal("Music/Coin Sound.wav"));
        lifeSound = Gdx.audio.newSound(Gdx.files.internal("Music/Life Sound.wav"));
        diedSound = Gdx.audio.newSound(Gdx.files.internal("Music/died.wav"));
    }

    public void createBackgrounds() {
        bgs = new Sprite[3];
        for (int i = 0; i < bgs.length; i++) {
            bgs[i] = new Sprite(new Texture("Backgrounds/Game BG.png"));
            bgs[i].setPosition(0, -i * bgs[i].getHeight());
            lastYPosition = Math.abs(bgs[i].getY());
        }
    }

    public void drawBackgrounds() {
        for (int i = 0; i < bgs.length; i++) {
            game.getBatch().draw(bgs[i], bgs[i].getX(), bgs[i].getY());
        }
    }

    public void checkBackgroundOutOfBounds() {
        for (int i = 0; i < bgs.length; i++) {
            if (bgs[i].getY() - bgs[i].getHeight() / 2 - 5 > mainCamera.position.y) {
                float newPosition = lastYPosition + bgs[i].getHeight();
                bgs[i].setPosition(0, -newPosition);
                lastYPosition = Math.abs(newPosition);
            }
        }
    }

    public void checkPlayerOutOfBounds() {
        if (player.getY() - GameInfo.HEIGHT / 2f - player.getHeight() / 2f > mainCamera.position.y) {
            if (!player.isDead()) {
                playerDied();
                diedSound.play();
            }
        } else if (player.getY() + GameInfo.HEIGHT / 2f + player.getHeight() / 2f < mainCamera.position.y) {
            if (!player.isDead()) {
                playerDied();
                diedSound.play();
            }
        }
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getX() > 10) {
            player.movePlayer(-2);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getX() < GameInfo.WIDTH - 150) {
            player.movePlayer(2);
        } else {
            player.setWalking(false);
        }
    }

    public void handleInputAndroid() {
        if (Gdx.input.isTouched()) {

            if (Gdx.input.getX() > (GameInfo.WIDTH / 2)) {
                if (player.getX() < GameInfo.WIDTH - 150) {
                    player.movePlayer(2);
                }
            } else {
                if (player.getX() > 10) {
                    player.movePlayer(-2);
                }
            }
        } else {
            player.setWalking(false);
        }
    }

    public void update(float dt) {
        checkForFirstTouch();
        if (!GameManager.getInstance().isPaused) {
            //handleInput(dt);
            handleInputAndroid();
            moveCamera(dt);
            checkBackgroundOutOfBounds();
            cloudController.setCameraPosition(mainCamera.position.y);
            cloudController.createAndArrangeNewClouds();
            cloudController.removeOffScreenColleactables();
            checkPlayerOutOfBounds();
            countScore();
        }
    }

    public void moveCamera(float delta) {
        mainCamera.position.y -= cameraSpeed * delta;
        cameraSpeed += acceleration * delta;
        if (cameraSpeed > cameraMaxSpeed) {
            cameraSpeed = cameraMaxSpeed;
        }
    }

    void setCameraSpeed() {
        if (GameManager.getInstance().gameData.isEasy()) {
            cameraSpeed = 100;
            cameraMaxSpeed = 140;
        } else if (GameManager.getInstance().gameData.isMed()) {
            cameraSpeed = 130;
            cameraMaxSpeed = 180;
        } else if (GameManager.getInstance().gameData.isHard()) {
            cameraSpeed = 150;
            cameraMaxSpeed = 250;
        }
    }

    public void countScore() {
        if (lastPlayerY > player.getY()) {
            hud.incrementScore(1);
            lastPlayerY = player.getY();
        }
    }

    public void playerDied() {
        GameManager.getInstance().isPaused = true;
        hud.decrementLife();
        player.setDead(true);
        player.setPosition(1000, 1000);
        if (GameManager.getInstance().lifeScore < 0) {
            //no more to continue

            //check for high score
            GameManager.getInstance().checkForNewHighScore();
            //show the end score
            hud.createGameOverPanel();
            //load main menu
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new MainMenu(game));
                }
            });
            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(4f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);

            hud.getStage().addAction(sa);
        } else {
            //reload the game
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new GamePlay(game));
                }
            });
            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(2f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);

            hud.getStage().addAction(sa);
        }

    }

    public void checkForFirstTouch() {
        if (!touchedFor1StTime) {
            if (Gdx.input.justTouched()) {
                GameManager.getInstance().isPaused = false;
                touchedFor1StTime = true;
                lastPlayerY = player.getY();
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        drawBackgrounds();
        cloudController.drawClouds(game.getBatch());
        cloudController.drawCollectables(game.getBatch());
        player.drawPlayerIdeal(game.getBatch());
        player.drawPlayerAnimation(game.getBatch());
        game.getBatch().end();

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();

        //debugRenderer.render(world, box2DCamera.combined);
        game.getBatch().setProjectionMatrix(mainCamera.combined);

        mainCamera.update();

        player.updatePlayer();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        gameViewPort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        for (int i = 0; i < bgs.length; i++) {
            bgs[i].getTexture().dispose();
        }
        player.getTexture().dispose();
        debugRenderer.dispose();
        hud.getStage().dispose();
        lifeSound.dispose();
        coinSound.dispose();
        diedSound.dispose();

    }


    @Override
    public void beginContact(Contact contact) {
        Fixture body1, body2;

        if (contact.getFixtureA().getUserData() == "Player") {
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == "Coin") {
            //Collided with coin
            coinSound.play();
            body2.setUserData("Remove");
            cloudController.removeCollectables();
            hud.incrementCoins();
        }
        if (body1.getUserData() == ("Player") && body2.getUserData() == ("Life")) {
            lifeSound.play();
            //Collided with Life
            body2.setUserData("Remove");
            cloudController.removeCollectables();
            hud.incrementLife();
        }
        if (body1.getUserData() == ("Player") && body2.getUserData() == ("Dark Cloud")) {
            //Collided with dark cloud
            if (!player.isDead()) {
                playerDied();
                diedSound.play();
            }
        }


    }


    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
