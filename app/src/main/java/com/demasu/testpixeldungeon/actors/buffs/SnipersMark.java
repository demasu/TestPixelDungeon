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

import com.demasu.testpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class SnipersMark extends FlavourBuff {

    private static final String OBJECT = "object";
    private int object = 0;

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( OBJECT, getObject() );

    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        setObject( bundle.getInt( OBJECT ) );
    }

    @Override
    public int icon () {
        return BuffIndicator.MARK;
    }

    @Override
    public String toString () {
        return "Zeroed in";
    }

    public int getObject () {
        return object;
    }

    public void setObject ( int object ) {
        this.object = object;
    }
}
