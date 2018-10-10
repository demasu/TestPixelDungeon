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
import com.watabou.utils.Random;

public class WebParticle extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit ( Emitter emitter, int index, float x, float y ) {
            for ( int i = 0; i < 3; i++ ) {
                ( (WebParticle) emitter.recycle( WebParticle.class ) ).reset( x, y );
            }
        }
    };

    public WebParticle () {
        super();

        color( 0xCCCCCC );
        setLifespan( 2f );
    }

    public void reset ( float x, float y ) {
        revive();

        this.setX( x );
        this.setY( y );

        setLeft( getLifespan() );
        setAngle( Random.Float( 360 ) );
    }

    @Override
    public void update () {
        super.update();

        float p = getLeft() / getLifespan();
        setAm( p < 0.5f ? p : 1 - p );
        getScale().setY( 16 + p * 8 );
    }
}
