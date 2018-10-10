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

import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.Heap.Type;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.keys.IronKey;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Room;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class PitPainter extends Painter {

    public static void paint ( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY );

        Room.Door entrance = room.entrance();
        entrance.set( Room.Door.Type.LOCKED );

        Point well = null;
        if ( entrance.getX() == room.getLeft() ) {
            well = new Point( room.getRight() - 1, Random.Int( 2 ) == 0 ? room.getTop() + 1 : room.getBottom() - 1 );
        } else if ( entrance.getX() == room.getRight() ) {
            well = new Point( room.getLeft() + 1, Random.Int( 2 ) == 0 ? room.getTop() + 1 : room.getBottom() - 1 );
        } else if ( entrance.getY() == room.getTop() ) {
            well = new Point( Random.Int( 2 ) == 0 ? room.getLeft() + 1 : room.getRight() - 1, room.getBottom() - 1 );
        } else if ( entrance.getY() == room.getBottom() ) {
            well = new Point( Random.Int( 2 ) == 0 ? room.getLeft() + 1 : room.getRight() - 1, room.getTop() + 1 );
        }
        set( level, well, Terrain.EMPTY_WELL );

        int remains = room.random();
        while ( level.map[remains] == Terrain.EMPTY_WELL ) {
            remains = room.random();
        }

        level.drop( new IronKey(), remains ).type = Type.SKELETON;

        if ( Random.Int( 5 ) == 0 ) {
            level.drop( Generator.random( Generator.Category.RING ), remains );
        } else {
            level.drop( Generator.random( Random.oneOf(
                    Generator.Category.WEAPON,
                    Generator.Category.ARMOR
            ) ), remains );
        }

        int n = Random.IntRange( 1, 2 );
        for ( int i = 0; i < n; i++ ) {
            level.drop( prize( level ), remains );
        }
    }

    private static Item prize ( Level level ) {

        Item prize = level.itemToSpanAsPrize();
        if ( prize != null ) {
            return prize;
        }

        return Generator.random( Random.oneOf(
                Generator.Category.POTION,
                Generator.Category.SCROLL,
                Generator.Category.FOOD,
                Generator.Category.GOLD
        ) );
    }
}
