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
package com.demasu.testpixeldungeon.actors.mobs;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Burning;
import com.demasu.testpixeldungeon.actors.buffs.Poison;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.items.potions.PotionOfHealing;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.levels.Terrain;
import com.demasu.testpixeldungeon.levels.features.Door;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Swarm extends Mob {

    private static final float SPLIT_DELAY = 1f;
    private static final String GENERATION = "generation";
    int generation = 0;

    {
        name = "swarm of flies";
        spriteClass = SwarmSprite.class;

        setHP( setHT( 80 ) );
        defenseSkill = 5;

        maxLvl = 10;

        flying = true;

        name = Dungeon.getCurrentDifficulty().mobPrefix() + name;
        setHT( getHT() * Dungeon.getCurrentDifficulty().mobHPModifier() );
        setHP( getHT() );
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( GENERATION, generation );
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        generation = bundle.getInt( GENERATION );
    }

    @Override
    public int damageRoll () {
        return Random.NormalIntRange( 1, 4 );
    }

    @Override
    public int defenseProc ( Char enemy, int damage ) {

        if ( getHP() >= damage + 2 ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();
            boolean[] passable = Level.passable;

            int[] neighbours = { pos + 1, pos - 1, pos + Level.WIDTH, pos - Level.WIDTH };
            for ( int n : neighbours ) {
                if ( passable[n] && Actor.findChar( n ) == null ) {
                    candidates.add( n );
                }
            }

            if ( candidates.size() > 0 ) {

                Swarm clone = split();
                clone.setHP( ( getHP() - damage ) / 2 );
                clone.pos = Random.element( candidates );
                clone.state = clone.HUNTING;

                if ( Dungeon.getLevel().map[clone.pos] == Terrain.DOOR ) {
                    Door.enter( clone.pos );
                }

                GameScene.add( clone, SPLIT_DELAY );
                Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

                setHP( getHP() - clone.getHP() );
            }
        }

        return damage;
    }

    @Override
    public int attackSkill ( Char target ) {
        return 12;
    }

    @Override
    public String defenseVerb () {
        return "evaded";
    }

    private Swarm split () {
        Swarm clone = new Swarm();
        clone.generation = generation + 1;
        if ( buff( Burning.class ) != null ) {
            Buff.affect( clone, Burning.class ).reignite( clone );
        }
        if ( buff( Poison.class ) != null ) {
            Buff.affect( clone, Poison.class ).set( 2 );
        }
        return clone;
    }

    @Override
    protected void dropLoot () {
        if ( Random.Int( 5 * ( generation + 1 ) ) == 0 ) {
            Dungeon.getLevel().drop( new PotionOfHealing(), pos ).sprite.drop();
        }
    }

    @Override
    public int attackProc ( Char enemy, int damage ) {
        champEffect( enemy, damage );
        return damage;
    }

    @Override
    public String description () {
        return
                "The deadly swarm of flies buzzes angrily. Every non-magical attack " +
                        "will split it into two smaller but equally dangerous swarms.";
    }
}
