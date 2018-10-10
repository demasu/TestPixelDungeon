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

import com.demasu.testpixeldungeon.actors.blobs.Alchemy;
import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.keys.IronKey;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Room;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LaboratoryPainter extends Painter {

    public static void paint ( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY_SP );

        Room.Door entrance = room.entrance();

        Point pot = null;
        if ( entrance.getX() == room.getLeft() ) {
            pot = new Point( room.getRight() - 1, Random.Int( 2 ) == 0 ? room.getTop() + 1 : room.getBottom() - 1 );
        } else if ( entrance.getX() == room.getRight() ) {
            pot = new Point( room.getLeft() + 1, Random.Int( 2 ) == 0 ? room.getTop() + 1 : room.getBottom() - 1 );
        } else if ( entrance.getY() == room.getTop() ) {
            pot = new Point( Random.Int( 2 ) == 0 ? room.getLeft() + 1 : room.getRight() - 1, room.getBottom() - 1 );
        } else if ( entrance.getY() == room.getBottom() ) {
            pot = new Point( Random.Int( 2 ) == 0 ? room.getLeft() + 1 : room.getRight() - 1, room.getTop() + 1 );
        }
        set( level, pot, Terrain.ALCHEMY );

        Alchemy alchemy = new Alchemy();
        alchemy.seed( pot.getX() + Level.WIDTH * pot.getY(), 1 );
        level.blobs.put( Alchemy.class, alchemy );

        int n = Random.IntRange( 2, 3 );
        for ( int i = 0; i < n; i++ ) {
            int pos;
            do {
                pos = room.random();
            } while (
                    level.map[pos] != Terrain.EMPTY_SP ||
                            level.heaps.get( pos ) != null );
            level.drop( prize( level ), pos );
        }

        entrance.set( Room.Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey() );
    }

    private static Item prize ( Level level ) {

        Item prize = level.itemToSpanAsPrize();
        if ( prize instanceof Potion ) {
            return prize;
        } else if ( prize != null ) {
            level.addItemToSpawn( prize );
        }

        return Generator.random( Generator.Category.POTION );
    }
}
