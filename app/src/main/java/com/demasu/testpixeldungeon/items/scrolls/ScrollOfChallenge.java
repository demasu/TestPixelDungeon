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
import com.demasu.testpixeldungeon.actors.buffs.Rage;
import com.demasu.testpixeldungeon.actors.mobs.Mimic;
import com.demasu.testpixeldungeon.actors.mobs.Mob;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.items.Heap;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.utils.GLog;

public class ScrollOfChallenge extends Scroll {

	{
		name = "Scroll of Challenge";
	}
	
	@Override
	protected void doRead() {
		
		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			mob.beckon( curUser.pos );
			if (Dungeon.visible[mob.pos]) {
				Buff.affect( mob, Rage.class, Level.distance( curUser.pos, mob.pos ) );
			}
		}
		
		for (Heap heap : Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.MIMIC) {
				Mimic m = Mimic.spawnAt( heap.pos, heap.items );
				if (m != null) {
					m.beckon( curUser.pos );
					heap.destroy();
				}
			}
		}
		
		GLog.w( "The scroll emits a challenging roar that echoes throughout the dungeon!" );
		setKnown();
		
		curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );		
		Sample.INSTANCE.play( Assets.SND_CHALLENGE );
		Invisibility.dispel();
		
		readAnimation();
	}
	
	@Override
	public String desc() {
		return 
			"When read aloud, this scroll will unleash a challenging roar " +
			"that will awaken all monsters and alert them to the reader's location.";
	}
}
