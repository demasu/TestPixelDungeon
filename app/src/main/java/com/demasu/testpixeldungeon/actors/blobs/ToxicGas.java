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

import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.ResultDescriptions;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.BlobEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class ToxicGas extends Blob implements Hero.Doom {

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    protected void evolve () {
        super.evolve();

        int levelDamage = 5 + Dungeon.getDepth() * 5;

        for ( int i = 0; i < LENGTH; i++ ) {
            Char ch = Actor.findChar( i );
            if ( getCur()[i] > 0 && ch != null ) {

                final int DAMAGE_MOD = 40;
                int damage = ( ch.getHT() + levelDamage ) / DAMAGE_MOD;
                if ( Random.Int( DAMAGE_MOD ) < ( ch.getHT() + levelDamage ) % DAMAGE_MOD ) {
                    damage++;
                }

                ch.damage( damage, this );
            }
        }

        Blob blob = Dungeon.getLevel().blobs.get( ParalyticGas.class );
        if ( blob != null ) {

            int[] par = blob.getCur();

            for ( int i = 0; i < LENGTH; i++ ) {

                int t = getCur()[i];
                int p = par[i];

                if ( p >= t ) {
                    setVolume( getVolume() - t );
                    getCur()[i] = 0;
                } else {
                    blob.setVolume( blob.getVolume() - p );
                    par[i] = 0;
                }
            }
        }
    }

    @Override
    public void use ( BlobEmitter emitter ) {
        super.use( emitter );

        final float INTERVAL = 0.6f;
        emitter.pour( Speck.factory( Speck.TOXIC ), INTERVAL );
    }

    @Override
    public String tileDesc () {
        return "A greenish cloud of toxic gas is swirling here.";
    }

    @Override
    public void onDeath () {

        Badges.validateDeathFromGas();

        Dungeon.fail( Utils.format( ResultDescriptions.GAS, Dungeon.getDepth() ) );
        GLog.n( "You died from a toxic gas.." );
    }
}
