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
package com.demasu.testpixeldungeon.items.potions;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.buffs.Bleeding;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Cripple;
import com.demasu.testpixeldungeon.actors.buffs.Poison;
import com.demasu.testpixeldungeon.actors.buffs.Weakness;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.ui.StatusPane;
import com.demasu.testpixeldungeon.utils.GLog;

public class PotionOfHealing extends Potion {

    {
        name = "Potion of Healing";
    }

    public static void heal ( Hero hero ) {

        hero.setHP( hero.getHT() );
        Buff.detach( hero, Poison.class );
        Buff.detach( hero, Cripple.class );
        Buff.detach( hero, Weakness.class );
        Buff.detach( hero, Bleeding.class );
        hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
    }

    public static void heal ( Hero hero, int limit ) {

        hero.setHP( hero.getHP() + hero.getHT() * limit / 100 );

        if ( hero.getHP() > hero.getHT() ) {
            hero.setHP( hero.getHT() );
        }

        Buff.detach( hero, Poison.class );
        Buff.detach( hero, Cripple.class );
        Buff.detach( hero, Weakness.class );
        Buff.detach( hero, Bleeding.class );

        hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
    }

    @Override
    protected void apply ( Hero hero ) {
        setKnown();
        heal( Dungeon.getHero(), Dungeon.getCurrentDifficulty().healingPotionLimit() );
        GLog.p( Dungeon.getCurrentDifficulty().healingPotionMessage() );
        StatusPane.takingDamage = 0;
    }

    @Override
    public String desc () {
        return
                "An elixir that will instantly return you to full health and cure poison.";
    }

    @Override
    public int price () {
        return isKnown() ? 30 * quantity : super.price();
    }
}
