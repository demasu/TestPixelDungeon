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

import java.util.HashSet;

public class Statue extends Mob {

    private static final String WEAPON = "weapon";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        RESISTANCES.add( ToxicGas.class );
        RESISTANCES.add( Poison.class );
        RESISTANCES.add( Death.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
        IMMUNITIES.add( Leech.class );
    }

    private Weapon weapon;

    {
        name = "animated statue";
        spriteClass = StatueSprite.class;

        EXP = 0;
        state = PASSIVE;
    }

    public Statue () {
        super();

        do {
            weapon = (Weapon) Generator.random( Generator.Category.WEAPON );
        } while ( !( weapon instanceof MeleeWeapon ) || weapon.level() < 0 );

        weapon.identify();
        weapon.enchant();

        setHP( setHT( 15 + Dungeon.getDepth() * 5 ) );
        defenseSkill = 4 + Dungeon.getDepth();
    }

    @Override
    public void storeInBundle ( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( WEAPON, weapon );
    }

    @Override
    public void restoreFromBundle ( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        weapon = (Weapon) bundle.get( WEAPON );
    }

    @Override
    protected boolean act () {
        if ( Dungeon.getVisible()[pos] ) {
            Journal.add( Journal.Feature.STATUE );
        }
        return super.act();
    }

    @Override
    public int damageRoll () {
        return Random.NormalIntRange( weapon.min(), weapon.max() );
    }

    @Override
    public int attackSkill ( Char target ) {
        return (int) ( ( 9 + Dungeon.getDepth() ) * weapon.ACU );
    }

    @Override
    protected float attackDelay () {
        return weapon.DLY;
    }

    @Override
    public int dr () {
        return Dungeon.getDepth();
    }

    @Override
    public void damage ( int dmg, Object src ) {

        if ( state == PASSIVE ) {
            state = HUNTING;
        }

        super.damage( dmg, src );
    }

    @Override
    public int attackProc ( Char enemy, int damage ) {
        weapon.proc( this, enemy, damage );
        return damage;
    }

    @Override
    public void beckon ( int cell ) {
        // Do nothing
    }

    @Override
    public void die ( Object cause ) {
        Dungeon.getLevel().drop( weapon, pos ).sprite.drop();
        super.die( cause );
    }

    @Override
    public void destroy () {
        Journal.remove( Journal.Feature.STATUE );
        super.destroy();
    }

    @Override
    public boolean reset () {
        state = PASSIVE;
        return true;
    }

    @Override
    public String description () {
        return
                "You would think that it's just another ugly statue of this dungeon, but its red glowing eyes give itself away. " +
                        "While the statue itself is made of stone, the _" + weapon.name() + "_, it's wielding, looks real.";
    }

    @Override
    public HashSet<Class<?>> resistances () {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities () {
        return IMMUNITIES;
    }
}
