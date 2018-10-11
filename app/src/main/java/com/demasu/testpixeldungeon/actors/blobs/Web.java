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

import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Roots;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.effects.particles.WebParticle;

public class Web extends Blob {

    @Override
    protected void evolve () {

        for ( int i = 0; i < LENGTH; i++ ) {

            int offv = getCur()[i] > 0 ? getCur()[i] - 1 : 0;
            getOff()[i] = offv;

            if ( offv > 0 ) {

                setVolume( getVolume() + offv );

                Char ch = Actor.findChar( i );
                if ( ch != null ) {
                    Buff.prolong( ch, Roots.class, TICK );
                }
            }
        }
    }

    @Override
    public void use ( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( WebParticle.FACTORY, 0.4f );
    }

    public void seed ( int cell, int amount ) {
        int diff = amount - getCur()[cell];
        if ( diff > 0 ) {
            getCur()[cell] = amount;
            setVolume( getVolume() + diff );
        }
    }

    @Override
    public String tileDesc () {
        return "Everything is covered with a thick web here.";
    }
}
