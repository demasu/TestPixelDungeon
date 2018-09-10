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
package com.demasu.testpixeldungeon.items;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.particles.ShadowParticle;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public abstract class EquipableItem extends Item {

    public static final String AC_EQUIP = "EQUIP";
    public static final String AC_UNEQUIP = "UNEQUIP";
    private static final String TXT_UNEQUIP_CURSED = "You can't remove cursed %s!";

    protected static void equipCursed ( Hero hero ) {
        hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
        Sample.INSTANCE.play( Assets.SND_CURSED );
    }

    @Override
    public void execute ( Hero hero, String action ) {
        if ( action.equals( AC_EQUIP ) ) {
            doEquip( hero );
        } else if ( action.equals( AC_UNEQUIP ) ) {
            doUnequip( hero, true );
        } else {
            super.execute( hero, action );
        }
    }

    @Override
    public void doDrop ( Hero hero ) {
        if ( !isEquipped( hero ) || doUnequip( hero, false, false ) ) {
            super.doDrop( hero );
        }
    }

    @Override
    public void cast ( final Hero user, int dst ) {

        if ( isEquipped( user ) ) {
            if ( quantity == 1 && !this.doUnequip( user, false, false ) ) {
                return;
            }
        }

        super.cast( user, dst );
    }

    protected float time2equip ( Hero hero ) {
        return 1;
    }

    public abstract boolean doEquip ( Hero hero );

    public boolean doUnequip ( Hero hero, boolean collect, boolean single ) {

        if ( cursed ) {
            GLog.w( TXT_UNEQUIP_CURSED, name() );
            return false;
        }

        if ( single ) {
            hero.spendAndNext( time2equip( hero ) );
        } else {
            hero.spend( time2equip( hero ) );
        }

        if ( collect && !collect( hero.belongings.backpack ) ) {
            Dungeon.getLevel().drop( this, hero.pos );
        }

        return true;
    }

    public final boolean doUnequip ( Hero hero, boolean collect ) {
        return doUnequip( hero, collect, true );
    }
}
