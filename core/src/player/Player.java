package player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


import helpers.GameInfo;

public class Player extends Sprite {
    private World world;
    private Body body;
    private TextureAtlas playerAtlas;
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean isWalking, dead;

    public Player(World world, float x, float y) {
        super(new Texture("Player/Player 1.png"));
        setPosition(x, y);
        this.world = world;
        createBody();
        dead = false;
        playerAtlas = new TextureAtlas("Player Animation/Player Animation.atlas");
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((getX() - 10) / GameInfo.PPM, getY() / GameInfo.PPM);
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2 - 20) / GameInfo.PPM, getHeight() / 2 / GameInfo.PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4f;
        fixtureDef.friction = 2f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.PLAYER;
        fixtureDef.filter.maskBits = GameInfo.DEFAULT | GameInfo.COLLECTABLES;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Player");
        shape.dispose();

    }

    public void movePlayer(float x) {
        if (x < 0 && !this.isFlipX()) {
            this.flip(true, false);
        } else if (body.getLinearVelocity().x > 0 && this.isFlipX()) {
            this.flip(true, false);
        }
        isWalking = true;
        body.setLinearVelocity(x, body.getLinearVelocity().y);
    }

    public void updatePlayer() {
        if (body.getLinearVelocity().x > 0) {
            //right
            setPosition(body.getPosition().x * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
        } else if (body.getLinearVelocity().x < 0) {
            //left
            setPosition((body.getPosition().x - .3f) * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
        }
    }

    public void drawPlayerIdeal(SpriteBatch batch) {
        if (!isWalking) {
            batch.draw(this, getX() + getWidth() / 2f, getY() - getHeight() / 2);
        }
    }

    public void drawPlayerAnimation(SpriteBatch batch) {
        TextureAtlas temp = playerAtlas;
        temp.dispose();
        playerAtlas = new TextureAtlas("Player Animation/Player Animation.atlas");
        if (isWalking) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            Array<TextureAtlas.AtlasRegion> frames = playerAtlas.getRegions();
            for (TextureRegion frame : frames) {
                if (body.getLinearVelocity().x < 0 && !frame.isFlipX()) {
                    frame.flip(true, false);
                } else if (body.getLinearVelocity().x > 0 && frame.isFlipX()) {
                    frame.flip(true, false);
                }
            }
            animation = new Animation<TextureRegion>(1f / 10f, playerAtlas.getRegions());
            batch.draw(animation.getKeyFrame(elapsedTime, true), getX() + getWidth() / 2f, getY() - getHeight() / 2);
        }

    }

    public void setWalking(boolean isWalking) {
        this.isWalking = isWalking;
    }

    public boolean getWalking() {
        return isWalking;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDead() {
        return dead;
    }
}


