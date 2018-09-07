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
package com.demasu.testpixeldungeon.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Invisibility;
import com.demasu.testpixeldungeon.actors.buffs.Sleep;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.utils.GLog;

public class ScrollOfLullaby extends Scroll {

    {
        name = "Scroll of Lullaby";
    }

    @Override
    protected void doRead () {

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.NOTE ), 0.3f, 5 );
        Sample.INSTANCE.play( Assets.SND_LULLABY );
        Invisibility.dispel();

        int count = 0;
        Mob affected = null;
        for ( Mob mob : Dungeon.getLevel().mobs.toArray( new Mob[0] ) ) {
            if ( Level.fieldOfView[mob.pos] ) {
                Buff.affect( mob, Sleep.class );
                if ( mob.buff( Sleep.class ) != null ) {
                    affected = mob;
                    count++;
                }
            }
        }

        switch ( count ) {
            case 0:
                GLog.i( "The scroll utters a soothing melody." );
                break;
            case 1:
                GLog.i( "The scroll utters a soothing melody and the " + affected.name + " falls asleep!" );
                break;
            default:
                GLog.i( "The scroll utters a soothing melody and the monsters fall asleep!" );
        }
        setKnown();

        readAnimation();
    }

    @Override
    public String desc () {
        return
                "A soothing melody will put all creatures in your field of view into a deep sleep, " +
                        "giving you a chance to flee or make a surprise attack on them.";
    }

    @Override
    public int price () {
        return isKnown() ? 50 * quantity : super.price();
    }
}
