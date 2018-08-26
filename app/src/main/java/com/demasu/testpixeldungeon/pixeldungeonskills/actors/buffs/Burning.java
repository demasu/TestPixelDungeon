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
package com.demasu.pixeldungeonskills.actors.buffs;

import com.demasu.pixeldungeonskills.Badges;
import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.ResultDescriptions;
import com.demasu.pixeldungeonskills.actors.Char;
import com.demasu.pixeldungeonskills.actors.blobs.Blob;
import com.demasu.pixeldungeonskills.actors.blobs.Fire;
import com.demasu.pixeldungeonskills.actors.hero.Hero;
import com.demasu.pixeldungeonskills.actors.mobs.Thief;
import com.demasu.pixeldungeonskills.effects.particles.ElmoParticle;
import com.demasu.pixeldungeonskills.items.Heap;
import com.demasu.pixeldungeonskills.items.Item;
import com.demasu.pixeldungeonskills.items.food.ChargrilledMeat;
import com.demasu.pixeldungeonskills.items.food.MysteryMeat;
import com.demasu.pixeldungeonskills.items.rings.RingOfElements.Resistance;
import com.demasu.pixeldungeonskills.items.scrolls.Scroll;
import com.demasu.pixeldungeonskills.levels.Level;
import com.demasu.pixeldungeonskills.scenes.GameScene;
import com.demasu.pixeldungeonskills.ui.BuffIndicator;
import com.demasu.pixeldungeonskills.utils.GLog;
import com.demasu.pixeldungeonskills.utils.Utils;
import com.demasu.utils.Bundle;
import com.demasu.utils.Random;

public class Burning extends Buff implements Hero.Doom {

	private static final String TXT_BURNS_UP		= "%s burns up!";
	private static final String TXT_BURNED_TO_DEATH	= "You burned to death...";
	
	private static final float DURATION = 8f;
	
	private float left;
	
	private static final String LEFT	= "left";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat( LEFT );
	}
	
	@Override
	public boolean act() {
		
		if (target.isAlive()) {
			
			if (target instanceof Hero) {
				Buff.prolong( target, Light.class, TICK * 1.01f );
			}

			target.damage( Random.Int( 1, 5 ), this );
			
			if (target instanceof Hero) {
				
				Item item = ((Hero)target).belongings.randomUnequipped();
				if (item instanceof Scroll) {
					
					item = item.detach( ((Hero)target).belongings.backpack );
					GLog.w( TXT_BURNS_UP, item.toString() );
					
					Heap.burnFX( target.pos );
					
				} else if (item instanceof MysteryMeat) {
					
					item = item.detach( ((Hero)target).belongings.backpack );
					ChargrilledMeat steak = new ChargrilledMeat(); 
					if (!steak.collect( ((Hero)target).belongings.backpack )) {
						Dungeon.level.drop( steak, target.pos ).sprite.drop();
					}
					GLog.w( TXT_BURNS_UP, item.toString() );
					
					Heap.burnFX( target.pos );
					
				}
				
			} else if (target instanceof Thief && ((Thief)target).item instanceof Scroll) {
				
				((Thief)target).item = null;
				target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
			}

		} else {
			detach();
		}
		
		if (Level.flamable[target.pos]) {
			GameScene.add( Blob.seed( target.pos, 4, Fire.class ) );
		}
		
		spend( TICK );
		left -= TICK;
		
		if (left <= 0 ||
			Random.Float() > (2 + (float)target.HP / target.HT) / 3 ||
			(Level.water[target.pos] && !target.flying)) {
			
			detach();
		}

		return true;
	}
	
	public void reignite( Char ch ) {
		left = duration( ch );
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}
	
	@Override
	public String toString() {
		return "Burning";
	}

	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

	@Override
	public void onDeath() {
		
		Badges.validateDeathFromFire();
		
		Dungeon.fail( Utils.format( ResultDescriptions.BURNING, Dungeon.depth ) );
		GLog.n( TXT_BURNED_TO_DEATH );
	}
}
