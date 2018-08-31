package com.demasu.testpixeldungeon.actors.skills;


import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.hero.Legend;
import com.demasu.testpixeldungeon.items.wands.WandOfMagicCasting;

import java.util.ArrayList;


public class Spark extends ActiveSkill3 {


    {
        name = "Spark";
        castText = "The end is neigh";
        tier = 3;
        image = 42;
        mana = 3;
    }


    @Override
    public float getAlpha () {
        return 1f;
    }

    @Override
    public ArrayList<String> actions ( Hero hero ) {
        ArrayList<String> actions = new ArrayList<String>();
        if ( level > 0 && hero.MP >= getManaCost() ) {
            actions.add( AC_CAST );
        }
        return actions;
    }

    @Override
    public void execute ( Hero hero, String action ) {
        if ( action == Skill.AC_CAST && hero.MP >= getManaCost() ) {
            //hero.MP -= getManaCost();
            //castTextYell();
            Legend.haxWand.castSpell( WandOfMagicCasting.CAST_TYPES.SPARK );
            Dungeon.hero.heroSkills.lastUsed = this;
        }
    }

    @Override
    public int getManaCost () {
        return (int) Math.ceil( mana * ( 1 + 0.25 * level ) );
    }

    @Override
    protected boolean upgrade () {
        return true;
    }


    @Override
    public String info () {
        return "Hatsune's pride...\n"
                + "Sacrifices spiritual energy to heal others.\n"

                + costUpgradeInfo();
    }

}
