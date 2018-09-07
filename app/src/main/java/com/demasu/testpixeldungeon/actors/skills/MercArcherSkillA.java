package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.sprites.CharSprite;

/**
 * Created by Moussa on 25-Jan-17.
 */
public class MercArcherSkillA extends KneeShot {
    {
        tag = "mercA";
    }

    @Override
    protected boolean upgrade () {
        return false;
    }

    @Override
    public void castTextYell () {
        if ( castText != "" ) {
            Dungeon.getHero().hiredMerc.sprite.showStatus( CharSprite.NEUTRAL, castText );
        }
    }


    @Override
    public String info () {
        return "Aims for weak spots crippling targets.\n";
    }
}
