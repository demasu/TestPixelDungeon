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

public class WindParticle extends PixelParticle {

    private static float angle = Random.Float( PointF.PI2 );
    private static PointF speed = new PointF().polar( angle, 5 );
    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit ( Emitter emitter, int index, float x, float y ) {
            ( (WindParticle) emitter.recycle( WindParticle.class ) ).reset( x, y );
        }
    };
    private float size;

    public WindParticle () {
        super();

        setLifespan( Random.Float( 1, 2 ) );
        scale.set( size = Random.Float( 3 ) );
    }

    public void reset ( float x, float y ) {
        revive();

        setLeft( getLifespan() );

        super.speed.set( WindParticle.speed );
        super.speed.scale( size );

        this.x = x - super.speed.x * getLifespan() / 2;
        this.y = y - super.speed.y * getLifespan() / 2;

        angle += Random.Float( -0.1f, +0.1f );
        speed = new PointF().polar( angle, 5 );

        am = 0;
    }

    @Override
    public void update () {
        super.update();

        float p = getLeft() / getLifespan();
        am = ( p < 0.5f ? p : 1 - p ) * size * 0.2f;
    }

    public static class Wind extends Group {

        private int pos;

        private float x;
        private float y;

        private float delay;

        public Wind ( int pos ) {
            super();

            this.pos = pos;
            PointF p = DungeonTilemap.tileToWorld( pos );
            x = p.x;
            y = p.y;

            delay = Random.Float( 5 );
        }

        @Override
        public void update () {

            if ( visible = Dungeon.getVisible()[pos] ) {

                super.update();

                if ( ( delay -= Game.getElapsed() ) <= 0 ) {

                    delay = Random.Float( 5 );

                    ( (WindParticle) recycle( WindParticle.class ) ).reset(
                            x + Random.Float( DungeonTilemap.SIZE ),
                            y + Random.Float( DungeonTilemap.SIZE ) );
                }
            }
        }
    }
}
