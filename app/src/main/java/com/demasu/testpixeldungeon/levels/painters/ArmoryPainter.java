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

import com.demasu.testpixeldungeon.items.Bomb;
import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.keys.IronKey;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Room;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class ArmoryPainter extends Painter {

    public static void paint ( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY );

        Room.Door entrance = room.entrance();
        Point statue = null;
        if ( entrance.getX() == room.getLeft() ) {
            statue = new Point( room.getRight() - 1, Random.Int( 2 ) == 0 ? room.getTop() + 1 : room.getBottom() - 1 );
        } else if ( entrance.getX() == room.getRight() ) {
            statue = new Point( room.getLeft() + 1, Random.Int( 2 ) == 0 ? room.getTop() + 1 : room.getBottom() - 1 );
        } else if ( entrance.getY() == room.getTop() ) {
            statue = new Point( Random.Int( 2 ) == 0 ? room.getLeft() + 1 : room.getRight() - 1, room.getBottom() - 1 );
        } else if ( entrance.getY() == room.getBottom() ) {
            statue = new Point( Random.Int( 2 ) == 0 ? room.getLeft() + 1 : room.getRight() - 1, room.getTop() + 1 );
        }
        if ( statue != null ) {
            set( level, statue, Terrain.STATUE );
        }

        int n = 3 + ( Random.Int( 4 ) == 0 ? 1 : 0 );
        for ( int i = 0; i < n; i++ ) {
            int pos;
            do {
                pos = room.random();
            } while ( level.map[pos] != Terrain.EMPTY || level.heaps.get( pos ) != null );
            level.drop( prize( level ), pos );
        }

        entrance.set( Room.Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey() );
    }

    private static Item prize ( Level level ) {
        return Random.Int( 6 ) == 0 ?
                new Bomb().random() :
                Generator.random( Random.oneOf(
                        Generator.Category.ARMOR,
                        Generator.Category.WEAPON
                ) );
    }
}
