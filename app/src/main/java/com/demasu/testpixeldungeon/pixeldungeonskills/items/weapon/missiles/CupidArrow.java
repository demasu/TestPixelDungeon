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
package com.demasu.pixeldungeonskills.items.weapon.missiles;

import com.demasu.noosa.audio.Sample;
import com.demasu.pixeldungeonskills.Assets;
import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.actors.Actor;
import com.demasu.pixeldungeonskills.actors.Char;
import com.demasu.pixeldungeonskills.actors.buffs.Buff;
import com.demasu.pixeldungeonskills.actors.buffs.Charm;
import com.demasu.pixeldungeonskills.actors.buffs.Paralysis;
import com.demasu.pixeldungeonskills.actors.hero.Hero;
import com.demasu.pixeldungeonskills.actors.mobs.Bestiary;
import com.demasu.pixeldungeonskills.effects.CellEmitter;
import com.demasu.pixeldungeonskills.effects.Speck;
import com.demasu.pixeldungeonskills.effects.particles.BlastParticle;
import com.demasu.pixeldungeonskills.effects.particles.SmokeParticle;
import com.demasu.pixeldungeonskills.items.Item;
import com.demasu.pixeldungeonskills.levels.Level;
import com.demasu.pixeldungeonskills.levels.Terrain;
import com.demasu.pixeldungeonskills.scenes.GameScene;
import com.demasu.pixeldungeonskills.sprites.ItemSpriteSheet;
import com.demasu.utils.Random;

import java.util.ArrayList;

public class CupidArrow extends Arrow {

	{
		name = "cupid arrow";
		image = ItemSpriteSheet.CupidArrow;

        stackable = true;
	}

	public CupidArrow() {
		this( 1 );
	}

	public CupidArrow(int number) {
		super();
		quantity = number;
	}

    @Override
    public Item random() {
        quantity = Random.Int(3, 5);
        return this;
    }

    @Override
    public void arrowEffect(Char attacker, Char defender)
    {
        if(Bestiary.isBoss(defender))
            return;
        int duration = Random.IntRange( 5, 10 );
        Buff.affect( defender, Charm.class, Charm.durationFactor( defender ) * duration ).object = attacker.id();
        defender.sprite.centerEmitter().start( Speck.factory(Speck.HEART), 0.2f, 5 );
    }

	@Override
	public String desc() {
		return 
			"An arrow believed to belong to cupid. Careful who you aim at.";
	}
	

	@Override
	public int price() {
		return quantity * 15;
	}

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if(Dungeon.hero.belongings.bow != null) {
            if(actions.contains(AC_THROW) == false)
            actions.add(AC_THROW);
        }
        else
            actions.remove( AC_THROW );
        actions.remove(AC_EQUIP);

        return actions;
    }
}
