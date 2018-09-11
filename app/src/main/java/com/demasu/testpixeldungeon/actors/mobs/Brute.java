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
import com.demasu.testpixeldungeon.actors.buffs.Terror;
import com.demasu.testpixeldungeon.items.Gold;
import com.demasu.testpixeldungeon.sprites.BruteSprite;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Brute extends Mob {

    private static final String TXT_ENRAGED = "%s becomes enraged!";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        IMMUNITIES.add( Terror.class );
    }

    private boolean enraged = false;

    {
        name = "gnoll brute";
        spriteClass = BruteSprite.class;

        setHP( setHT( 40 ) );
        defenseSkill = 15;

        EXP = 8;
        maxLvl = 15;

        loot = Gold.class;
        lootChance = 0.5f;

        name = Dungeon.getCurrentDifficulty().mobPrefix() + name;
        setHT( getHT() * Dungeon.getCurrentDifficulty().mobHPModifier() );
        setHP( getHT() );
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        enraged = getHP() < getHT() / 4;
    }

    @Override
    public int damageRoll () {
        return enraged ?
                Random.NormalIntRange( 10, 40 ) :
                Random.NormalIntRange( 8, 18 );
    }

    @Override
    public int attackSkill ( Char target ) {
        return 20;
    }

    @Override
    public int dr () {
        return 8;
    }

    @Override
    public void damage ( int dmg, Object src ) {
        super.damage( dmg, src );

        if ( isAlive() && !enraged && getHP() < getHT() / 4 ) {
            enraged = true;
            spend( TICK );
            if ( Dungeon.getVisible()[pos] ) {
                GLog.w( TXT_ENRAGED, name );
                sprite.showStatus( CharSprite.NEGATIVE, "enraged" );
            }
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
                "Brutes are the largest, strongest and toughest of all gnolls. When severely wounded, " +
                        "they go berserk, inflicting even more damage to their enemies.";
    }

    @Override
    public HashSet<Class<?>> immunities () {
        return IMMUNITIES;
    }
}
