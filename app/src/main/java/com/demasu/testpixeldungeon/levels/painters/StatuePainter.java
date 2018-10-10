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

import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.mobs.Statue;
import com.demasu.testpixeldungeon.items.keys.IronKey;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Room;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;

public class StatuePainter extends Painter {

    public static void paint ( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY );

        Point c = room.center();
        int cx = c.getX();
        int cy = c.getY();

        Room.Door door = room.entrance();

        door.set( Room.Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey() );

        if ( door.getX() == room.getLeft() ) {

            fill( level, room.getRight() - 1, room.getTop() + 1, 1, room.height() - 1, Terrain.STATUE );
            cx = room.getRight() - 2;

        } else if ( door.getX() == room.getRight() ) {

            fill( level, room.getLeft() + 1, room.getTop() + 1, 1, room.height() - 1, Terrain.STATUE );
            cx = room.getLeft() + 2;

        } else if ( door.getY() == room.getTop() ) {

            fill( level, room.getLeft() + 1, room.getBottom() - 1, room.width() - 1, 1, Terrain.STATUE );
            cy = room.getBottom() - 2;

        } else if ( door.getY() == room.getBottom() ) {

            fill( level, room.getLeft() + 1, room.getTop() + 1, room.width() - 1, 1, Terrain.STATUE );
            cy = room.getTop() + 2;

        }

        Statue statue = new Statue();
        statue.pos = cx + cy * Level.WIDTH;
        level.mobs.add( statue );
        Actor.occupyCell( statue );
    }
}
