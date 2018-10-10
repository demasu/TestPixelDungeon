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

import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class TunnelPainter extends Painter {

    public static void paint ( Level level, Room room ) {

        int floor = level.tunnelTile();

        Point c = room.center();

        if ( room.width() > room.height() || ( room.width() == room.height() && Random.Int( 2 ) == 0 ) ) {

            int from = room.getRight() - 1;
            int to = room.getLeft() + 1;

            for ( Room.Door door : room.connected.values() ) {

                int step = door.getY() < c.getY() ? +1 : -1;

                if ( door.getX() == room.getLeft() ) {

                    from = room.getLeft() + 1;
                    for ( int i = door.getY(); i != c.getY(); i += step ) {
                        set( level, from, i, floor );
                    }

                } else if ( door.getX() == room.getRight() ) {

                    to = room.getRight() - 1;
                    for ( int i = door.getY(); i != c.getY(); i += step ) {
                        set( level, to, i, floor );
                    }

                } else {
                    if ( door.getX() < from ) {
                        from = door.getX();
                    }
                    if ( door.getX() > to ) {
                        to = door.getX();
                    }

                    for ( int i = door.getY() + step; i != c.getY(); i += step ) {
                        set( level, door.getX(), i, floor );
                    }
                }
            }

            for ( int i = from; i <= to; i++ ) {
                set( level, i, c.getY(), floor );
            }

        } else {

            int from = room.getBottom() - 1;
            int to = room.getTop() + 1;

            for ( Room.Door door : room.connected.values() ) {

                int step = door.getX() < c.getX() ? +1 : -1;

                if ( door.getY() == room.getTop() ) {

                    from = room.getTop() + 1;
                    for ( int i = door.getX(); i != c.getX(); i += step ) {
                        set( level, i, from, floor );
                    }

                } else if ( door.getY() == room.getBottom() ) {

                    to = room.getBottom() - 1;
                    for ( int i = door.getX(); i != c.getX(); i += step ) {
                        set( level, i, to, floor );
                    }

                } else {
                    if ( door.getY() < from ) {
                        from = door.getY();
                    }
                    if ( door.getY() > to ) {
                        to = door.getY();
                    }

                    for ( int i = door.getX() + step; i != c.getX(); i += step ) {
                        set( level, i, door.getY(), floor );
                    }
                }
            }

            for ( int i = from; i <= to; i++ ) {
                set( level, c.getX(), i, floor );
            }
        }

        for ( Room.Door door : room.connected.values() ) {
            door.set( Room.Door.Type.TUNNEL );
        }
    }
}
