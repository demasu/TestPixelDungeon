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
package com.demasu.pixeldungeonskills.items.wands;

import com.demasu.noosa.Camera;
import com.demasu.noosa.audio.Sample;
import com.demasu.pixeldungeonskills.Assets;
import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.ResultDescriptions;
import com.demasu.pixeldungeonskills.actors.Actor;
import com.demasu.pixeldungeonskills.actors.Char;
import com.demasu.pixeldungeonskills.actors.buffs.Buff;
import com.demasu.pixeldungeonskills.actors.buffs.Paralysis;
import com.demasu.pixeldungeonskills.actors.mobs.Mob;
import com.demasu.pixeldungeonskills.effects.CellEmitter;
import com.demasu.pixeldungeonskills.effects.MagicMissile;
import com.demasu.pixeldungeonskills.effects.Speck;
import com.demasu.pixeldungeonskills.levels.Level;
import com.demasu.pixeldungeonskills.mechanics.Ballistica;
import com.demasu.pixeldungeonskills.scenes.GameScene;
import com.demasu.pixeldungeonskills.utils.BArray;
import com.demasu.pixeldungeonskills.utils.GLog;
import com.demasu.pixeldungeonskills.utils.Utils;
import com.demasu.utils.Callback;
import com.demasu.utils.PathFinder;
import com.demasu.utils.Random;

public class WandOfAvalanche extends Wand {

	{
		name = "Wand of Avalanche";
		hitChars = false;
	}

	@Override
	protected void onZap( int cell ) {

		Sample.INSTANCE.play( Assets.SND_ROCKS );

		int level = power();

		Ballistica.distance = Math.min( Ballistica.distance, 8 + level );

		int size = 1 + level / 3;
		PathFinder.buildDistanceMap( cell, BArray.not( Level.solid, null ), size );

		int shake = 0;
		for (int i=0; i < Level.LENGTH; i++) {

			int d = PathFinder.distance[i];

			if (d < Integer.MAX_VALUE) {

				Char ch = Actor.findChar( i );
				if (ch != null) {

                    if(ch.sprite != null)
					    ch.sprite.flash();
					ch.damage( Random.Int( 2, 6 + (int)((size - d) * 2 * Dungeon.hero.heroSkills.passiveB2.wandDamageBonus())  ), this );

					if (ch.isAlive() && Random.Int( 2 + d ) == 0) {
						Buff.prolong( ch, Paralysis.class, Random.IntRange( 2, 6 ) );
					}
				}

				if (ch != null && ch.isAlive()) {
					if (ch instanceof Mob) {
						Dungeon.level.mobPress( (Mob)ch );
					} else {
						Dungeon.level.press( i, ch );
					}
				} else {
					Dungeon.level.press( i, null );
				}

				if (Dungeon.visible[i]) {
					CellEmitter.get(i).start(Speck.factory(Speck.ROCK), 0.07f, 3 + (size - d));
					if (Level.water[i]) {
						GameScene.ripple( i );
					}
					if (shake < size - d) {
						shake = size - d;
					}
				}
			}

			Camera.main.shake( 3, 0.07f * (3 + shake) );
		}

		if (!curUser.isAlive()) {
			Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
			GLog.n( "You killed yourself with your own Wand of Avalanche..." );
		}
	}

	protected void fx( int cell, Callback callback ) {
		MagicMissile.earth( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public String desc() {
		return
				"When a discharge of this wand hits a wall (or any other solid obstacle) it causes " +
						"an avalanche of stones, damaging and stunning all creatures in the affected area.";
	}
}
