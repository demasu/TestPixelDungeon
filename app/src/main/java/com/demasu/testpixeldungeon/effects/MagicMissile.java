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

import com.demasu.testpixeldungeon.DungeonTilemap;
import com.demasu.testpixeldungeon.effects.particles.FlameParticle;
import com.demasu.testpixeldungeon.effects.particles.LeafParticle;
import com.demasu.testpixeldungeon.effects.particles.PoisonParticle;
import com.demasu.testpixeldungeon.effects.particles.PurpleParticle;
import com.demasu.testpixeldungeon.effects.particles.ShadowParticle;
import com.demasu.testpixeldungeon.effects.particles.WoolParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.noosa.particles.PixelParticle.Shrinking;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class MagicMissile extends Emitter {

    private static final float SPEED = 200f;

    private Callback callback;

    private float sx;
    private float sy;
    private float time;

    public static void blueLight ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.pour( MagicParticle.FACTORY, 0.01f );
    }

    public static void fire ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 4 );
        missile.pour( FlameParticle.FACTORY, 0.01f );
    }

    public static void earth ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 2 );
        missile.pour( EarthParticle.FACTORY, 0.01f );
    }

    public static void purpleLight ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 2 );
        missile.pour( PurpleParticle.MISSILE, 0.01f );
    }

    public static void whiteLight ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 4 );
        missile.pour( WhiteParticle.FACTORY, 0.01f );
    }

    public static void wool ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 3 );
        missile.pour( WoolParticle.FACTORY, 0.01f );
    }

    public static void poison ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 3 );
        missile.pour( PoisonParticle.MISSILE, 0.01f );
    }

    public static void foliage ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 4 );
        missile.pour( LeafParticle.GENERAL, 0.01f );
    }

    public static void slowness ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.pour( SlowParticle.FACTORY, 0.01f );
    }

    public static void force ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 0 );
        missile.pour( ForceParticle.FACTORY, 0.01f );
    }

    public static void coldLight ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 4 );
        missile.pour( ColdParticle.FACTORY, 0.01f );
    }

    public static void shadow ( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( 4 );
        missile.pour( ShadowParticle.MISSILE, 0.01f );
    }

    public static void shadow ( Group group, int from, int to, Callback callback, int size ) {
        MagicMissile missile = ( (MagicMissile) group.recycle( MagicMissile.class ) );
        missile.reset( from, to, callback );
        missile.size( size );
        missile.pour( ShadowParticle.MISSILE, 0.01f );
    }

    public void reset ( int from, int to, Callback callback ) {
        reset( from, to, SPEED, callback );
    }

    public void reset ( int from, int to, float velocity, Callback callback ) {
        this.callback = callback;

        revive();

        PointF pf = DungeonTilemap.tileCenterToWorld( from );
        PointF pt = DungeonTilemap.tileCenterToWorld( to );

        setX( pf.x );
        setY( pf.y );
        setWidth( 0 );
        setHeight( 0 );

        PointF d = PointF.diff( pt, pf );
        PointF speed = new PointF( d ).normalize().scale( velocity );
        sx = speed.x;
        sy = speed.y;
        time = d.length() / velocity;
    }

    public void size ( float size ) {
        setX( getX() - size / 2 );
        setY( getY() - size / 2 );
        setWidth( size );
        setHeight( size );
    }

    @Override
    public void update () {
        super.update();
        if ( isOn() ) {
            float d = Game.getElapsed();
            setX( getX() + sx * d );
            setY( getY() + sy * d );
            if ( ( time -= d ) <= 0 ) {
                setOn( false );
                callback.call();
            }
        }
    }

    public static class MagicParticle extends PixelParticle {

        public static final Emitter.Factory FACTORY = new Factory() {
            @Override
            public void emit ( Emitter emitter, int index, float x, float y ) {
                ( (MagicParticle) emitter.recycle( MagicParticle.class ) ).reset( x, y );
            }

            @Override
            public boolean lightMode () {
                return true;
            }

        };

        public MagicParticle () {
            super();

            color( 0x88CCFF );
            setLifespan( 0.5f );

            getSpeed().set( Random.Float( -10, +10 ), Random.Float( -10, +10 ) );
        }

        public void reset ( float x, float y ) {
            revive();

            this.setX( x );
            this.setY( y );

            setLeft( getLifespan() );
        }

        @Override
        public void update () {
            super.update();
            // alpha: 1 -> 0; size: 1 -> 4
            setAm( getLeft() / getLifespan() );
            size( 4 - ( getAm() ) * 3 );
        }
    }

    public static class EarthParticle extends PixelParticle.Shrinking {

        public static final Emitter.Factory FACTORY = new Factory() {
            @Override
            public void emit ( Emitter emitter, int index, float x, float y ) {
                ( (EarthParticle) emitter.recycle( EarthParticle.class ) ).reset( x, y );
            }
        };

        public EarthParticle () {
            super();

            setLifespan( 0.5f );

            color( ColorMath.random( 0x555555, 0x777766 ) );

            getAcc().set( 0, +40 );
        }

        public void reset ( float x, float y ) {
            revive();

            this.setX( x );
            this.setY( y );

            setLeft( getLifespan() );
            setSize( 4 );

            getSpeed().set( Random.Float( -10, +10 ), Random.Float( -10, +10 ) );
        }
    }

    public static class WhiteParticle extends PixelParticle {

        public static final Emitter.Factory FACTORY = new Factory() {
            @Override
            public void emit ( Emitter emitter, int index, float x, float y ) {
                ( (WhiteParticle) emitter.recycle( WhiteParticle.class ) ).reset( x, y );
            }

            @Override
            public boolean lightMode () {
                return true;
            }

        };

        public WhiteParticle () {
            super();

            setLifespan( 0.4f );

            setAm( 0.5f );
        }

        public void reset ( float x, float y ) {
            revive();

            this.setX( x );
            this.setY( y );

            setLeft( getLifespan() );
        }

        @Override
        public void update () {
            super.update();
            // size: 3 -> 0
            size( ( getLeft() / getLifespan() ) * 3 );
        }
    }

    public static class SlowParticle extends PixelParticle {

        public static final Emitter.Factory FACTORY = new Factory() {
            @Override
            public void emit ( Emitter emitter, int index, float x, float y ) {
                ( (SlowParticle) emitter.recycle( SlowParticle.class ) ).reset( x, y, emitter );
            }

            @Override
            public boolean lightMode () {
                return true;
            }

        };
        private Emitter emitter;

        public SlowParticle () {
            super();

            setLifespan( 0.6f );

            color( 0x664422 );
            size( 2 );
        }

        public void reset ( float x, float y, Emitter emitter ) {
            revive();

            this.setX( x );
            this.setY( y );
            this.emitter = emitter;

            setLeft( getLifespan() );

            getAcc().set( 0 );
            getSpeed().set( Random.Float( -20, +20 ), Random.Float( -20, +20 ) );
        }

        @Override
        public void update () {
            super.update();

            setAm( getLeft() / getLifespan() );
            getAcc().set( ( emitter.getX() - getX() ) * 10, ( emitter.getY() - getY() ) * 10 );
        }
    }

    public static class ForceParticle extends Shrinking {

        public static final Emitter.Factory FACTORY = new Factory() {
            @Override
            public void emit ( Emitter emitter, int index, float x, float y ) {
                ( (ForceParticle) emitter.recycle( ForceParticle.class ) ).reset( index, x, y );
            }
        };

        public void reset ( int index, float x, float y ) {
            super.reset( x, y, 0xFFFFFF, 8, 0.5f );

            getSpeed().polar( PointF.PI2 / 8 * index, 12 );
            this.setX( this.getX() - getSpeed().x * getLifespan() );
            this.setY( this.getY() - getSpeed().y * getLifespan() );
        }

        @Override
        public void update () {
            super.update();

            setAm( ( 1 - getLeft() / getLifespan() ) / 2 );
        }
    }

    public static class ColdParticle extends PixelParticle.Shrinking {

        public static final Emitter.Factory FACTORY = new Factory() {
            @Override
            public void emit ( Emitter emitter, int index, float x, float y ) {
                ( (ColdParticle) emitter.recycle( ColdParticle.class ) ).reset( x, y );
            }

            @Override
            public boolean lightMode () {
                return true;
            }

        };

        public ColdParticle () {
            super();

            setLifespan( 0.6f );

            color( 0x2244FF );
        }

        public void reset ( float x, float y ) {
            revive();

            this.setX( x );
            this.setY( y );

            setLeft( getLifespan() );
            setSize( 8 );
        }

        @Override
        public void update () {
            super.update();

            setAm( 1 - getLeft() / getLifespan() );
        }
    }
}
