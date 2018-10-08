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
package com.demasu.testpixeldungeon.effects;

import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.ui.Icons;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class EmoIcon extends Image {

    protected float maxSize = 2;
    protected float timeScale = 1;

    protected boolean growing = true;

    protected CharSprite owner;

    public EmoIcon ( CharSprite owner ) {
        super();

        this.owner = owner;
        GameScene.add( this );
    }

    @Override
    public void update () {
        super.update();

        if ( getVisible() ) {
            if ( growing ) {
                getScale().set( getScale().x + Game.getElapsed() * timeScale );
                if ( getScale().x > maxSize ) {
                    growing = false;
                }
            } else {
                getScale().set( getScale().x - Game.getElapsed() * timeScale );
                if ( getScale().x < 1 ) {
                    growing = true;
                }
            }

            setX( owner.getX() + owner.getWidth() - getWidth() / 2 );
            setY( owner.getY() - getHeight() );
        }
    }

    public static class Sleep extends EmoIcon {

        public Sleep ( CharSprite owner ) {

            super( owner );

            copy( Icons.get( Icons.SLEEP ) );

            maxSize = 1.2f;
            timeScale = 0.5f;

            getOrigin().set( getWidth() / 2, getHeight() / 2 );
            getScale().set( Random.Float( 1, maxSize ) );
        }
    }

    public static class Alert extends EmoIcon {

        public Alert ( CharSprite owner ) {

            super( owner );

            copy( Icons.get( Icons.ALERT ) );

            maxSize = 1.3f;
            timeScale = 2;

            getOrigin().set( 2.5f, getHeight() - 2.5f );
            getScale().set( Random.Float( 1, maxSize ) );
        }
    }

}
