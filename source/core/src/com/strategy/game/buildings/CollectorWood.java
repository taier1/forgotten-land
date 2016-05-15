package com.strategy.game.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.strategy.game.Assets;
import com.strategy.game.ResourceContainer;

/**
 * Created by batta on 28/04/16.
 */
public class CollectorWood extends Building {
    private static final String NAME = "Lumber camp";
    private static final ResourceContainer COST = new ResourceContainer(40, 0, 10, 5, 0);
    private static final ResourceContainer PRODUCTION = new ResourceContainer(10, 0, 0, 0, 0);
    private static final ResourceContainer MAINTENANCE = new ResourceContainer(0, 0, 1, 1, 0);
    private static final int MAX_LIFE = 10;
    private static final int MAX_WORKERS = 5;
    private static final int INFLUENCE_RADIUS = 5;
    private static final Texture TEXTURE = Assets.house1;
    private static final Vector2 COLLISION = new Vector2(2,2);
    private int life = MAX_LIFE;
    private int workers = 0;

    public CollectorWood() {
        super(NAME, COST, PRODUCTION, MAINTENANCE, MAX_LIFE, MAX_WORKERS,INFLUENCE_RADIUS, TEXTURE, COLLISION);
    }
}