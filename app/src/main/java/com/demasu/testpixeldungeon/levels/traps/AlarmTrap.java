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

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class AlarmTrap {

    // 0xDD3333

    public static void trigger ( int pos, Char ch ) {

        for ( Mob mob : Dungeon.getLevel().mobs ) {
            if ( mob != ch ) {
                mob.beckon( pos );
            }
        }

        if ( Dungeon.getVisible()[pos] ) {
            GLog.w( "The trap emits a piercing sound that echoes throughout the dungeon!" );
            CellEmitter.center( pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
        }

        Sample.INSTANCE.play( Assets.SND_ALERT );
    }
}
