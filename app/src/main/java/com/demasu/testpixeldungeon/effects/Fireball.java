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

import android.graphics.RectF;
import android.opengl.GLES20;

import com.demasu.testpixeldungeon.Assets;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

import javax.microedition.khronos.opengles.GL10;

public class Fireball extends Component {

    private static final RectF BLIGHT = new RectF( 0, 0, 0.25f, 1 );
    private static final RectF FLIGHT = new RectF( 0.25f, 0, 0.5f, 1 );
    private static final RectF FLAME1 = new RectF( 0.50f, 0, 0.75f, 1 );
    private static final RectF FLAME2 = new RectF( 0.75f, 0, 1.00f, 1 );

    private static final int COLOR = 0xFF66FF;

    private Image bLight;
    private Image fLight;
    private Emitter emitter;
    private Group sparks;

    @Override
    protected void createChildren () {

        sparks = new Group();
        add( sparks );

        bLight = new Image( Assets.FIREBALL );
        bLight.frame( BLIGHT );
        bLight.getOrigin().set( bLight.getWidth() / 2 );
        bLight.setAngularSpeed( -90 );
        add( bLight );

        emitter = new Emitter();
        emitter.pour( new Emitter.Factory() {
            @Override
            public void emit ( Emitter emitter, int index, float x, float y ) {
                Flame p = (Flame) emitter.recycle( Flame.class );
                p.reset();
                p.setX( x - p.getWidth() / 2 );
                p.setY( y - p.getHeight() / 2 );
            }
        }, 0.1f );
        add( emitter );

        fLight = new Image( Assets.FIREBALL );
        fLight.frame( FLIGHT );
        fLight.getOrigin().set( fLight.getWidth() / 2 );
        fLight.setAngularSpeed( 360 );
        add( fLight );

        bLight.getTexture().filter( Texture.LINEAR, Texture.LINEAR );
    }

    @Override
    protected void layout () {

        bLight.setX( getX() - bLight.getWidth() / 2 );
        bLight.setY( getY() - bLight.getHeight() / 2 );

        emitter.pos(
                getX() - bLight.getWidth() / 4,
                getY() - bLight.getHeight() / 4,
                bLight.getWidth() / 2,
                bLight.getHeight() / 2 );

        fLight.setX( getX() - fLight.getWidth() / 2 );
        fLight.setY( getY() - fLight.getHeight() / 2 );
    }

    @Override
    public void update () {

        super.update();

        if ( Random.Float() < Game.getElapsed() ) {
            PixelParticle spark = (PixelParticle) sparks.recycle( PixelParticle.Shrinking.class );
            spark.reset( getX(), getY(), ColorMath.random( COLOR, 0x66FF66 ), 2, Random.Float( 0.5f, 1.0f ) );
            spark.getSpeed().set(
                    Random.Float( -40, +40 ),
                    Random.Float( -60, +20 ) );
            spark.getAcc().set( 0, +80 );
            sparks.add( spark );
        }
    }

    @Override
    public void draw () {
        GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
        super.draw();
        GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
    }

    public static class Flame extends Image {

        private static float LIFESPAN = 1f;

        private static float SPEED = -40f;
        private static float ACC = -20f;

        private float timeLeft;

        public Flame () {

            super( Assets.FIREBALL );

            frame( Random.Int( 2 ) == 0 ? FLAME1 : FLAME2 );
            getOrigin().set( getWidth() / 2, getHeight() / 2 );
            getAcc().set( 0, ACC );
        }

        public void reset () {
            revive();
            timeLeft = LIFESPAN;
            getSpeed().set( 0, SPEED );
        }

        @Override
        public void update () {

            super.update();

            if ( ( timeLeft -= Game.getElapsed() ) <= 0 ) {

                kill();

            } else {

                float p = timeLeft / LIFESPAN;
                getScale().set( p );
                alpha( p > 0.8f ? ( 1 - p ) * 5f : p * 1.25f );

            }
        }
    }
}
