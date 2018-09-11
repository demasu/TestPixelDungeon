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
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Amok;
import com.demasu.testpixeldungeon.actors.buffs.Terror;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.npcs.Imp;
import com.demasu.testpixeldungeon.items.KindOfWeapon;
import com.demasu.testpixeldungeon.items.food.Food;
import com.demasu.testpixeldungeon.items.weapon.melee.Knuckles;
import com.demasu.testpixeldungeon.sprites.MonkSprite;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Monk extends Mob {

    public static final String TXT_DISARM = "%s has knocked the %s from your hands!";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        IMMUNITIES.add( Amok.class );
        IMMUNITIES.add( Terror.class );
    }

    {
        name = "dwarf monk";
        spriteClass = MonkSprite.class;

        setHP( 70 );
        setHT( 70 );
        defenseSkill = 30;

        EXP = 11;
        maxLvl = 21;

        loot = new Food();
        lootChance = 0.083f;

        name = Dungeon.getCurrentDifficulty().mobPrefix() + name;
        setHT( (int) (getHT() * Dungeon.getCurrentDifficulty().mobHPModifier()) );
        setHP( getHT() );
    }

    @Override
    public int damageRoll () {
        return Random.NormalIntRange( 12, 16 );
    }

    @Override
    public int attackSkill ( Char target ) {
        return 30;
    }

    @Override
    protected float attackDelay () {
        return 0.5f;
    }

    @Override
    public int dr () {
        return 2;
    }

    @Override
    public String defenseVerb () {
        return "parried";
    }

    @Override
    public void die ( Object cause ) {
        Imp.Quest.process( this );

        super.die( cause );
    }

    @Override
    public int attackProc ( Char enemy, int damage ) {

        if ( Random.Int( 6 ) == 0 && enemy == Dungeon.getHero() ) {

            Hero hero = Dungeon.getHero();
            KindOfWeapon weapon = hero.belongings.weapon;

            if ( weapon != null && !( weapon instanceof Knuckles ) && !weapon.cursed ) {
                hero.belongings.weapon = null;
                Dungeon.getLevel().drop( weapon, hero.pos ).sprite.drop();
                GLog.w( TXT_DISARM, name, weapon.name() );
            }
        }

        champEffect( enemy, damage );


        return damage;
    }

    @Override
    public String description () {
        return
                "These monks are fanatics, who devoted themselves to protecting their city's secrets from all aliens. " +
                        "They don't use any armor or weapons, relying solely on the art of hand-to-hand combat.";
    }

    @Override
    public HashSet<Class<?>> immunities () {
        return IMMUNITIES;
    }
}
