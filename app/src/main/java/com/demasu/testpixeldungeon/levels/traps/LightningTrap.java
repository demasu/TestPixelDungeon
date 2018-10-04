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
package com.demasu.testpixeldungeon.levels.traps;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.ResultDescriptions;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Lightning;
import com.demasu.testpixeldungeon.effects.particles.SparkParticle;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class LightningTrap {

    public static final Electricity LIGHTNING = new Electricity();

    // 00x66CCEE
    private static final String name = "lightning trap";

    public static void trigger ( int pos, Char ch ) {

        if ( ch != null ) {
            ch.damage( Math.max( 1, Random.Int( ch.getHP() / 3, 2 * ch.getHP() / 3 ) ), LIGHTNING );
            if ( ch == Dungeon.getHero() ) {

                Camera.getMain().shake( 2, 0.3f );

                if ( !ch.isAlive() ) {
                    Dungeon.fail( Utils.format( ResultDescriptions.TRAP, name, Dungeon.getDepth() ) );
                    GLog.n( "You were killed by a discharge of a lightning trap..." );
                } else {
                    ( (Hero) ch ).belongings.charge( false );
                }
            }

            int[] points = new int[2];

            points[0] = pos - Level.WIDTH;
            points[1] = pos + Level.WIDTH;
            ch.sprite.getParent().add( new Lightning( points, 2, null ) );

            points[0] = pos - 1;
            points[1] = pos + 1;
            ch.sprite.getParent().add( new Lightning( points, 2, null ) );
        }

        CellEmitter.center( pos ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 4 ) );

    }

    public static class Electricity {
    }
}
