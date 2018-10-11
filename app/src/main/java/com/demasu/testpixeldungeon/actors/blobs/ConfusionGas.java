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
import com.demasu.testpixeldungeon.actors.buffs.Vertigo;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.effects.Speck;

public class ConfusionGas extends Blob {

    @Override
    protected void evolve () {
        super.evolve();

        for ( int i = 0; i < LENGTH; i++ ) {
            Char ch = Actor.findChar( i );
            if ( getCur()[i] > 0 && ch != null ) {
                Buff.prolong( ch, Vertigo.class, Vertigo.duration( ch ) );
            }
        }
    }

    @Override
    public void use ( BlobEmitter emitter ) {
        super.use( emitter );

        final float INTERVAL = 0.6f;
        emitter.pour( Speck.factory( Speck.CONFUSION, true ), INTERVAL );
    }

    @Override
    public String tileDesc () {
        return "A cloud of confusion gas is swirling here.";
    }
}
