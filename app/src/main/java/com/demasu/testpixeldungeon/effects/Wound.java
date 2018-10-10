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

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.DungeonTilemap;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.levels.Level;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;

public class Wound extends Image {

    private static final float TIME_TO_FADE = 0.8f;

    private float time;

    public Wound () {
        super( Effects.get( Effects.Type.WOUND ) );
        getOrigin().set( getWidth() / 2, getHeight() / 2 );
    }

    public static void hit ( Char ch ) {
        hit( ch, 0 );
    }

    public static void hit ( Char ch, float angle ) {
        Wound w = (Wound) ch.sprite.getParent().recycle( Wound.class );
        ch.sprite.getParent().bringToFront( w );
        w.reset( ch.pos );
        w.setAngle( angle );
    }

    public static void hit ( int pos ) {
        hit( pos, 0 );
    }

    public static void hit ( int pos, float angle ) {
        Group parent = Dungeon.getHero().sprite.getParent();
        Wound w = (Wound) parent.recycle( Wound.class );
        parent.bringToFront( w );
        w.reset( pos );
        w.setAngle( angle );
    }

    public void reset ( int p ) {
        revive();

        setX( ( p % Level.WIDTH ) * DungeonTilemap.SIZE + ( DungeonTilemap.SIZE - getWidth() ) / 2 );
        setY( ( p / Level.WIDTH ) * DungeonTilemap.SIZE + ( DungeonTilemap.SIZE - getHeight() ) / 2 );

        time = TIME_TO_FADE;
    }

    @Override
    public void update () {
        super.update();

        if ( ( time -= Game.getElapsed() ) <= 0 ) {
            kill();
        } else {
            float p = time / TIME_TO_FADE;
            alpha( p );
            getScale().setX( 1 + p );
        }
    }
}
