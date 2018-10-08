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
package com.demasu.testpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class WoolParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit ( Emitter emitter, int index, float x, float y ) {
            ( (WoolParticle) emitter.recycle( WoolParticle.class ) ).reset( x, y );
        }
    };

    public WoolParticle () {
        super();

        color( ColorMath.random( 0x999999, 0xEEEEE0 ) );

        getAcc().set( 0, -40 );
    }

    public void reset ( float x, float y ) {
        revive();

        this.setX( x );
        this.setY( y );

        setLeft( Random.Float( 0.6f, 1f ) );
        setLifespan( Random.Float( 0.6f, 1f ) );
        setSize( 5 );

        getSpeed().set( Random.Float( -10, +10 ), Random.Float( -10, +10 ) );
    }
}
