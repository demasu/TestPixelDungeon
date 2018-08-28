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

import java.util.HashSet;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Amok;
import com.demasu.testpixeldungeon.actors.buffs.Sleep;
import com.demasu.testpixeldungeon.actors.buffs.Terror;
import com.demasu.testpixeldungeon.actors.mobs.npcs.Imp;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.demasu.testpixeldungeon.sprites.GolemSprite;
import com.watabou.utils.Random;

public class Golem extends Mob {

    {
        name = "golem";
        spriteClass = GolemSprite.class;

        HP = HT = 85;
        defenseSkill = 18;

        EXP = 12;
        maxLvl = 22;

        name = Dungeon.currentDifficulty.mobPrefix() + name;
        HT *= Dungeon.currentDifficulty.mobHPModifier();
        HP = HT;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(20, 40);
    }

    @Override
    public int attackSkill(Char target) {
        return 28;
    }

    @Override
    protected float attackDelay() {
        return 1.5f;
    }

    @Override
    public int dr() {
        return 12;
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public void die(Object cause) {
        Imp.Quest.process(this);

        super.die(cause);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        champEffect(enemy, damage);
        return damage;
    }

    @Override
    public String description() {
        return
                "The Dwarves tried to combine their knowledge of mechanisms with their newfound power of elemental binding. " +
                        "They used spirits of earth as the \"soul\" for the mechanical bodies of golems, which were believed to be " +
                        "most controllable of all. Despite this, the tiniest mistake in the ritual could cause an outbreak.";
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(ScrollOfPsionicBlast.class);
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Terror.class);
        IMMUNITIES.add(Sleep.class);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
