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

import com.demasu.testpixeldungeon.actors.mobs.npcs.RatKing;
import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.Gold;
import com.demasu.testpixeldungeon.items.Heap;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Room;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class RatKingPainter extends Painter {

    public static void paint ( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY_SP );

        Room.Door entrance = room.entrance();
        entrance.set( Room.Door.Type.HIDDEN );
        int door = entrance.getX() + entrance.getY() * Level.WIDTH;

        for ( int i = room.left + 1; i < room.right; i++ ) {
            addChest( level, ( room.top + 1 ) * Level.WIDTH + i, door );
            addChest( level, ( room.bottom - 1 ) * Level.WIDTH + i, door );
        }

        for ( int i = room.top + 2; i < room.bottom - 1; i++ ) {
            addChest( level, i * Level.WIDTH + room.left + 1, door );
            addChest( level, i * Level.WIDTH + room.right - 1, door );
        }

        while ( true ) {
            Heap chest = level.heaps.get( room.random() );
            if ( chest != null ) {
                chest.type = Heap.Type.MIMIC;
                break;
            }
        }

        RatKing king = new RatKing();
        king.pos = room.random( 1 );
        level.mobs.add( king );
    }

    private static void addChest ( Level level, int pos, int door ) {

        if ( pos == door - 1 ||
                pos == door + 1 ||
                pos == door - Level.WIDTH ||
                pos == door + Level.WIDTH ) {
            return;
        }

        Item prize;
        switch ( Random.Int( 10 ) ) {
            case 0:
                prize = Generator.random( Generator.Category.WEAPON );
                if ( prize instanceof MissileWeapon ) {
                    prize.quantity( 1 );
                } else {
                    prize.degrade( Random.Int( 3 ) );
                }
                break;
            case 1:
                prize = Generator.random( Generator.Category.ARMOR ).degrade( Random.Int( 3 ) );
                break;
            default:
                prize = new Gold( Random.IntRange( 1, 5 ) );
                break;
        }

        level.drop( prize, pos ).type = Heap.Type.CHEST;
    }
}
