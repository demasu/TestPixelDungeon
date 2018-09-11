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
package com.demasu.testpixeldungeon.items.food;

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Badges;
import com.demasu.testpixeldungeon.Statistics;
import com.demasu.testpixeldungeon.actors.buffs.Hunger;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.effects.SpellSprite;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

    public static final String AC_EAT = "EAT";
    private static final float TIME_TO_EAT = 3f;
    public float energy = Hunger.HUNGRY;
    public String message = "That food tasted delicious!";

    {
        stackable = true;
        name = "ration of food";
        image = ItemSpriteSheet.RATION;
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_EAT );
        return actions;
    }

    @Override
    public void execute ( Hero hero, String action ) {
        if ( action.equals( AC_EAT ) ) {

            detach( hero.belongings.backpack );

            ( (Hunger) hero.buff( Hunger.class ) ).satisfy( energy );
            GLog.i( message );

            switch ( hero.getHeroClass() ) {
                case WARRIOR:
                    if ( hero.getHP() < hero.getHT() ) {
                        hero.setHP( Math.min( hero.getHP() + 5, hero.getHT() ) );
                        hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                    }
                    break;
                case MAGE:
                    hero.belongings.charge( false );
                    ScrollOfRecharging.charge( hero );
                    break;
                case ROGUE:
                case HUNTRESS:
                    break;
            }

            hero.sprite.operate( hero.pos );
            hero.busy();
            SpellSprite.show( hero, SpellSprite.FOOD );
            Sample.INSTANCE.play( Assets.SND_EAT );

            hero.spend( TIME_TO_EAT );

            Statistics.foodEaten++;
            Badges.validateFoodEaten();

        } else {

            super.execute( hero, action );

        }
    }

    @Override
    public String info () {
        return
                "Nothing fancy here: dried meat, " +
                        "some biscuits - things like that.";
    }

    @Override
    public boolean isUpgradable () {
        return false;
    }

    @Override
    public boolean isIdentified () {
        return true;
    }

    @Override
    public int price () {
        return 10 * quantity;
    }
}
