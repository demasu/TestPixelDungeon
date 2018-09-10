package com.demasu.testpixeldungeon.actors.mobs.npcs;

import com.demasu.testpixeldungeon.actors.Char;

/**
 * Created by Moussa on 22-Jan-17.
 */
public class ShadowClone extends MirrorImage {
    int lifeSpan = 3;

    {
        name = "Shadow Clone";
    }

    @Override
    public int attackProc ( Char enemy, int damage ) {
        int dmg = super.attackProc( enemy, damage );
        lifeSpan--;
        if ( lifeSpan < 1 ) {
            destroy();
            sprite.die();
        }
        return dmg;
    }

    @Override
    public String description () {
        return
                "Shadow clones share your strength and cannot be attacked. \nThey will however disappear after dealing some punishment.";
    }

}
