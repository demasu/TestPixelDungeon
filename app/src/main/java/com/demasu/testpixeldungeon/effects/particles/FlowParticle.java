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
import com.demasu.testpixeldungeon.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class FlowParticle extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit ( Emitter emitter, int index, float x, float y ) {
            ( (FlowParticle) emitter.recycle( FlowParticle.class ) ).reset( x, y );
        }
    };

    public FlowParticle () {
        super();

        setLifespan( 0.6f );
        getAcc().set( 0, 32 );
        setAngularSpeed( Random.Float( -360, +360 ) );
    }

    public void reset ( float x, float y ) {
        revive();

        setLeft( getLifespan() );

        this.setX( x );
        this.setY( y );

        setAm( 0 );
        size( 0 );
        getSpeed().set( 0 );
    }

    @Override
    public void update () {
        super.update();

        float p = getLeft() / getLifespan();
        setAm( ( p < 0.5f ? p : 1 - p ) * 0.6f );
        size( ( 1 - p ) * 4 );
    }

    public static class Flow extends Group {

        private static final float DELAY = 0.1f;

        private int pos;

        private float x;
        private float y;

        private float delay;

        public Flow ( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileToWorld( pos );
            x = p.getX();
            y = p.getY() + DungeonTilemap.SIZE - 1;

            delay = Random.Float( DELAY );
        }

        @Override
        public void update () {

            setVisible( Dungeon.getVisible()[pos] );
            if ( getVisible() ) {

                super.update();

                if ( ( delay -= Game.getElapsed() ) <= 0 ) {

                    delay = Random.Float( DELAY );

                    ( (FlowParticle) recycle( FlowParticle.class ) ).reset(
                            x + Random.Float( DungeonTilemap.SIZE ), y );
                }
            }
        }
    }
}
