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
import com.demasu.testpixeldungeon.Journal.Feature;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.items.Heap;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class WellWater extends Blob {

    protected int pos;

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
            if ( this instanceof WaterOfAwareness ) {
                Journal.add( Feature.WELL_OF_AWARENESS );
            } else if ( this instanceof WaterOfHealth ) {
                Journal.add( Feature.WELL_OF_HEALTH );
            } else if ( this instanceof WaterOfTransmutation ) {
                Journal.add( Feature.WELL_OF_TRANSMUTATION );
            }
        }
    }

    protected boolean affect () {

        Heap heap;

        if ( pos == Dungeon.getHero().pos && affectHero( Dungeon.getHero() ) ) {

            volume = off[pos] = cur[pos] = 0;
            return true;

        } else if ( ( heap = Dungeon.getLevel().heaps.get( pos ) ) != null ) {

            Item oldItem = heap.peek();
            Item newItem = affectItem( oldItem );

            if ( newItem != null ) {

                if ( newItem == oldItem ) {

                } else if ( oldItem.quantity() > 1 ) {

                    oldItem.quantity( oldItem.quantity() - 1 );
                    heap.drop( newItem );

                } else {
                    heap.replace( oldItem, newItem );
                }

                heap.sprite.link();
                volume = off[pos] = cur[pos] = 0;

                return true;

            } else {

                int newPlace;
                do {
                    newPlace = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
                } while ( !Level.passable[newPlace] && !Level.avoid[newPlace] );
                Dungeon.getLevel().drop( heap.pickUp(), newPlace ).sprite.drop( pos );

                return false;

            }

        } else {

            return false;

        }
    }

    protected boolean affectHero ( Hero hero ) {
        return false;
    }

    protected Item affectItem ( Item item ) {
        return null;
    }

    @Override
    public void seed ( int cell, int amount ) {
        cur[pos] = 0;
        pos = cell;
        volume = cur[pos] = amount;
    }

    public static void affectCell ( int cell ) {

        Class<?>[] waters = { WaterOfHealth.class, WaterOfAwareness.class, WaterOfTransmutation.class };

        for ( Class<?> waterClass : waters ) {
            WellWater water = (WellWater) Dungeon.getLevel().blobs.get( waterClass );
            if ( water != null &&
                    water.volume > 0 &&
                    water.pos == cell &&
                    water.affect() ) {

                Level.set( cell, Terrain.EMPTY_WELL );
                GameScene.updateMap( cell );

                return;
            }
        }
    }
}
