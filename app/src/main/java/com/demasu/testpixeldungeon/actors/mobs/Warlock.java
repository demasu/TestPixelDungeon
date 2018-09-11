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
import com.demasu.testpixeldungeon.ResultDescriptions;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Weakness;
import com.demasu.testpixeldungeon.items.Generator;
import com.demasu.testpixeldungeon.items.weapon.enchantments.Death;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.mechanics.Ballistica;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.demasu.testpixeldungeon.sprites.WarlockSprite;
import com.demasu.testpixeldungeon.utils.GLog;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Warlock extends Mob implements Callback {

    private static final float TIME_TO_ZAP = 1f;

    private static final String TXT_SHADOWBOLT_KILLED = "%s's shadow bolt killed you...";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

    static {
        RESISTANCES.add( Death.class );
    }

    {
        name = "dwarf warlock";
        spriteClass = WarlockSprite.class;

        setHP( 70 );
        setHT( 70 );
        defenseSkill = 18;

        EXP = 11;
        maxLvl = 21;

        loot = Generator.Category.POTION;
        lootChance = 0.83f;

        name = Dungeon.getCurrentDifficulty().mobPrefix() + name;
        setHT( (int) (getHT() * Dungeon.getCurrentDifficulty().mobHPModifier()) );
        setHP( getHT() );
    }

    @Override
    public int damageRoll () {
        return Random.NormalIntRange( 12, 20 );
    }

    @Override
    public int attackSkill ( Char target ) {
        return 25;
    }

    @Override
    public int dr () {
        return 8;
    }

    @Override
    protected boolean canAttack ( Char enemy ) {
        return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
    }

    protected boolean doAttack ( Char enemy ) {

        if ( Level.adjacent( pos, enemy.pos ) ) {

            return super.doAttack( enemy );

        } else {

            boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
            if ( visible ) {
                ( (WarlockSprite) sprite ).zap( enemy.pos );
            } else {
                zap();
            }

            return !visible;
        }
    }

    private void zap () {
        spend( TIME_TO_ZAP );

        if ( hit( this, enemy, true ) ) {
            if ( enemy == Dungeon.getHero() && Random.Int( 2 ) == 0 ) {
                Buff.prolong( enemy, Weakness.class, Weakness.duration( enemy ) );
            }

            int dmg = Random.Int( 12, 18 );
            enemy.damage( dmg, this );

            if ( !enemy.isAlive() && enemy == Dungeon.getHero() ) {
                Dungeon.fail( Utils.format( ResultDescriptions.MOB,
                        Utils.indefinite( name ), Dungeon.getDepth() ) );
                GLog.n( TXT_SHADOWBOLT_KILLED, name );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
        }
    }

    public void onZapComplete () {
        zap();
        next();
    }

    @Override
    public void call () {
        next();
    }

    @Override
    public int attackProc ( Char enemy, int damage ) {
        champEffect( enemy, damage );
        return damage;
    }

    @Override
    public String description () {
        return
                "When dwarves' interests have shifted from engineering to arcane arts, " +
                        "warlocks have come to power in the city. They started with elemental magic, " +
                        "but soon switched to demonology and necromancy.";
    }

    @Override
    public HashSet<Class<?>> resistances () {
        return RESISTANCES;
    }
}
