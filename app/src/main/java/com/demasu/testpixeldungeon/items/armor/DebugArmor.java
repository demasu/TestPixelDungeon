package com.demasu.testpixeldungeon.items.armor;

import com.demasu.testpixeldungeon.sprites.ItemSpriteSheet;

public class DebugArmor extends Armor {

    {
        name = "Debug armor";
        image = ItemSpriteSheet.ARMOR_PLATE;
    }

    public DebugArmor () {
        super( 5 );
    }

    @Override
    public String desc () {
        return "This armor is for debugging everything.\n" +
                "It should hopefully protect you a bit.";
    }
}
