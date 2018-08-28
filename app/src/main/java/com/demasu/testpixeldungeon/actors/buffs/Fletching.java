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
package com.demasu.testpixeldungeon.actors.buffs;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.hero.HeroClass;
import com.demasu.testpixeldungeon.items.weapon.missiles.Arrow;
import com.demasu.testpixeldungeon.utils.GLog;

public class Fletching extends Buff {


    @Override
    public boolean act() {
        if (target.isAlive()) {


            Hero hero = (Hero) target;

            if (hero.heroSkills.passiveA1.fletching() < 1)// Huntress fletching if present
            {
                spend(100);
                return true;
            }

            GLog.p("Fletched an arrow!");
            Arrow arrow = new Arrow();
            if (!arrow.collect())
                Dungeon.level.drop(arrow, hero.pos).sprite.drop();


            spend(100 - hero.heroSkills.passiveA1.fletching() * 10);

        } else {

            diactivate();

        }

        return true;
    }


}
