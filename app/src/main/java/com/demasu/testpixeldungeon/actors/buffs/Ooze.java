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
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.ui.BuffIndicator;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;

public class Ooze extends Buff {

    private static final String TXT_HERO_KILLED = "%s killed you...";

    public int damage = 1;

    @Override
    public int icon () {
        return BuffIndicator.OOZE;
    }

    @Override
    public String toString () {
        return "Caustic ooze";
    }

    @Override
    public boolean act () {
        if ( target.isAlive() ) {
            target.damage( damage, this );
            if ( !target.isAlive() && target == Dungeon.getHero() ) {
                Dungeon.fail( Utils.format( ResultDescriptions.OOZE, Dungeon.getDepth() ) );
                GLog.n( TXT_HERO_KILLED, toString() );
            }
            spend( TICK );
        }
        if ( Level.water[target.pos] ) {
            detach();
        }
        return true;
    }
}
