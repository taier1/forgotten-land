package com.strategy.game;

import com.badlogic.gdx.math.Vector2;

/**
 * A type of MapEntity
 */
public class LeftWall extends MapEntity {
    public LeftWall() {
        super();
        this.mainTexture = Assets.leftwall;
        this.collisionSize = new Vector2(1,1);
//        this.imgOffset = new Vector2(0,1);
        sliceTexture(mainTexture);
    }
}
