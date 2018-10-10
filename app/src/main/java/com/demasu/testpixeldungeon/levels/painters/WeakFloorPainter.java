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
import com.demasu.testpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class WeakFloorPainter extends Painter {

    public static void paint ( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.CHASM );

        Room.Door door = room.entrance();
        door.set( Room.Door.Type.REGULAR );

        if ( door.getX() == room.getLeft() ) {
            for ( int i = room.getTop() + 1; i < room.getBottom(); i++ ) {
                drawInside( level, room, new Point( room.getLeft(), i ), Random.IntRange( 1, room.width() - 2 ), Terrain.EMPTY_SP );
            }
        } else if ( door.getX() == room.getRight() ) {
            for ( int i = room.getTop() + 1; i < room.getBottom(); i++ ) {
                drawInside( level, room, new Point( room.getRight(), i ), Random.IntRange( 1, room.width() - 2 ), Terrain.EMPTY_SP );
            }
        } else if ( door.getY() == room.getTop() ) {
            for ( int i = room.getLeft() + 1; i < room.getRight(); i++ ) {
                drawInside( level, room, new Point( i, room.getTop() ), Random.IntRange( 1, room.height() - 2 ), Terrain.EMPTY_SP );
            }
        } else if ( door.getY() == room.getBottom() ) {
            for ( int i = room.getLeft() + 1; i < room.getRight(); i++ ) {
                drawInside( level, room, new Point( i, room.getBottom() ), Random.IntRange( 1, room.height() - 2 ), Terrain.EMPTY_SP );
            }
        }
    }
}
