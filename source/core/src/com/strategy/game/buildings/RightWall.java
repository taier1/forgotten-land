package com.strategy.game.buildings;

import com.badlogic.gdx.math.Vector2;
import com.strategy.game.Assets;

/**
 * A type of MapEntity
 */
public class RightWall extends MapEntity {
    public RightWall() {
        super();
        this.mainTexture = Assets.rightwall;
        this.collisionSize = new Vector2(1,1);
//        this.imgOffset = new Vector2(0,1);
        sliceTexture(mainTexture);
    }
}