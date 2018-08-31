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
package com.demasu.testpixeldungeon.actors.blobs;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Frost;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.particles.SnowParticle;
import com.demasu.testpixeldungeon.items.Heap;
import com.watabou.utils.Random;

public class Freezing {

    // Returns true, if this cell is visible
    public static boolean affect ( int cell, Fire fire ) {

        Char ch = Actor.findChar( cell );
        if ( ch != null ) {
            Buff.prolong( ch, Frost.class, Frost.duration( ch ) * Random.Float( 1.0f, 1.5f ) );
        }

        if ( fire != null ) {
            fire.clear( cell );
        }

        Heap heap = Dungeon.level.heaps.get( cell );
        if ( heap != null ) {
            heap.freeze();
        }

        if ( Dungeon.visible[cell] ) {
            CellEmitter.get( cell ).start( SnowParticle.FACTORY, 0.2f, 6 );
            return true;
        } else {
            return false;
        }
    }
}
