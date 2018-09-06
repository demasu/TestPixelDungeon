/*
 * Test Pixel Dungeon
 * Copyright (C) 2018 Demasu
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
package com.demasu.testpixeldungeon.items.armor;

import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;

public class DebugArmor extends Armor {

    {
        name  = "Debug armor";
        image =  ItemSpriteSheet.ARMOR_SCALE;
    }

    public DebugArmor () {
        super( 5 );
    }

    @Override
    public String desc () {
        return "This armor is for debugging everything.\n" +
                "It should hopefully protect you a bit.";
    }
}
