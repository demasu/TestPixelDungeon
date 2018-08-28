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
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Burning;
import com.demasu.testpixeldungeon.actors.buffs.Frost;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.demasu.testpixeldungeon.items.wands.WandOfFirebolt;
import com.demasu.testpixeldungeon.items.weapon.enchantments.Fire;
import com.demasu.testpixeldungeon.sprites.ElementalSprite;
import com.watabou.utils.Random;

public class Elemental extends Mob {

    {
        name = "fire elemental";
        spriteClass = ElementalSprite.class;

        HP = HT = 65;
        defenseSkill = 20;

        EXP = 10;
        maxLvl = 20;

        flying = true;

        loot = new PotionOfLiquidFlame();
        lootChance = 0.1f;

        name = Dungeon.currentDifficulty.mobPrefix() + name;
        HT *= Dungeon.currentDifficulty.mobHPModifier();
        HP = HT;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(16, 20);
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int dr() {
        return 5;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(enemy, Burning.class).reignite(enemy);
        }

        champEffect(enemy, damage);

        return damage;
    }

    @Override
    public void add(Buff buff) {
        if (buff instanceof Burning) {
            if (HP < HT) {
                HP++;
                sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            }
        } else {
            if (buff instanceof Frost) {
                damage(Random.NormalIntRange(1, HT * 2 / 3), buff);
            }
            super.add(buff);
        }
    }

    @Override
    public String description() {
        return
                "Wandering fire elementals are a byproduct of summoning greater entities. " +
                        "They are too chaotic in their nature to be controlled by even the most powerful demonologist.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        IMMUNITIES.add(Burning.class);
        IMMUNITIES.add(Fire.class);
        IMMUNITIES.add(WandOfFirebolt.class);
        IMMUNITIES.add(ScrollOfPsionicBlast.class);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
