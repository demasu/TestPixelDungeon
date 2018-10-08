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

import com.demasu.testpixeldungeon.Dungeon;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class LeafParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory GENERAL = new Factory() {
        @Override
        public void emit ( Emitter emitter, int index, float x, float y ) {
            LeafParticle p = ( (LeafParticle) emitter.recycle( LeafParticle.class ) );
            p.color( ColorMath.random( 0x004400, 0x88CC44 ) );
            p.reset( x, y );
        }
    };
    public static final Emitter.Factory LEVEL_SPECIFIC = new Factory() {
        @Override
        public void emit ( Emitter emitter, int index, float x, float y ) {
            LeafParticle p = ( (LeafParticle) emitter.recycle( LeafParticle.class ) );
            p.color( ColorMath.random( Dungeon.getLevel().color1, Dungeon.getLevel().color2 ) );
            p.reset( x, y );
        }
    };
    public static int color1;
    public static int color2;

    public LeafParticle () {
        super();

        setLifespan( 1.2f );
        getAcc().set( 0, 25 );
    }

    public void reset ( float x, float y ) {
        revive();

        this.setX( x );
        this.setY( y );

        getSpeed().set( Random.Float( -8, +8 ), -20 );

        setLeft( getLifespan() );
        setSize( Random.Float( 2, 3 ) );
    }
}
