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
package com.demasu.testpixeldungeon.levels.painters;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.Gold;
import com.demasu.testpixeldungeon.items.Heap;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Room;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class StandardPainter extends Painter {

    public static void paint ( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        for ( Room.Door door : room.connected.values() ) {
            door.set( Room.Door.Type.REGULAR );
        }

        if ( !Dungeon.bossLevel() && Random.Int( 5 ) == 0 ) {
            switch ( Random.Int( 6 ) ) {
                case 0:
                    if ( level.feeling != Level.Feeling.GRASS ) {
                        if ( Math.min( room.width(), room.height() ) >= 4 && Math.max( room.width(), room.height() ) >= 6 ) {
                            paintGraveyard( level, room );
                            return;
                        }
                        break;
                    } else {
                        // Burned room
                    }
                case 1:
                    if ( Dungeon.getDepth() > 1 ) {
                        paintBurned( level, room );
                        return;
                    }
                    break;
                case 2:
                    if ( Math.max( room.width(), room.height() ) >= 4 ) {
                        paintStriped( level, room );
                        return;
                    }
                    break;
                case 3:
                    if ( room.width() >= 6 && room.height() >= 6 ) {
                        paintStudy( level, room );
                        return;
                    }
                    break;
                case 4:
                    if ( level.feeling != Level.Feeling.WATER ) {
                        if ( room.connected.size() == 2 && room.width() >= 4 && room.height() >= 4 ) {
                            paintBridge( level, room );
                            return;
                        }
                        break;
                    } else {
                        // Fissure
                    }
                case 5:
                    if ( !Dungeon.bossLevel() && !Dungeon.bossLevel( Dungeon.getDepth() + 1 ) &&
                            Math.min( room.width(), room.height() ) >= 5 ) {
                        paintFissure( level, room );
                        return;
                    }
                    break;
            }
        }

        fill( level, room, 1, Terrain.EMPTY );
    }

    private static void paintBurned ( Level level, Room room ) {
        for ( int i = room.getTop() + 1; i < room.getBottom(); i++ ) {
            for ( int j = room.getLeft() + 1; j < room.getRight(); j++ ) {
                int t = Terrain.EMBERS;
                switch ( Random.Int( 5 ) ) {
                    case 0:
                        t = Terrain.EMPTY;
                        break;
                    case 1:
                        t = Terrain.FIRE_TRAP;
                        break;
                    case 2:
                        t = Terrain.SECRET_FIRE_TRAP;
                        break;
                    case 3:
                        t = Terrain.INACTIVE_TRAP;
                        break;
                }
                level.map[i * Level.WIDTH + j] = t;
            }
        }
    }

    private static void paintGraveyard ( Level level, Room room ) {
        fill( level, room.getLeft() + 1, room.getTop() + 1, room.width() - 1, room.height() - 1, Terrain.GRASS );

        int w = room.width() - 1;
        int h = room.height() - 1;
        int nGraves = Math.max( w, h ) / 2;

        int index = Random.Int( nGraves );

        int shift = Random.Int( 2 );
        for ( int i = 0; i < nGraves; i++ ) {
            int pos = w > h ?
                    room.getLeft() + 1 + shift + i * 2 + ( room.getTop() + 2 + Random.Int( h - 2 ) ) * Level.WIDTH :
                    ( room.getLeft() + 2 + Random.Int( w - 2 ) ) + ( room.getTop() + 1 + shift + i * 2 ) * Level.WIDTH;
            level.drop( i == index ? Generator.random() : new Gold(), pos ).type = Heap.Type.TOMB;
        }
    }

    private static void paintStriped ( Level level, Room room ) {
        fill( level, room.getLeft() + 1, room.getTop() + 1, room.width() - 1, room.height() - 1, Terrain.EMPTY_SP );

        if ( room.width() > room.height() ) {
            for ( int i = room.getLeft() + 2; i < room.getRight(); i += 2 ) {
                fill( level, i, room.getTop() + 1, 1, room.height() - 1, Terrain.HIGH_GRASS );
            }
        } else {
            for ( int i = room.getTop() + 2; i < room.getBottom(); i += 2 ) {
                fill( level, room.getLeft() + 1, i, room.width() - 1, 1, Terrain.HIGH_GRASS );
            }
        }
    }

    private static void paintStudy ( Level level, Room room ) {
        fill( level, room.getLeft() + 1, room.getTop() + 1, room.width() - 1, room.height() - 1, Terrain.BOOKSHELF );
        fill( level, room.getLeft() + 2, room.getTop() + 2, room.width() - 3, room.height() - 3, Terrain.EMPTY_SP );

        for ( Point door : room.connected.values() ) {
            if ( door.getX() == room.getLeft() ) {
                set( level, door.getX() + 1, door.getY(), Terrain.EMPTY );
            } else if ( door.getX() == room.getRight() ) {
                set( level, door.getX() - 1, door.getY(), Terrain.EMPTY );
            } else if ( door.getY() == room.getTop() ) {
                set( level, door.getX(), door.getY() + 1, Terrain.EMPTY );
            } else if ( door.getY() == room.getBottom() ) {
                set( level, door.getX(), door.getY() - 1, Terrain.EMPTY );
            }
        }

        set( level, room.center(), Terrain.PEDESTAL );
    }

    private static void paintBridge ( Level level, Room room ) {

        fill( level, room.getLeft() + 1, room.getTop() + 1, room.width() - 1, room.height() - 1,
                !Dungeon.bossLevel() && !Dungeon.bossLevel( Dungeon.getDepth() + 1 ) && Random.Int( 3 ) == 0 ?
                        Terrain.CHASM :
                        Terrain.WATER );

        Point door1 = null;
        Point door2 = null;
        for ( Point p : room.connected.values() ) {
            if ( door1 == null ) {
                door1 = p;
            } else {
                door2 = p;
            }
        }

        if ( ( door1.getX() == room.getLeft() && door2.getX() == room.getRight() ) ||
                ( door1.getX() == room.getRight() && door2.getX() == room.getLeft() ) ) {

            int s = room.width() / 2;

            drawInside( level, room, door1, s, Terrain.EMPTY_SP );
            drawInside( level, room, door2, s, Terrain.EMPTY_SP );
            fill( level, room.center().getX(), Math.min( door1.getY(), door2.getY() ), 1, Math.abs( door1.getY() - door2.getY() ) + 1, Terrain.EMPTY_SP );

        } else if ( ( door1.getY() == room.getTop() && door2.getY() == room.getBottom() ) ||
                ( door1.getY() == room.getBottom() && door2.getY() == room.getTop() ) ) {

            int s = room.height() / 2;

            drawInside( level, room, door1, s, Terrain.EMPTY_SP );
            drawInside( level, room, door2, s, Terrain.EMPTY_SP );
            fill( level, Math.min( door1.getX(), door2.getX() ), room.center().getY(), Math.abs( door1.getX() - door2.getX() ) + 1, 1, Terrain.EMPTY_SP );

        } else if ( door1.getX() == door2.getX() ) {

            fill( level, door1.getX() == room.getLeft() ? room.getLeft() + 1 : room.getRight() - 1, Math.min( door1.getY(), door2.getY() ), 1, Math.abs( door1.getY() - door2.getY() ) + 1, Terrain.EMPTY_SP );

        } else if ( door1.getY() == door2.getY() ) {

            fill( level, Math.min( door1.getX(), door2.getX() ), door1.getY() == room.getTop() ? room.getTop() + 1 : room.getBottom() - 1, Math.abs( door1.getX() - door2.getX() ) + 1, 1, Terrain.EMPTY_SP );

        } else if ( door1.getY() == room.getTop() || door1.getY() == room.getBottom() ) {

            drawInside( level, room, door1, Math.abs( door1.getY() - door2.getY() ), Terrain.EMPTY_SP );
            drawInside( level, room, door2, Math.abs( door1.getX() - door2.getX() ), Terrain.EMPTY_SP );

        } else if ( door1.getX() == room.getLeft() || door1.getX() == room.getRight() ) {

            drawInside( level, room, door1, Math.abs( door1.getX() - door2.getX() ), Terrain.EMPTY_SP );
            drawInside( level, room, door2, Math.abs( door1.getY() - door2.getY() ), Terrain.EMPTY_SP );

        }
    }

    private static void paintFissure ( Level level, Room room ) {
        fill( level, room.getLeft() + 1, room.getTop() + 1, room.width() - 1, room.height() - 1, Terrain.EMPTY );

        for ( int i = room.getTop() + 2; i < room.getBottom() - 1; i++ ) {
            for ( int j = room.getLeft() + 2; j < room.getRight() - 1; j++ ) {
                int v = Math.min( i - room.getTop(), room.getBottom() - i );
                int h = Math.min( j - room.getLeft(), room.getRight() - j );
                if ( Math.min( v, h ) > 2 || Random.Int( 2 ) == 0 ) {
                    set( level, j, i, Terrain.CHASM );
                }
            }
        }
    }
}
