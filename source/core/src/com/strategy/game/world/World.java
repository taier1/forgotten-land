package com.strategy.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.strategy.game.ExtendedStaticTiledMapTile;
import com.strategy.game.buildings.Building;
import com.strategy.game.buildings.StaticEntityBuilder;
import com.strategy.game.screens.GameScreen;
import com.strategy.game.screens.MessageLog;
//import com.sun.javafx.scene.control.skin.VirtualFlow;

import java.util.ArrayList;
import java.util.Random;

/**
 * Contains the world simulation.
 *
 */
public class World implements Disposable{

    private int tickDuration;
    private Stage gameStage;
    private GameScreen gameScreen;
    private ArrayList<Building> buildings; // Resources and Buildings
    private ArrayList<Resource> resources;
    private ArrayList<MovableEntity> movableEntities;
    private TiledMap map;
    private StaticEntityBuilder builder;
    private ResourceHandler resourceHandler;
    private int updateCounter;
    private int eventsCounter;
    private PopulationHandler populationHandler;
    private WorldEventsHandler worldEventsHandler;
    private int tick;
    private boolean isRunning;
    private static final int DEFAULT_TICK_DURATION = 300;
    private static final int FAST_TICK_DURATION = 10;
    private static final int WORLD_EVENT_FREQUENCY = 50; // After how many ticks we have a chance for a random event

    public enum GameSpeed {
        NORMAL, FAST
    }


    public World(GameScreen gameScreen) {
        this.gameStage = new Stage();
        this.gameScreen = gameScreen;
        this.map = gameScreen.getMap();
        this.buildings = new ArrayList<Building>();
        this.resources = new ArrayList<Resource>();
        this.movableEntities = new ArrayList<MovableEntity>();
        this.resourceHandler = new ResourceHandler(this, 300, 300, 300, 300, 5);
        this.updateCounter = 0;
        this.populationHandler = new PopulationHandler(5, 200, 20);
        this.tick = 0;
        this.isRunning = true;
        this.tickDuration = DEFAULT_TICK_DURATION;
        this.worldEventsHandler = new WorldEventsHandler(this);
        this.eventsCounter = 0;
        readResources();
    }

    public WorldEventsHandler getWorldEventsHandler() {
        return worldEventsHandler;
    }

    public void setBuilder(StaticEntityBuilder builder) {
        this.builder = builder;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public StaticEntityBuilder getBuilder() {
        return builder;
    }

    public ResourceHandler getResourceHandler() {
        return resourceHandler;
    }

    public PopulationHandler getPopulationHandler() {
        return populationHandler;
    }

    /**
     * Sets the simulation speed (i.e. the tick duration).
     * @param speed the requested speed (either normal or fast).
     */
    public void setGameSpeed(GameSpeed speed) {
        switch (speed){
            case NORMAL:
                tickDuration = DEFAULT_TICK_DURATION;
                break;
            case FAST:
                tickDuration = FAST_TICK_DURATION;
                break;
        }
    }

    /**
     * Changes the simulation speed.
     */
    public void toggleGameSpeed() {
        if (tickDuration == DEFAULT_TICK_DURATION) {
            tickDuration = FAST_TICK_DURATION;
        }
        else {
            tickDuration = DEFAULT_TICK_DURATION;
        }
    }

    /**
     * Toggles between running and pausing the game.
     */
    public void toggleRunning() {
        isRunning = !isRunning;
    }

    /**
     * What happens when the game-lost conditions are satisfied (i.e. people <= 0 for now).
     */
    public void handleGameLost() {
        isRunning = false;
        gameScreen.toggleGameOver();
        //TODO: add a message or something else
    }

    /**
     * Searches the map file for tiles that are of type resource, and adds them to the resource list
     * to keep track of them.
     * TODO: refactor
     */
    private void readResources() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Buildings");
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                if (cell != null && cell.getTile() != null) {
                    ExtendedStaticTiledMapTile tile = (ExtendedStaticTiledMapTile) cell.getTile();
                    String type = tile.getProperties().get("type", String.class);
                    if (type != null) {
                        if (type.equals("wood")) {
                            Resource wood = new Resource("Tree", 100);
                            wood.setMainTextureSimple(tile.getTextureRegion().getTexture());
                            wood.setCoords(new Vector2(x, y));
                            resources.add(wood);
                            tile.setObject(wood);
                        } else if (type.equals("rock")) {
                            Resource rock = new Resource("Stone", 100);
                            rock.setMainTextureSimple(tile.getTextureRegion().getTexture());
                            rock.setCoords(new Vector2(x, y));
                            resources.add(rock);
                            tile.setObject(rock);
                        }
                    }
                }
            }
        }
    }

    /**
     * The main world simulation loop, called every frame.
     * @param delta time since last frame (used for animations)
     */
    public void update(float delta) {
        if(updateCounter / tickDuration >= 1 && isRunning) {
            resourceHandler.update();
            updateCounter = 0;
            tick++;

            // TODO: refactor
            if (tick % WORLD_EVENT_FREQUENCY == 0) {
                // Roll dice
                int rand = new Random().nextInt(100);
                if (rand > 50) { // 50% probability of having the plague
                    worldEventsHandler.randomEvent();
                }
            }


            builder.checkDeadBuildings();
            gameScreen.getResourcesBar().update();
        }
        updateCounter++;
    }


    @Override
    public void dispose() {
        gameStage.dispose();
    }
}
