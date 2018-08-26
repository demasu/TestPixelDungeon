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
package com.demasu.pixeldungeonskills.actors.hero;

import com.demasu.pixeldungeonskills.Badges;
import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.items.Item;
import com.demasu.pixeldungeonskills.items.KindOfWeapon;
import com.demasu.pixeldungeonskills.items.armor.Armor;
import com.demasu.pixeldungeonskills.items.bags.Bag;
import com.demasu.pixeldungeonskills.items.keys.IronKey;
import com.demasu.pixeldungeonskills.items.keys.Key;
import com.demasu.pixeldungeonskills.items.rings.Ring;
import com.demasu.pixeldungeonskills.items.scrolls.ScrollOfRemoveCurse;
import com.demasu.pixeldungeonskills.items.wands.Wand;
//import com.demasu.pixeldungeonskills.items.weapon.missiles.Bow;
import com.demasu.utils.Bundle;
import com.demasu.utils.Random;

import java.util.Iterator;

public class Storage implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 5;

	private Hero owner;

	public Bag backpack;



	public Storage(Hero owner) {
		this.owner = owner;
		
		backpack = new Bag() {{
			name = "Storage";
			size = BACKPACK_SIZE;
		}};
		backpack.owner = owner;
	}
	


	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle2( bundle );
		

	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle2( bundle );


	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}

	private class ItemIterator implements Iterator<Item> {

		@Override
		public boolean hasNext() {
            return false;
		}

		@Override
		public Item next() {
			return null;

		}

		@Override
		public void remove() {

		}
	}
}
