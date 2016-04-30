package com.strategy.game.buildings;

import com.badlogic.gdx.math.Vector2;
import com.strategy.game.Assets;

/**
 * A type of MapEntity
 */
public class MiddleWall extends MapEntity {
    public MiddleWall() {
        super();
        this.mainTexture = Assets.middlewall;
        this.collisionSize = new Vector2(1,1);
//        this.imgOffset = new Vector2(0,1);
        sliceTexture(mainTexture);
    }
}