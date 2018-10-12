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

import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.ResultDescriptions;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.items.rings.RingOfElements.Resistance;
import com.demasu.testpixeldungeon.ui.BuffIndicator;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;

public class Poison extends Buff implements Hero.Doom {

    private static final String LEFT = "left";
    private float left;

    public static float durationFactor ( Char ch ) {
        Resistance r = ch.buff( Resistance.class );
        return r != null ? r.durationFactor() : 1;
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );

    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        left = bundle.getFloat( LEFT );
    }

    public void set ( float duration ) {
        this.left = duration;
    }

    @Override
    public int icon () {
        return BuffIndicator.POISON;
    }

    @Override
    public String toString () {
        return "Poisoned";
    }

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public boolean act () {
        if ( getTarget().isAlive() ) {

            if ( getTarget() instanceof Mob && Dungeon.getHero().heroSkills.passiveB2.venomBonus() > 0 ) {
                getTarget().damage( (int) ( left / 3 ) + 1 + Dungeon.getHero().heroSkills.passiveB2.venomBonus(), this );
            } else {
                getTarget().damage( (int) ( left / 3 ) + 1, this );
            }
            spend( TICK );

            left -= TICK;
            if ( left <= 0 ) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    @Override
    public void onDeath () {
        Badges.validateDeathFromPoison();

        Dungeon.fail( Utils.format( ResultDescriptions.POISON, Dungeon.getDepth() ) );
        GLog.n( "You died from poison..." );
    }
}
