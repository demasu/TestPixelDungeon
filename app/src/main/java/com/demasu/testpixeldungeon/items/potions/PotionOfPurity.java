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
package com.demasu.testpixeldungeon.items.potions;

import com.watabou.noosa.audio.Sample;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.blobs.Blob;
import com.demasu.testpixeldungeon.actors.blobs.ParalyticGas;
import com.demasu.testpixeldungeon.actors.blobs.ToxicGas;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.GasesImmunity;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.utils.BArray;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

public class PotionOfPurity extends Potion {

    private static final String TXT_FRESHNESS = "You feel uncommon freshness in the air.";
    private static final String TXT_NO_SMELL = "You've stopped sensing any smells!";

    private static final int DISTANCE = 2;

    {
        name = "Potion of Purification";
    }

    @Override
    public void shatter(int cell) {

        PathFinder.buildDistanceMap(cell, BArray.not(Level.losBlocking, null), DISTANCE);

        boolean procd = false;

        Blob[] blobs = {
                Dungeon.level.blobs.get(ToxicGas.class),
                Dungeon.level.blobs.get(ParalyticGas.class)
        };

        for (Blob blob : blobs) {

            if (blob == null) {
                continue;
            }

            for (int i = 0; i < Level.LENGTH; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE) {

                    int value = blob.cur[i];
                    if (value > 0) {

                        blob.cur[i] = 0;
                        blob.volume -= value;
                        procd = true;

                        if (Dungeon.visible[i]) {
                            CellEmitter.get(i).burst(Speck.factory(Speck.DISCOVER), 1);
                        }
                    }

                }
            }
        }

        boolean heroAffected = PathFinder.distance[Dungeon.hero.pos] < Integer.MAX_VALUE;

        if (procd) {

            if (Dungeon.visible[cell]) {
                splash(cell);
                Sample.INSTANCE.play(Assets.SND_SHATTER);
            }

            setKnown();

            if (heroAffected) {
                GLog.p(TXT_FRESHNESS);
            }

        } else {

            super.shatter(cell);

            if (heroAffected) {
                GLog.i(TXT_FRESHNESS);
                setKnown();
            }

        }
    }

    @Override
    protected void apply(Hero hero) {
        GLog.w(TXT_NO_SMELL);
        Buff.prolong(hero, GasesImmunity.class, GasesImmunity.DURATION);
        setKnown();
    }

    @Override
    public String desc() {
        return
                "This reagent will quickly neutralize all harmful gases in the area of effect. " +
                        "Drinking it will give you a temporary immunity to such gases.";
    }

    @Override
    public int price() {
        return isKnown() ? 50 * quantity : super.price();
    }
}
