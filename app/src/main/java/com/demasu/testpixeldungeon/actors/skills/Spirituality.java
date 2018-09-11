package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class Spirituality extends PassiveSkillA1 {


    {
        name = "Spirituality";
        image = 25;
        tier = 1;
    }

    @Override
    protected boolean upgrade () {
        Dungeon.getHero().setMP( Dungeon.getHero().getMP() + 5 );
        Dungeon.getHero().setMMP( Dungeon.getHero().getMMP() + 5 );
        return true;
    }


    @Override
    public String info () {
        return "+5 to mana per level.\n"
                + costUpgradeInfo();
    }
}
