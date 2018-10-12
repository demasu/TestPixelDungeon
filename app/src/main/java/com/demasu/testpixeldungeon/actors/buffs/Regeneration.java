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

import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.items.rings.RingOfMending;

public class Regeneration extends Buff {

    private static final float REGENERATION_DELAY = 10;

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public boolean act () {
        if ( getTarget().isAlive() ) {

            if ( getTarget().getHP() < getTarget().getHT() && !( (Hero) getTarget() ).isStarving() ) {
                getTarget().setHP( getTarget().getHP() + 1 );
            }

            int bonus = 0;
            for ( Buff buff : getTarget().buffs( RingOfMending.Rejuvenation.class ) ) {
                bonus += ( (RingOfMending.Rejuvenation) buff ).level;
            }

            bonus += ( (Hero) getTarget() ).heroSkills.passiveA2.healthRegenerationBonus(); // <-- Warrior regeneration if present

            final double BASE = 1.2;
            spend( (float) ( REGENERATION_DELAY / Math.pow( BASE, bonus ) ) );

        } else {

            diactivate();

        }

        return true;
    }
}
