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
package com.demasu.pixeldungeonskills.levels.features;

import com.demasu.noosa.audio.Sample;
import com.demasu.pixeldungeonskills.Assets;
import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.levels.Level;
import com.demasu.pixeldungeonskills.levels.Terrain;
import com.demasu.pixeldungeonskills.scenes.GameScene;

public class Door {

	public static void enter( int pos ) {
		Level.set( pos, Terrain.OPEN_DOOR );
		GameScene.updateMap( pos );
		Dungeon.observe();
		
		if (Dungeon.visible[pos]) {
			Sample.INSTANCE.play( Assets.SND_OPEN );
		}
	}
	
	public static void leave( int pos ) {
		if (Dungeon.level.heaps.get( pos ) == null) {
			Level.set( pos, Terrain.DOOR );
			GameScene.updateMap( pos );
			Dungeon.observe();
		}
	}
}
