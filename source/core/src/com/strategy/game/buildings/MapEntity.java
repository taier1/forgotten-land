package com.strategy.game.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.strategy.game.Assets;
import com.strategy.game.ExtendedStaticTiledMapTile;
import com.strategy.game.Utils;
import com.strategy.game.world.World;

import java.util.ArrayList;

/**
 * A generic object to put on the map.
 */
public abstract class MapEntity implements Disposable{
    protected Vector2 collisionSize;
    protected Texture mainTexture;
    private boolean isClicked;
    private int clickX, clickY;
    protected Vector2 imgOffset; //TODO: useless?
    private TiledMapTileLayer layer;
    protected ArrayList<Texture> textures;
    private int counter;
    protected int influenceRadius;

    private Vector2 position;

    private ExtendedStaticTiledMapTile[][] tiles;
    private TiledMapTileLayer.Cell[][] prevCells;
    private TiledMapTileLayer.Cell[][] prevCellsInfluence;


    public MapEntity() {
        this.mainTexture = null;
        this.collisionSize = new Vector2(0,0);
        this.imgOffset = new Vector2(0,0);
        this.isClicked = false;
        this.counter = 0;
        this.textures = new ArrayList<Texture>();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
        //TODO: make pop-up window appear with details about the building.
    }

    public int getInfluenceRadius() {
        return influenceRadius;
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
        // TODO: 13/05/2016 Set dynamically
        this.prevCellsInfluence = new TiledMapTileLayer.Cell[1000][1000];

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
     * Removes this building from the game.
     */
//    public void destroy() {
//        System.out.println("Destroyed");
////        MapEntity entity = getEntityAt(x, y);
//        world.getStaticEntities().remove(entity);
////
////        if (entity != null) {
////            Vector2 coords = entity.getCoords();
////            for (int i = (int)coords.x; i < (int)coords.x + entity.collisionSize.x; i++) {
////                for (int j = (int)coords.y; j < (int)coords.y + entity.collisionSize.y; j++) {
////                    TiledMapTileLayer.Cell buildingsCell = buildingsLayer.getCell(i,j);
////                    ExtendedStaticTiledMapTile buildingTile = (ExtendedStaticTiledMapTile) buildingsCell.getTile();
////                    buildingTile.setTextureRegion(new TextureRegion(Assets.emptyTile));
////                    buildingTile.setObject(null);
////
////                    buildingsCell.setTile(buildingTile);
////                    buildingsLayer.setCell(i,j,buildingsCell);
////                }
////            }
////
////
//////            // Set influence radius
//////            int startX = x - entity.getInfluenceRadius();
//////            int startY = y - entity.getInfluenceRadius();
//////            int endY = y + entity.getInfluenceRadius() + (int) entity.collisionSize.y;
//////            int endX = x + entity.getInfluenceRadius() + (int) entity.collisionSize.x;
//////
//////            // TODO: 12/05/2016 Add also for upper limits
//////            if (startX < 0) startX = 0;
//////            if (startY < 0) startY = 0;
//////
//////            for (int j = startY; j < endY; j++) {
//////                for (int i = startX; i < endX; i++) {
//////                    TiledMapTileLayer.Cell cell = buildingsLayer.getCell(i, j);
//////                    if (cell != null) {
//////                        ExtendedStaticTiledMapTile tile = (ExtendedStaticTiledMapTile) cell.getTile();
//////                        tile.decBuildingsNearby();
//////
//////
//////                        cell.setTile(tile);
//////                        buildingsLayer.setCell(i, j, cell);
//////                    }
//////                }
//////            }
////        }
////
//////        TiledMapTileLayer.Cell buildingsCell = buildingsLayer.getCell(x,y);
//////        if (buildingsCell != null) {
//////            TiledMapTile tile = buildingsCell.getTile();
//////            if (tile instanceof ExtendedStaticTiledMapTile) {
//////                tile.setTextureRegion(new TextureRegion(Assets.redTile));
//////                buildingsCell.setTile(tile);
//////                buildingsLayer.setCell(x, y, buildingsCell);
//////            }
//////        }
////
////        System.out.println("Destroyed!");
////        System.out.println(getEntityAt(x, y));
//
//    }


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

        // Set collision on cells
        for (int y = 0; y < collisionSize.y; y++) {
            for (int x = 0; x < collisionSize.x; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(clickX + x, clickY + y);
                prevCells[x][y] = cell;
                if (cell == null)
                    cell = new TiledMapTileLayer.Cell();
                tiles[x][y].setObstacle(true);
                tiles[x][y].setObject(this);
                cell.setTile(tiles[x][y]);
                layer.setCell(clickX + x, clickY + y, cell);
            }
        }

        // Set influence radius
        int startY = clickY - influenceRadius;
        int startX = clickX - influenceRadius;
        int endY = clickY + influenceRadius + (int) collisionSize.y;
        int endX = clickX + influenceRadius + (int) collisionSize.x;

        // TODO: 12/05/2016 Add also for upper limits
        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;

        // Creates the influence area
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                prevCellsInfluence[x][y] = cell;

                if (cell == null) {
                    cell = new TiledMapTileLayer.Cell();
                }

                TextureRegion texture;
                //TODO: remove the first condition (temporarily left there)
                if (layer.getName().equals("Selection") && cell.getTile() != null) {
//                    texture = new TextureRegion(Assets.redTile);
                    texture = cell.getTile().getTextureRegion();
                } else if (cell.getTile() != null) {
                    texture = cell.getTile().getTextureRegion();
                } else {
                    texture = new TextureRegion(Assets.emptyTile);
                }

                TiledMapTile tile;
                tile = cell.getTile();
                if (!(tile instanceof ExtendedStaticTiledMapTile)) {
                    if (tile == null) tile = new ExtendedStaticTiledMapTile(texture);
                    else    tile = new ExtendedStaticTiledMapTile((StaticTiledMapTile) tile);
                }

                ((ExtendedStaticTiledMapTile) tile).incBuildingsNearby();
                cell.setTile(tile);
                layer.setCell(x, y, cell);
            }
        }
    }

    /**
     * Resets the tiles to their previous state
     */
    public void resetTiles() {
        int startY = clickY - influenceRadius;
        int startX = clickX - influenceRadius;
        int endY = clickY + influenceRadius + (int) collisionSize.y;
        int endX = clickX + influenceRadius + (int) collisionSize.x;

        // TODO: 12/05/2016 Add also for upper limits
        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                layer.setCell(x, y, prevCellsInfluence[x][y]);
            }
        }

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
