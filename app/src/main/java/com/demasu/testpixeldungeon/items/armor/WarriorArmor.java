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
package com.demasu.testpixeldungeon.items.armor;

import com.watabou.noosa.Camera;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;
import com.demasu.testpixeldungeon.actors.buffs.Fury;
import com.demasu.testpixeldungeon.actors.buffs.Invisibility;
import com.demasu.testpixeldungeon.actors.buffs.Paralysis;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.hero.HeroClass;
import com.demasu.testpixeldungeon.actors.hero.HeroSubClass;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.mechanics.Ballistica;
import com.demasu.testpixeldungeon.scenes.CellSelector;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WarriorArmor extends ClassArmor {

    private static final int LEAP_TIME = 1;
    private static final int SHOCK_TIME = 3;

    private static final String AC_SPECIAL = "HEROIC LEAP";

    private static final String TXT_NOT_WARRIOR = "Only warriors can use this armor!";

    {
        name = "warrior suit of armor";
        image = ItemSpriteSheet.ARMOR_WARRIOR;
    }

    @Override
    public String special() {
        return AC_SPECIAL;
    }

    @Override
    public void doSpecial() {
        GameScene.selectCell(leaper);
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (hero.heroClass == HeroClass.WARRIOR) {
            return super.doEquip(hero);
        } else {
            GLog.w(TXT_NOT_WARRIOR);
            return false;
        }
    }

    @Override
    public String desc() {
        return
                "While this armor looks heavy, it allows a warrior to perform heroic leap towards " +
                        "a targeted location, slamming down to stun all neighbouring enemies.";
    }

    private static final CellSelector.Listener leaper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            if (target != null && target != curUser.pos) {

                int cell = Ballistica.cast(curUser.pos, target, false, true);
                if (Actor.findChar(cell) != null && cell != curUser.pos) {
                    cell = Ballistica.trace[Ballistica.distance - 2];
                }

                curUser.HP -= (curUser.HP / 3);
                if (curUser.subClass == HeroSubClass.BERSERKER && curUser.HP <= curUser.HT * Fury.LEVEL) {
                    Buff.affect(curUser, Fury.class);
                }

                Invisibility.dispel();

                final int dest = cell;
                curUser.busy();
                curUser.sprite.jump(curUser.pos, cell, new Callback() {
                    @Override
                    public void call() {
                        curUser.move(dest);
                        Dungeon.level.press(dest, curUser);
                        Dungeon.observe();

                        for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
                            Char mob = Actor.findChar(curUser.pos + Level.NEIGHBOURS8[i]);
                            if (mob != null && mob != curUser) {
                                Buff.prolong(mob, Paralysis.class, SHOCK_TIME);
                            }
                        }

                        CellEmitter.center(dest).burst(Speck.factory(Speck.DUST), 10);
                        Camera.main.shake(2, 0.5f);

                        curUser.spendAndNext(LEAP_TIME);
                    }
                });
            }
        }

        @Override
        public String prompt() {
            return "Choose direction to leap";
        }
    };
}
