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
package com.demasu.testpixeldungeon.levels;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.DungeonTilemap;
import com.demasu.testpixeldungeon.actors.mobs.npcs.Ghost;
import com.demasu.testpixeldungeon.items.DewVial;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class SewerLevel extends RegularLevel {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    public static void addVisuals ( Level level, Scene scene ) {
        for ( int i = 0; i < LENGTH; i++ ) {
            if ( level.map[i] == Terrain.WALL_DECO ) {
                scene.add( new Sink( i ) );
            }
        }
    }

    @Override
    public String tilesTex () {
        return Assets.TILES_SEWERS;
    }

    @Override
    public String waterTex () {
        return Assets.WATER_SEWERS;
    }

    protected boolean[] water () {
        return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 5 );
    }

    protected boolean[] grass () {
        return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 4 );
    }

    @Override
    protected void decorate () {

        for ( int i = 0; i < WIDTH; i++ ) {
            if ( map[i] == Terrain.WALL &&
                    map[i + WIDTH] == Terrain.WATER &&
                    Random.Int( 4 ) == 0 ) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for ( int i = WIDTH; i < LENGTH - WIDTH; i++ ) {
            if ( map[i] == Terrain.WALL &&
                    map[i - WIDTH] == Terrain.WALL &&
                    map[i + WIDTH] == Terrain.WATER &&
                    Random.Int( 2 ) == 0 ) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for ( int i = WIDTH + 1; i < LENGTH - WIDTH - 1; i++ ) {
            if ( map[i] == Terrain.EMPTY ) {

                int count =
                        ( map[i + 1] == Terrain.WALL ? 1 : 0 ) +
                                ( map[i - 1] == Terrain.WALL ? 1 : 0 ) +
                                ( map[i + WIDTH] == Terrain.WALL ? 1 : 0 ) +
                                ( map[i - WIDTH] == Terrain.WALL ? 1 : 0 );

                if ( Random.Int( 16 ) < count * count ) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        while ( true ) {
            int pos = roomEntrance.random();
            if ( pos != entrance && pos != storage ) {
                map[pos] = Terrain.SIGN;
                break;
            }
        }
    }

    @Override
    protected void createMobs () {
        super.createMobs();

        Ghost.Quest.spawn( this );
    }

    @Override
    protected void createItems () {
        if ( Dungeon.isDewVial() && Random.Int( 4 - Dungeon.getDepth() ) == 0 ) {
            addItemToSpawn( new DewVial() );
            Dungeon.setDewVial( false );
        }

        super.createItems();
    }

    @Override
    public void addVisuals ( Scene scene ) {
        super.addVisuals( scene );
        addVisuals( this, scene );
    }

    @Override
    public String tileName ( int tile ) {
        switch ( tile ) {
            case Terrain.WATER:
                return "Murky water";
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc ( int tile ) {
        switch ( tile ) {
            case Terrain.EMPTY_DECO:
                return "Wet yellowish moss covers the floor.";
            case Terrain.BOOKSHELF:
                return "The bookshelf is packed with cheap useless books. Might it burn?";
            default:
                return super.tileDesc( tile );
        }
    }

    private static class Sink extends Emitter {

        private static final Emitter.Factory factory = new Factory() {

            @Override
            public void emit ( Emitter emitter, int index, float x, float y ) {
                WaterParticle p = (WaterParticle) emitter.recycle( WaterParticle.class );
                p.reset( x, y );
            }
        };
        private int pos;
        private float rippleDelay = 0;

        public Sink ( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
            pos( p.getX() - 2, p.getY() + 1, 4, 0 );

            pour( factory, 0.05f );
        }

        @Override
        public void update () {
            setVisible( Dungeon.getVisible()[pos] );
            if ( getVisible() ) {

                super.update();

                if ( ( rippleDelay -= Game.getElapsed() ) <= 0 ) {
                    GameScene.ripple( pos + WIDTH ).setY( GameScene.ripple( pos + WIDTH ).getY() - DungeonTilemap.SIZE / 2 );
                    rippleDelay = Random.Float( 0.2f, 0.3f );
                }
            }
        }
    }

    public static final class WaterParticle extends PixelParticle {

        public WaterParticle () {
            super();

            getAcc().setY( 50 );
            setAm( 0.5f );

            color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
            size( 2 );
        }

        public void reset ( float x, float y ) {
            revive();

            this.setX( x );
            this.setY( y );

            getSpeed().set( Random.Float( -2, +2 ), 0 );

            setLeft( 0.5f );
            setLifespan( 0.5f );
        }
    }
}
