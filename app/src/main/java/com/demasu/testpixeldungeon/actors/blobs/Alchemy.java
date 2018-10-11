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
import com.demasu.testpixeldungeon.Journal;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.items.Heap;
import com.demasu.testpixeldungeon.items.Item;
import com.watabou.utils.Bundle;

public class Alchemy extends Blob {

    private int pos;

    public static void transmute ( int cell ) {
        Heap heap = Dungeon.getLevel().heaps.get( cell );
        if ( heap != null ) {

            Item result = heap.transmute();
            if ( result != null ) {
                Dungeon.getLevel().drop( result, cell ).sprite.drop( cell );
            }
        }
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        for ( int i = 0; i < LENGTH; i++ ) {
            if ( cur[i] > 0 ) {
                pos = i;
                break;
            }
        }
    }

    @Override
    protected void evolve () {
        volume = off[pos] = cur[pos];

        if ( Dungeon.getVisible()[pos] ) {
            Journal.add( Journal.Feature.ALCHEMY );
        }
    }

    @Override
    public void seed ( int cell, int amount ) {
        cur[pos] = 0;
        pos = cell;
        volume = cur[pos] = amount;
    }

    @Override
    public void use ( BlobEmitter emitter ) {
        super.use( emitter );
        final float INTERVAL = 0.4f
        emitter.start( Speck.factory( Speck.BUBBLE ), INTERVAL, 0 );
    }
}
