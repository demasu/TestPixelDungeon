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
package com.demasu.testpixeldungeon.items.rings;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class GemStone extends Ring {

    public int charge = 0;

    {
        name = "Gemstone";
    }

    public void ChargeUp () {
        charge++;
        if ( charge == 20 ) {
            image = ItemSpriteSheet.GemStonePart;
            GLog.p( "Gem stone partially charged!" );
        }
        if ( charge == 40 ) {
            image = ItemSpriteSheet.GemStoneFull;
            GLog.p( "Gem stone fully charged!" );
        }
    }

    @Override
    public void execute ( Hero hero, String action ) {
        if ( action.equals( "Activate" ) ) {
            image = ItemSpriteSheet.GemStone;
            Dungeon.getHero().HP += charge;
            if ( Dungeon.getHero().HP > Dungeon.getHero().HT ) {
                Dungeon.getHero().HP = Dungeon.getHero().HT;
            }
            CellEmitter.center( hero.pos ).burst( Speck.factory( Speck.HEALING ), 1 );
            if ( charge == 40 ) {
                GLog.p( "Gemstone fully healed you!" );
                Dungeon.getHero().HP = Dungeon.getHero().HT;
            } else {
                GLog.p( "Gemstone heals for " + charge + "HP!" );
            }
            charge = 0;
        } else {

            super.execute( hero, action );

        }
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( "charge", charge );
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        charge = bundle.getInt( "charge" );
        if ( charge > 19 ) {
            image = ItemSpriteSheet.GemStonePart;
        }
        if ( charge == 40 ) {
            image = ItemSpriteSheet.GemStoneFull;
        }
    }

    @Override
    public Item random () {
        level = +1;
        return this;
    }

    @Override
    protected RingBuff buff () {
        return new GemStoneBuff();
    }

    @Override
    public boolean doPickUp ( Hero hero ) {
        identify();
        return super.doPickUp( hero );
    }

    @Override
    public boolean isUpgradable () {
        return false;
    }

    @Override
    public void use () {
        // Do nothing (it can't degrade)
    }

    @Override
    public String desc () {
        return isKnown() ?
                "The Gemstone is a unique artifact capable of storing life energy for later use." +
                        " Although it does not damage the wearer, it drains away any rejuvenation his body possesses." +
                        " That is until it fully charges and becomes a life savior in times of need.\n" +
                        " The Gemstone's current charge is " + charge + "/40" :
                super.desc();
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if ( charge > 19 ) {
            actions.add( "Activate" );
        }
        return actions;
    }

    @Override
    public String info () {
        if ( isEquipped( Dungeon.getHero() ) ) {

            return desc();

        } else if ( cursed && cursedKnown ) {

            return desc() + "\n\nYou can feel a malevolent magic lurking within the " + name() + ".";

        } else {

            return desc();

        }
    }

    public class GemStoneBuff extends RingBuff {
    }
}
