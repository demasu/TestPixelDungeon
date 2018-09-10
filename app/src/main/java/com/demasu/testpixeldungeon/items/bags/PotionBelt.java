/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
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

package com.demasu.testpixeldungeon.items.bags;

import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.items.potions.Potion;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;

@SuppressWarnings ( "WeakerAccess" )
public class PotionBelt extends Bag {

    private int sellPrice;

    {
        name = "potion belt";
        image = ItemSpriteSheet.BELT;
        //noinspection MagicNumber
        setSellPrice( 50 );
        //noinspection MagicNumber
        size = 14;
    }

    @Override
    public boolean grab ( Item item ) {
        return item instanceof Potion;
    }

    @Override
    public int price () {
        return getSellPrice();
    }

    @Override
    public String info () {
        return
                "You can store a significant number of potions in the curiously made containers " +
                        "which go around this wondrous belt. It would not only save room in your backpack, " +
                        "but also protect these potions from breaking.";
    }

    @Override
    public boolean doPickUp ( Hero hero ) {

        return hero.belongings.getItem( PotionBelt.class ) == null && super.doPickUp( hero );

    }

    @SuppressWarnings ( "WeakerAccess" )
    public int getSellPrice () {
        return sellPrice;
    }

    public void setSellPrice ( int sellPrice ) {
        this.sellPrice = sellPrice;
    }
}
