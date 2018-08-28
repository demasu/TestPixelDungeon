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

import com.demasu.testpixeldungeon.sprites.ImpSprite;

public class ImpShopkeeper extends Shopkeeper {

    // --Commented out by Inspection (8/28/18, 6:22 PM):private static final String TXT_GREETINGS = "Hello, friend!";

    {
        name = "ambitious imp";
        spriteClass = ImpSprite.class;
    }

    // --Commented out by Inspection (8/28/18, 6:22 PM):private boolean seenBefore = false;

// --Commented out by Inspection START (8/28/18, 6:19 PM):
//    @Override
//    protected boolean act() {
//
//        if (!seenBefore && Dungeon.visible[pos]) {
//            yell(Utils.format(TXT_GREETINGS));
//            seenBefore = true;
//        }
//
//        return super.act();
//    }
// --Commented out by Inspection STOP (8/28/18, 6:19 PM)

// --Commented out by Inspection START (8/28/18, 6:19 PM):
//    @Override
//    protected void flee() {
//        for (Heap heap : Dungeon.level.heaps.values()) {
//            if (heap.type == Heap.Type.FOR_SALE) {
//                CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
//                heap.destroy();
//            }
//        }
//
//        destroy();
//
//        sprite.emitter().burst(Speck.factory(Speck.WOOL), 15);
//        sprite.killAndErase();
//    }
// --Commented out by Inspection STOP (8/28/18, 6:19 PM)

// --Commented out by Inspection START (8/28/18, 6:19 PM):
//    @Override
//    public String description() {
//        return
//                "Imps are lesser demons. They are notable for neither their strength nor their magic talent. " +
//                        "But they are quite smart and sociable, and many of imps prefer to live and do business among non-demons.";
//    }
// --Commented out by Inspection STOP (8/28/18, 6:19 PM)
}
