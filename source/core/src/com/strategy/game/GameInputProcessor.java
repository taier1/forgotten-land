package com.strategy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.strategy.game.buildings.*;
import com.strategy.game.screens.GameScreen;
import com.strategy.game.world.Resource;
import com.strategy.game.world.World;


/**
 * Handles the game input
 */
public class GameInputProcessor implements InputProcessor{
    private GameScreen screen;
    private OrthographicCamera camera;
    private final int EDGE_THRESHOLD_WIDTH = 50;
    private Vector2 touchDownCoords;
    private boolean isPressingMouse;
    private Vector2 prevMouseCoords;
    private World world;

    public GameInputProcessor(GameScreen screen) {
        this.screen = screen;
        this.camera = screen.getCamera();
        this.world = screen.getWorld();
        this.isPressingMouse = false;
        this.prevMouseCoords = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    // Used to handle continuous presses
    public void pollKeyboard() {
        // Moves the camera in the specified direction
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(camera.zoom*Utils.BASE_CAMERA_SPEED,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(camera.zoom*(-Utils.BASE_CAMERA_SPEED),0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0,camera.zoom*Utils.BASE_CAMERA_SPEED);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0,camera.zoom*(-Utils.BASE_CAMERA_SPEED));
        }
    }

    public void pollMouse() {
        // y-axis starts bottom-left by default
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();


        // Move the camera when clicking and dragging.
        // TODO: make it so that you have to press it for a certain amount of time to start dragging
        Vector2 currentMouseCoords = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 deltaMouse = new Vector2(currentMouseCoords.x - prevMouseCoords.x, currentMouseCoords.y - prevMouseCoords.y);

        // Only works when not placing a building
        if (isPressingMouse && screen.getBuilder().getSelectedEntity() == null) {
            camera.translate(-deltaMouse.x * camera.zoom, deltaMouse.y * camera.zoom);
        }
        prevMouseCoords = new Vector2(currentMouseCoords);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.NUM_1:
                screen.getBuilder().setSelectedEntity(new House());
                break;
            case Input.Keys.NUM_2:
                screen.getBuilder().setSelectedEntity(new Castle());
                break;
            case Input.Keys.NUM_3:
                screen.getBuilder().setSelectedEntity(new Wall());
                break;
            case Input.Keys.NUM_4:
                screen.getBuilder().setSelectedEntity(new CollectorWood());
                break;
            case Input.Keys.NUM_5:
                screen.getBuilder().setSelectedEntity(new CollectorFood());
                break;
            case Input.Keys.NUM_6:
                screen.getBuilder().setSelectedEntity(new Warehouse());
                break;
            case Input.Keys.NUM_7:
                screen.getBuilder().setSelectedEntity(new CollectorRock());
                break;
            case Input.Keys.NUM_8:
                screen.getBuilder().setSelectedEntity(new CollectorGold());
                break;
            case Input.Keys.NUM_9:
                screen.getBuilder().setSelectedEntity(new Road());
                break;
            case Input.Keys.ESCAPE:
                screen.getBuilder().removeSelectedEntity();
                break;
            case Input.Keys.R:
                screen.getBuilder().rotate();
                break;
            case Input.Keys.P:
                screen.getWorld().toggleRunning();
                break;
            case Input.Keys.K:
                screen.getWorld().setGameSpeed(World.GameSpeed.NORMAL);
                break;
            case Input.Keys.L:
                screen.getWorld().setGameSpeed(World.GameSpeed.FAST);
                break;
            case Input.Keys.I:
                screen.getWorld().getResourceHandler().removeOnePop();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 touch = new Vector3(screenX, screenY, 0);

        // Set coords used for selection box
        screen.setTouchDownCoords(new Vector2(screenX, screenY));
        screen.setSelecting(true);

        if (button == Input.Buttons.LEFT) {
            isPressingMouse = true;
        }

        Vector3 pickedTile = Utils.cartesianToIso(touch, camera);

        MapEntity clickedEntity = screen.getBuilder().getEntityAt((int)pickedTile.x, (int)pickedTile.y);

        if (screen.getBuilder().getSelectedEntity() != null && button == Input.Buttons.LEFT) {
            screen.getBuilder().placeSelectedEntity((int) pickedTile.x, (int) pickedTile.y, false);
        }
        else if (screen.getBuilder().getSelectedEntity() == null && button == Input.Buttons.LEFT)  {
            // TODO: show building info from here

            if (clickedEntity instanceof Building) {
                screen.getSidebar().setEntity(clickedEntity, false);
                screen.getBuilder().showInfluenceArea((Building) clickedEntity);
            }
            else if (clickedEntity instanceof Resource) {
                screen.getSidebar().setEntity(clickedEntity, false);
            }
            else {
                screen.getBuilder().showInfluenceArea(null);
                screen.getSidebar().setEntity(null, false);
            }
        } else {
//            if (clickedEntity instanceof Building) {
//                screen.getBuilder().repairBuilding((Building) clickedEntity);
//            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        screen.setTouchUpCoords(new Vector2(screenX, screenY));
//        screen.setSelecting(false);
        isPressingMouse = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        // The center coordinates of the screen
        float centerX = Gdx.graphics.getWidth() / 2;
        float centerY = Gdx.graphics.getHeight() / 2;

        // Attenuates the amount of scrolling
        final float FACTOR = 7.5f;

        final float MAX_ZOOM = 0.4f;
        final float MIN_ZOOM = 5f;

        if(camera.zoom + amount / FACTOR > MAX_ZOOM && camera.zoom + amount / FACTOR < MIN_ZOOM) {
            camera.zoom += amount / FACTOR;
            camera.translate(- amount * (mouseX - centerX) / FACTOR, - amount * (mouseY - centerY) / FACTOR);
        }

        camera.update(); // Applies the changes.
        return true;
    }
}
