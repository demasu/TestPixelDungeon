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

 /*
 * This weapong made for debugging by Demasu (5 September 2018)
 */

package com.demasu.testpixeldungeon.items.weapon.melee;

import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;

public class SwordOfDebug extends MeleeWeapon {

    {
      name = "Sword of debug";
      image = ItemSpriteSheet.NecroBlade5;
    }
    
    public SwordOfDebug () {
      super( 5, 1f, 1f);
    }

    @Override
    public String desc () {
      return "A blade that should only be used for debugging the game\n" +
      "This weapon should hit more often than not.";
    }
}