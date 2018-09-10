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
import com.demasu.testpixeldungeon.actors.blobs.Blob;
import com.demasu.testpixeldungeon.actors.blobs.ParalyticGas;
import com.demasu.testpixeldungeon.actors.buffs.Paralysis;
import com.demasu.testpixeldungeon.items.quest.RatSkull;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.FetidRatSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FetidRat extends Mob {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        IMMUNITIES.add( Paralysis.class );
    }

    {
        name = "fetid rat";
        spriteClass = FetidRatSprite.class;

        HP = HT = 15;
        defenseSkill = 5;

        EXP = 3;
        maxLvl = 5;

        state = WANDERING;

        name = Dungeon.getCurrentDifficulty().mobPrefix() + name;
        HT *= Dungeon.getCurrentDifficulty().mobHPModifier();
        HP = HT;
    }

    @Override
    public int damageRoll () {
        return Random.NormalIntRange( 2, 6 );
    }

    @Override
    public int attackSkill ( Char target ) {
        return 12;
    }

    @Override
    public int dr () {
        return 2;
    }

    @Override
    public String defenseVerb () {
        return "evaded";
    }

    @Override
    public int defenseProc ( Char enemy, int damage ) {

        GameScene.add( Blob.seed( pos, 20, ParalyticGas.class ) );

        return super.defenseProc( enemy, damage );
    }

    @Override
    public void die ( Object cause ) {
        super.die( cause );

        Dungeon.getLevel().drop( new RatSkull(), pos ).sprite.drop();
    }

    @Override
    public String description () {
        return
                "This marsupial rat is much larger than a regular one. It is surrounded by a foul cloud.";
    }

    @Override
    public HashSet<Class<?>> immunities () {
        return IMMUNITIES;
    }
}
