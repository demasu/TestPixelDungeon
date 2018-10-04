/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.demasu.testpixeldungeon.ui;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;

public class DangerIndicator extends Tag {

    public static final int COLOR = 0xFF4C4C;

    private BitmapText number;
    private Image icon;

    private int enemyIndex = 0;

    private int lastNumber = -1;

    public DangerIndicator () {
        super( 0xFF4C4C );

        setSize( 24, 16 );

        setVisible( false );
    }

    @Override
    protected void createChildren () {
        super.createChildren();

        number = new BitmapText( PixelScene.font1x );
        add( number );

        icon = Icons.SKULL.get();
        add( icon );
    }

    @Override
    protected void layout () {
        super.layout();

        icon.x = right() - 10;
        icon.y = getY() + ( getHeight() - icon.height ) / 2;

        placeNumber();
    }

    private void placeNumber () {
        number.x = right() - 11 - number.width();
        number.y = PixelScene.align( getY() + ( getHeight() - number.baseLine() ) / 2 );
    }

    @Override
    public void update () {

        if ( Dungeon.getHero().isAlive() ) {
            int v = Dungeon.getHero().visibleEnemies();
            if ( v != lastNumber ) {
                lastNumber = v;
                setVisible( lastNumber > 0 );
                if ( getVisible() ) {
                    number.text( Integer.toString( lastNumber ) );
                    number.measure();
                    placeNumber();

                    flash();
                }
            }
        } else {
            setVisible( false );
        }

        super.update();
    }

    @Override
    protected void onClick () {

        Mob target = Dungeon.getHero().visibleEnemy( enemyIndex++ );

        HealthIndicator.instance.target( target == HealthIndicator.instance.target() ? null : target );

        Camera.getMain().setTarget( null );
        Camera.getMain().focusOn( target.sprite );
    }
}
