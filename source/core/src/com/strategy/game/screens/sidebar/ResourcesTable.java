package com.strategy.game.screens.sidebar;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.strategy.game.Assets;
import com.strategy.game.ResourceContainer;
import com.strategy.game.Utils;

/**
 * Created by Amedeo on 21/05/16.
 */
public class ResourcesTable extends Table {

    private static final int RESOURCES_NUMBER = 5;
    private BitmapFont default_font;
    private Actor[][] table;

    public ResourcesTable(int columnsNumber, BitmapFont font) {
        this.default_font = font;

        table = new Actor[RESOURCES_NUMBER + 1][columnsNumber + 1];

        table[1][0] = Assets.makeImage(Assets.resourcesFood);
        table[2][0] = Assets.makeImage(Assets.resourcesWood);
        table[3][0] = Assets.makeImage(Assets.resourcesGold);
        table[4][0] = Assets.makeImage(Assets.resourcesRock);
        table[5][0] = Assets.makeImage(Assets.resourcesPeople);

        for (int y = 0; y < table.length; y++) {
            for (int x = 0; x < table[y].length; x++) {
                if (x == 0) {
                    add(table[y][x]);
                }
                else {
                    table[y][x] = Assets.makeLabel("", font);
                    add(table[y][x]).uniform().expand();
                }

            }
            row();
        }
    }

    public void set(String[] titles, int[][] values) {
        for (int i = 0; i < titles.length; i++) {
            ((Label) table[0][i+1]).setText(titles[i]);
        }
        set(values);
    }

    public void set(int[][] values) {
        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                ((Label) table[y+1][x+1]).setText(Integer.toString(values[x][y]));
            }
        }
    }

    public void set(ResourceContainer[] resourceContainers) {
        for (int i = 0; i < resourceContainers.length; i++) {
            if (resourceContainers[i].food < 0)
                ((Label) table[1][i+1]).setStyle(Assets.makeLabelStyle(Utils.FONT_SMALL_RED));
            else
                ((Label) table[1][i+1]).setStyle(Assets.makeLabelStyle(default_font));
            ((Label) table[1][i+1]).setText(Integer.toString(resourceContainers[i].food));

            if (resourceContainers[i].wood < 0)
                ((Label) table[2][i+1]).setStyle(Assets.makeLabelStyle(Utils.FONT_SMALL_RED));
            else
                ((Label) table[2][i+1]).setStyle(Assets.makeLabelStyle(default_font));
            ((Label) table[2][i+1]).setText(Integer.toString(resourceContainers[i].wood));

            if (resourceContainers[i].rock < 0)
                ((Label) table[3][i+1]).setStyle(Assets.makeLabelStyle(Utils.FONT_SMALL_RED));
            else
                ((Label) table[3][i+1]).setStyle(Assets.makeLabelStyle(default_font));
            ((Label) table[3][i+1]).setText(Integer.toString(resourceContainers[i].rock));

            if (resourceContainers[i].gold < 0)
                ((Label) table[4][i+1]).setStyle(Assets.makeLabelStyle(Utils.FONT_SMALL_RED));
            else
                ((Label) table[4][i+1]).setStyle(Assets.makeLabelStyle(default_font));
            ((Label) table[4][i+1]).setText(Integer.toString(resourceContainers[i].gold));

            if (resourceContainers[i].people < 0)
                ((Label) table[5][i+1]).setStyle(Assets.makeLabelStyle(Utils.FONT_SMALL_RED));
            else
                ((Label) table[5][i+1]).setStyle(Assets.makeLabelStyle(default_font));
            ((Label) table[5][i+1]).setText(Integer.toString(resourceContainers[i].people));
        }
    }
}
