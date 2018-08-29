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
package com.demasu.testpixeldungeon.actors.mobs.npcs;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Char;
import com.demasu.testpixeldungeon.actors.buffs.Buff;

import com.demasu.testpixeldungeon.sprites.RatKingSprite;

public class RatKing extends NPC {

    {
        name = "rat king";
        spriteClass = RatKingSprite.class;

        state = SLEEPEING;
    }

// --Commented out by Inspection START (8/29/18, 12:51 PM):
//    @Override
//    public int defenseSkill() {
//        return 1000;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:51 PM)

// --Commented out by Inspection START (8/29/18, 12:51 PM):
//    @Override
//    public float speed() {
//        return 2f;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:51 PM)

// --Commented out by Inspection START (8/29/18, 12:51 PM):
//    @Override
//    protected Char chooseEnemy() {
//        return null;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:51 PM)

// --Commented out by Inspection START (8/29/18, 12:51 PM):
//    @Override
//    public void damage(int dmg, Object src) {
//    }
// --Commented out by Inspection STOP (8/29/18, 12:51 PM)

// --Commented out by Inspection START (8/29/18, 12:51 PM):
//    @Override
//    public void add(Buff buff) {
//    }
// --Commented out by Inspection STOP (8/29/18, 12:51 PM)

// --Commented out by Inspection START (8/29/18, 12:51 PM):
//    @Override
//    public boolean reset() {
//        return true;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:51 PM)

// --Commented out by Inspection START (8/29/18, 12:51 PM):
//    @Override
    public void interact() {
        sprite.turnTo(pos, Dungeon.hero.pos);
        if (state == SLEEPEING) {
            notice();
            yell("I'm not sleeping!");
            state = WANDERING;
        } else {
            yell("What is it? I have no time for this nonsense. My kingdom won't rule itself!");
        }
    }
// --Commented out by Inspection STOP (8/29/18, 12:51 PM)

// --Commented out by Inspection START (8/29/18, 12:51 PM):
//    @Override
//    public String description() {
//        return
//                "This rat is a little bigger than a regular marsupial rat " +
//                        "and it's wearing a tiny crown on its head.";
//    }
// --Commented out by Inspection STOP (8/29/18, 12:51 PM)
}
