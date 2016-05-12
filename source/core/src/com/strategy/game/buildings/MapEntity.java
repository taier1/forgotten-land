package com.strategy.game.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.strategy.game.ExtendedStaticTiledMapTile;
import com.strategy.game.Utils;

import java.util.ArrayList;

/**
 * A generic object to put on the map.
 */
public class MapEntity implements Disposable{
    protected Vector2 collisionSize;
    protected Texture mainTexture;
    private boolean isClicked;
    private int clickX, clickY;
    protected Vector2 imgOffset; //TODO: useless?
    private TiledMapTileLayer layer;
    protected ArrayList<Texture> textures;
    private int counter;
    protected int influenceRadius;

    private ExtendedStaticTiledMapTile[][] tiles;
    private TiledMapTileLayer.Cell[][] prevCells;


    public MapEntity() {
        this.mainTexture = null;
        this.collisionSize = new Vector2(0,0);
        this.imgOffset = new Vector2(0,0);
        this.isClicked = false;
        this.counter = 0;
        this.textures = new ArrayList<Texture>();
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
        //TODO: make pop-up window appear with details about the building.
    }

    public Vector2 getCoords() {
        return new Vector2(clickX, clickY);
    }

    public Texture getMainTexture() {
        return mainTexture;
    }

    public void setMainTexture(Texture mainTexture) {
        this.mainTexture = mainTexture;
        sliceTexture(mainTexture);
    }

    /**
     * Switch to alternative textures, if they exist.
     */
    public void changeTexture() {
        if (textures.size() > 0) {
            counter = (counter + 1) % textures.size();
            mainTexture = textures.get(counter);
            sliceTexture(mainTexture);
        }
    }

    public Vector2 getCollisionSize() {
        return collisionSize;
    }

    /**
     * Splits the texture based on the size of the building.
     * @param mainTexture the building's texture
     */
    protected void sliceTexture(Texture mainTexture) {
        this.tiles = new ExtendedStaticTiledMapTile[(int)collisionSize.x][(int)collisionSize.y];
        this.prevCells = new TiledMapTileLayer.Cell[(int)collisionSize.x][(int)collisionSize.y];

        this.mainTexture = mainTexture;
        if (mainTexture != null) {
            TextureRegion tex = new TextureRegion(mainTexture);

            int TILE_HEIGHT = Utils.TILE_SIZE/2;
            for (int y = 0; y < collisionSize.y; y++) {
                for (int x = 0; x < collisionSize.x; x++) {
                    float cY = (collisionSize.x - 1 + (y - x)) * (TILE_HEIGHT/2);
                    float cX = (x + y) * Utils.TILE_SIZE/2;
                    TextureRegion current = new TextureRegion(tex, (int)cX, 0, Utils.TILE_SIZE, tex.getRegionHeight() - (int)cY);
                    ExtendedStaticTiledMapTile tile = new ExtendedStaticTiledMapTile(current);
                    tiles[x][y] = tile;
                }
            }
        }
    }

    /**
     * Places the Entity onto the given layer at the specified coordinates
     * @param layer the layer onto which to put the entity
     * @param clickX tile coordinate
     * @param clickY tile coordinate
     */
    public void placeOnLayer(TiledMapTileLayer layer, int clickX, int clickY) {
        this.layer = layer;
        this.clickX = clickX;
        this.clickY = clickY;

        for (int y = 0; y < collisionSize.y; y++) {
            for (int x = 0; x < collisionSize.x; x++) {
                prevCells[x][y] = layer.getCell(clickX + x, clickY + y);
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                tiles[x][y].setObstacle(true);
                tiles[x][y].setObject(this);
                cell.setTile(tiles[x][y]);
                layer.setCell(clickX + x, clickY + y, cell);
            }
        }
    }

    /**
     * Resets the tiles to their previous state
     */
    public void resetTiles() {
        for (int y = 0; y < collisionSize.y; y++) {
            for (int x = 0; x < collisionSize.x; x++) {
                layer.setCell(clickX + x, clickY + y, prevCells[x][y]);
            }
        }
    }



    @Override
    public void dispose() {
        mainTexture.dispose();
    }
}
