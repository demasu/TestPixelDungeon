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
package com.demasu.testpixeldungeon.items.weapon.missiles;

import com.watabou.noosa.audio.Sample;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Charm;
import com.demasu.testpixeldungeon.actors.buffs.Paralysis;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.Bestiary;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.effects.particles.BlastParticle;
import com.demasu.testpixeldungeon.effects.particles.SmokeParticle;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CupidArrow extends Arrow {

    {
        name = "cupid arrow";
        image = ItemSpriteSheet.CupidArrow;

        stackable = true;
    }

    public CupidArrow () {
        this( 1 );
    }

    public CupidArrow ( int number ) {
        super();
        quantity = number;
    }

    @Override
    public Item random () {
        quantity = Random.Int( 3, 5 );
        return this;
    }

    @Override
    public void arrowEffect ( Char attacker, Char defender ) {
        if ( Bestiary.isBoss( defender ) ) {
            return;
        }
        int duration = Random.IntRange( 5, 10 );
        Buff.affect( defender, Charm.class, Charm.durationFactor( defender ) * duration ).object = attacker.id();
        defender.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
    }

    @Override
    public String desc () {
        return
                "An arrow believed to belong to cupid. Careful who you aim at.";
    }


    @Override
    public int price () {
        return quantity * 15;
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if ( Dungeon.hero.belongings.bow != null ) {
            if ( actions.contains( AC_THROW ) == false ) {
                actions.add( AC_THROW );
            }
        } else {
            actions.remove( AC_THROW );
        }
        actions.remove( AC_EQUIP );

        return actions;
    }
}
