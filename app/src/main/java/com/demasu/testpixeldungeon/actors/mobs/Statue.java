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
import com.demasu.testpixeldungeon.Journal;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.blobs.ToxicGas;
import com.demasu.testpixeldungeon.actors.buffs.Poison;
import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.demasu.testpixeldungeon.items.weapon.Weapon;
import com.demasu.testpixeldungeon.items.weapon.enchantments.Death;
import com.demasu.testpixeldungeon.items.weapon.enchantments.Leech;
import com.demasu.testpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.demasu.testpixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Statue extends Mob {

    {
        name = "animated statue";
        spriteClass = StatueSprite.class;

        EXP = 0;
        state = PASSIVE;
    }

    private Weapon weapon;

    public Statue() {
        super();

        do {
            weapon = (Weapon) Generator.random(Generator.Category.WEAPON);
        } while (!(weapon instanceof MeleeWeapon) || weapon.level() < 0);

        weapon.identify();
        weapon.enchant();

        HP = HT = 15 + Dungeon.depth * 5;
        defenseSkill = 4 + Dungeon.depth;
    }

    // --Commented out by Inspection (8/29/18, 4:04 PM):private static final String WEAPON = "weapon";

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public void storeInBundle(Bundle bundle) {
//        super.storeInBundle(bundle);
//        bundle.put(WEAPON, weapon);
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public void restoreFromBundle(Bundle bundle) {
//        super.restoreFromBundle(bundle);
//        weapon = (Weapon) bundle.get(WEAPON);
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    protected boolean act() {
//        if (Dungeon.visible[pos]) {
//            Journal.add(Journal.Feature.STATUE);
//        }
//        return super.act();
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public int damageRoll() {
//        return Random.NormalIntRange(weapon.min(), weapon.max());
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public int attackSkill(Char target) {
//        return (int) ((9 + Dungeon.depth) * weapon.ACU);
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    protected float attackDelay() {
//        return weapon.DLY;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public int dr() {
//        return Dungeon.depth;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public void damage(int dmg, Object src) {
//
//        if (state == PASSIVE) {
//            state = HUNTING;
//        }
//
//        super.damage(dmg, src);
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public int attackProc(Char enemy, int damage) {
//        weapon.proc(this, enemy, damage);
//        return damage;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public void beckon(int cell) {
//        // Do nothing
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public void die(Object cause) {
//        Dungeon.level.drop(weapon, pos).sprite.drop();
//        super.die(cause);
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public void destroy() {
//        Journal.remove(Journal.Feature.STATUE);
//        super.destroy();
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public boolean reset() {
//        state = PASSIVE;
//        return true;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public String description() {
//        return
//                "You would think that it's just another ugly statue of this dungeon, but its red glowing eyes give itself away. " +
//                        "While the statue itself is made of stone, the _" + weapon.name() + "_, it's wielding, looks real.";
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(ToxicGas.class);
        RESISTANCES.add(Poison.class);
        RESISTANCES.add(Death.class);
        RESISTANCES.add(ScrollOfPsionicBlast.class);
        IMMUNITIES.add(Leech.class);
    }

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public HashSet<Class<?>> resistances() {
//        return RESISTANCES;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)

// --Commented out by Inspection START (8/29/18, 12:42 PM):
//    @Override
//    public HashSet<Class<?>> immunities() {
//        return IMMUNITIES;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:42 PM)
}
