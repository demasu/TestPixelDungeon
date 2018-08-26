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
package com.demasu.pixeldungeonskills.actors.mobs;

import com.demasu.pixeldungeonskills.Badges;
import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.actors.Char;
import com.demasu.pixeldungeonskills.sprites.AcidicSprite;
import com.demasu.utils.Random;

public class Acidic extends Scorpio {

	{
		name = "acidic scorpio";
		spriteClass = AcidicSprite.class;

        name = Dungeon.currentDifficulty.mobPrefix() + name;
        HT *= Dungeon.currentDifficulty.mobHPModifier();
        HP = HT;
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {
		
		int dmg = Random.IntRange( 0, damage );
		if (dmg > 0) {
			enemy.damage( dmg, this );
		}
		
		return super.defenseProc( enemy, damage );
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Badges.validateRare( this );
	}
}
