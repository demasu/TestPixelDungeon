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
package com.demasu.testpixeldungeon.items.rings;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.CellEmitter;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;
import com.demasu.testpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

// --Commented out by Inspection START (8/29/18, 12:38 PM):
// --Commented out by Inspection START (8/29/18, 12:38 PM):
////public class GemStone extends Ring {
////
////    {
////        name = "Gemstone";
////    }
////
////
////    private int charge = 0;
////
////// --Commented out by Inspection START (8/29/18, 12:38 PM):
//////    public void ChargeUp() {
//////        charge++;
//////        if (charge == 20) {
//////            image = ItemSpriteSheet.GemStonePart;
//////            GLog.p("Gem stone partially charged!");
//////        }
//////        if (charge == 40) {
//////            image = ItemSpriteSheet.GemStoneFull;
//////            GLog.p("Gem stone fully charged!");
//////        }
//////    }
////// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
////
////// --Commented out by Inspection START (8/29/18, 12:38 PM):
//////    @Override
//////    public void execute(Hero hero, String action) {
//////        if (action.equals("Activate")) {
//////            image = ItemSpriteSheet.GemStone;
//////            Dungeon.hero.HP += charge;
//////            if (Dungeon.hero.HP > Dungeon.hero.HT)
//////                Dungeon.hero.HP = Dungeon.hero.HT;
//////            CellEmitter.center(hero.pos).burst(Speck.factory(Speck.HEALING), 1);
//////            if (charge == 40) {
//////                GLog.p("Gemstone fully healed you!");
//////                Dungeon.hero.HP = Dungeon.hero.HT;
//////            } else
//////                GLog.p("Gemstone heals for " + charge + "HP!");
//////            charge = 0;
//////        } else {
//////
//////            super.execute(hero, action);
//////
//////        }
//////    }
////// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
////
////// --Commented out by Inspection START (8/29/18, 12:38 PM):
//////    @Override
//////    public void storeInBundle(Bundle bundle) {
//////        super.storeInBundle(bundle);
//////        bundle.put("charge", charge);
//////    }
////// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
////
////// --Commented out by Inspection START (8/29/18, 12:38 PM):
//////    @Override
//////    public void restoreFromBundle(Bundle bundle) {
//////        super.restoreFromBundle(bundle);
//////        charge = bundle.getInt("charge");
//////        if (charge > 19) {
//////            image = ItemSpriteSheet.GemStonePart;
//////        }
//////        if (charge == 40) {
//////            image = ItemSpriteSheet.GemStoneFull;
//////        }
//////    }
////// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
////
////// --Commented out by Inspection START (8/29/18, 12:38 PM):
//////    @Override
//////    public Item random() {
//////        level = +1;
//////        return this;
//////    }
////// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
////
////// --Commented out by Inspection START (8/29/18, 12:38 PM):
//////    @Override
//////    protected RingBuff buff() {
//////        return new GemStoneBuff();
//////    }
////// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
////
////// --Commented out by Inspection START (8/29/18, 12:38 PM):
//////    @Override
//////    public boolean doPickUp(Hero hero) {
//////        identify();
//////        return super.doPickUp(hero);
//////    }
////// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
////
////// --Commented out by Inspection START (8/29/18, 12:38 PM):
//////    @Override
// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
////    public boolean isUpgradable() {
////        return false;
////    }
//// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
//
//// --Commented out by Inspection START (8/29/18, 12:38 PM):
////    @Override
////    public void use() {
////        // Do nothing (it can't degrade)
// --Commented out by Inspection STOP (8/29/18, 12:38 PM)
//    }
// --Commented out by Inspection STOP (8/29/18, 12:38 PM)

//    @Override
//    protected String desc() {
//        return isKnown() ?
//                "The Gemstone is a unique artifact capable of storing life energy for later use." +
//                        " Although it does not damage the wearer, it drains away any rejuvenation his body possesses." +
//                        " That is until it fully charges and becomes a life savior in times of need.\n" +
//                        " The Gemstone's current charge is " + charge + "/40" :
//                super.desc();
//    }

// --Commented out by Inspection START (8/29/18, 12:38 PM):
//    @Override
//    public ArrayList<String> actions(Hero hero) {
//        ArrayList<String> actions = super.actions(hero);
//        if (charge > 19)
//            actions.add("Activate");
//        return actions;
//    }
// --Commented out by Inspection STOP (8/29/18, 12:38 PM)

// --Commented out by Inspection START (8/29/18, 12:38 PM):
//    @Override
//    public String info() {
//        if (isEquipped(Dungeon.hero)) {
//
//            return desc();
//
//        } else if (cursed && cursedKnown) {
//
//            return desc() + "\n\nYou can feel a malevolent magic lurking within the " + name() + ".";
//
//        } else {
//
//            return desc();
//
//        }
//    }
// --Commented out by Inspection STOP (8/29/18, 12:38 PM)

//    class GemStoneBuff extends RingBuff {
//    }
//}
