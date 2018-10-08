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

import android.opengl.GLES20;

import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.Game;

import javax.microedition.khronos.opengles.GL10;

public class ArcherMaidenHalo extends Halo {

    static final int RED = 0xb70202;
    static final int YELLOW = 0xe4ff00;
    static final int BLUE = 0x2c04ac;
    static final int WHITE = 0xffffff;
    static final int BLACK = 0x000000;
    private CharSprite target;
    private float phase = 0;


    public ArcherMaidenHalo ( CharSprite sprite ) {
        super( 15, WHITE, 0.17f );
        target = sprite;
        setAm( 0 );
    }


    @Override
    public void update () {
        super.update();

        if ( phase < 0 ) {
            if ( ( phase += Game.getElapsed() ) >= 0 ) {
                killAndErase();
            } else {
                getScale().set( ( 2 + phase ) * radius / RADIUS );
                setAm( -phase * brightness );
            }
        } else if ( phase < 1 ) {
            if ( ( phase += Game.getElapsed() ) >= 1 ) {
                phase = 1;
            }
            getScale().set( phase * radius / RADIUS );
            setAm( phase * brightness );
        }

        point( target.getX() + target.getWidth() / 2, target.getY() + target.getHeight() / 2 );
    }

    @Override
    public void draw () {
        GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
        super.draw();
        GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
    }

    public void putOut () {
        phase = -1;
    }
}
