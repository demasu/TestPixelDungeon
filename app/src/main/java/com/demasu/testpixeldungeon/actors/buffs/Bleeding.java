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
package com.demasu.testpixeldungeon.actors.buffs;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.ResultDescriptions;
import com.demasu.testpixeldungeon.effects.Splash;
import com.demasu.testpixeldungeon.ui.BuffIndicator;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Bleeding extends Buff {

    private static final String LEVEL = "level";
    private int level;

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEVEL, level );

    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        level = bundle.getInt( LEVEL );
    }

    public void set ( int level ) {
        this.level = level;
    }

    @Override
    public int icon () {
        return BuffIndicator.BLEEDING;
    }

    @Override
    public String toString () {
        return "Bleeding";
    }

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public boolean act () {
        if ( getTarget().isAlive() ) {

            level = Random.Int( level / 2, level );
            if ( level > 0 ) {

                getTarget().damage( level, this );
                if ( getTarget().sprite.getVisible() ) {
                    Splash.at( getTarget().sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                            getTarget().sprite.blood(), Math.min( 10 * level / getTarget().getHT(), 10 ) );
                }

                if ( getTarget() == Dungeon.getHero() && !getTarget().isAlive() ) {
                    Dungeon.fail( Utils.format( ResultDescriptions.BLEEDING, Dungeon.getDepth() ) );
                    GLog.n( "You bled to death..." );
                }

                spend( TICK );
            } else {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }
}
