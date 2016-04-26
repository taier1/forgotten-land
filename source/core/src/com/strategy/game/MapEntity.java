package com.strategy.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

/**
 * A generic object to put on the map. Inherit from this to create new buildings.
 */
public class MapEntity implements Disposable{
    private int collisionSize;
    private boolean isSelected;
    private Texture texture;
    private ArrayList<StaticTiledMapTile> tiles;
    private int x,y;
    private TiledMapTileLayer layer;
    private ArrayList<TiledMapTileLayer.Cell> prevCells;

    public MapEntity(Texture texture) {
        this.texture = texture;
        this.tiles = new ArrayList<StaticTiledMapTile>();
        this.prevCells = new ArrayList<TiledMapTileLayer.Cell>();
        this.isSelected = true;
        sliceTexture();
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<StaticTiledMapTile> getTiles() {
        return tiles;
    }

    // Slices the texture into vertical slices of tile size
    private void sliceTexture() {
        TextureRegion tex = new TextureRegion(texture);
        TextureRegion[][] arr = tex.split(Utils.TILE_SIZE, tex.getRegionHeight());

        for (int i = 0; i < tex.getRegionWidth()/Utils.TILE_SIZE; i++) {
            StaticTiledMapTile tile = new StaticTiledMapTile(arr[0][i]);
            tiles.add(tile);
        }
    }

    public void placeOnLayer(TiledMapTileLayer layer, int x, int y) {
//        System.out.println("Placed");
        int offset = 0;
        this.layer = layer;
        this.x = x;
        this.y = y;
//        prevTiles = new ArrayList<StaticTiledMapTile>();
//        prevCells = new ArrayList<TiledMapTileLayer.Cell>(); // saves previous state

//        for ()
//        oldLayer.
        for (StaticTiledMapTile tile :
                tiles) {
            prevCells.add(layer.getCell(x + offset, y + offset)); // save previous state
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tile);
            layer.setCell(x + offset, y + offset, cell);
            offset++;
        }
    }

    public void resetTiles() {
        if (isSelected) {
            int offset = 0;
            for (TiledMapTileLayer.Cell cell :
                    prevCells) {
                layer.setCell(x + offset, y + offset, cell);
                offset++;
            }
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
