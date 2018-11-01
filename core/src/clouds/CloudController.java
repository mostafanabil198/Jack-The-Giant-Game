package clouds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import collectables.Collectables;
import helpers.GameInfo;
import helpers.GameManager;
import player.Player;


public class CloudController {
    private World world;
    private Array<Cloud> clouds = new Array<Cloud>();
    private Array<Collectables> collectables = new Array<Collectables>();
    private final float DISTANCE_BETWEEN_CLOUDS = 250f;
    private float minX, maxX;
    private Random rand = new Random();
    private float cameraPosition;
    private float lastCloudPositionY;
    private Collectables collectable1;

    public CloudController(World world) {
        this.world = world;
        minX = GameInfo.WIDTH / 2f - 130;
        maxX = GameInfo.WIDTH / 2f + 130;
        createClouds();
        positionClouds(true);
        //createColllectables();


    }

    //Clouds
    public void createClouds() {

        for (int i = 0; i < 2; i++) {
            clouds.add(new Cloud(world, "Dark Cloud"));
        }
        int index = 1;
        for (int i = 0; i < 6; i++) {
            clouds.add(new Cloud(world, "Cloud " + index));
            index++;
            if (index == 4) {
                index = 1;
            }
        }
        clouds.shuffle();
    }

    public void positionClouds(boolean firstTimeArranging) {
        while (clouds.get(0).getCloudName() == "Dark Cloud") {
            clouds.shuffle();
        }
        float positionY = 0;
        if (firstTimeArranging) {
            positionY = GameInfo.HEIGHT / 2f;
        } else {
            positionY = lastCloudPositionY;
        }
        int controlX = 0;
        for (Cloud c : clouds) {
            if (c.getX() == 0 && c.getY() == 0) {
                float tempX = 0;
                if (controlX == 0) {
                    tempX = randomBetweenNumbers(maxX - 25, maxX);
                    controlX = 1;
                    c.setDrawLeft(false);
                } else {
                    tempX = randomBetweenNumbers(minX + 25, minX);
                    controlX = 0;
                    c.setDrawLeft(true);

                }
                c.setSpritePosition(tempX, positionY);
                positionY -= DISTANCE_BETWEEN_CLOUDS;
                lastCloudPositionY = positionY;

                if (!firstTimeArranging && c.getCloudName() != "Dark Cloud") {
                    int num = rand.nextInt(20);
                    if (num > 12 && num <= 18) {
                        collectable1 = new Collectables(world, "Coin");
                        collectable1.setCollectablePosition(c.getX(), c.getY() + 40);
                        collectables.add(collectable1);
                    } else if (num > 18 && GameManager.getInstance().lifeScore < 2) {
                        collectable1 = new Collectables(world, "Life");
                        collectable1.setCollectablePosition(c.getX(), c.getY() + 40);
                        collectables.add(collectable1);
                    }

                }
            }


        }
    }

    public void removeOffScreenColleactables() {
        for (int i = 0; i < collectables.size; i++) {
            if (collectables.get(i).getY() - GameInfo.HEIGHT / 2 - 15 > cameraPosition) {
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);

            }
        }
    }

    public void createAndArrangeNewClouds() {
        for (int i = 0; i < clouds.size; i++) {
            if (clouds.get(i).getY() - GameInfo.HEIGHT / 2 - 15 > cameraPosition) {
                clouds.get(i).getTexture().dispose();
                clouds.removeIndex(i);
            }
        }

        if (clouds.size == 4) {
            createClouds();
            positionClouds(false);
        }
    }

    public void drawClouds(SpriteBatch batch) {
        for (Cloud c : clouds) {
            if (c.getDrawLeft()) {
                batch.draw(c, c.getX() - c.getWidth() / 2 - 20, c.getY() - c.getHeight() / 2f);
            } else {
                batch.draw(c, c.getX() - c.getWidth() / 2 + 10, c.getY() - c.getHeight() / 2f);
            }

        }
    }

    //player
    public Player positionThePlayer(Player player) {
        player = new Player(world, clouds.get(0).getX(), clouds.get(0).getY() + 78);
        return player;
    }


    public void drawCollectables(SpriteBatch batch) {
        for (Collectables c : collectables) {
            c.updateCollectablePositions();
            batch.draw(c, c.getX(), c.getY());
        }
    }

    public void removeCollectables() {
        for (int i = 0; i < collectables.size; i++) {
            if (collectables.get(i).getFixture().getUserData() == "Remove") {
                collectables.get(i).changeFilter();
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
            }
        }
    }

    public void setCameraPosition(float cameraY) {
        this.cameraPosition = cameraY;
    }

    private float randomBetweenNumbers(float min, float max) {
        return rand.nextFloat() * (max - min) + min;
    }


}
