package com.demasu.testpixeldungeon.actors.skills;


import com.demasu.testpixeldungeon.actors.hero.Hero;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class SoulFury extends ActiveSkill3 {


    {
        name = "Soul Fury";
        castText = "Forgive me girls";
        tier = 3;
        image = 123;
        mana = 3;
    }


    @Override
    public float getAlpha() {
        return 1f;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {

        return new ArrayList<>();
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action == Skill.AC_CAST) {
            getManaCost();
        }
    }

    @Override
    public int getManaCost() {
        return (int) Math.ceil(mana * (1 + 0.25 * level));
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Sets spiritual energy ablaze unlocking god-like powers.\n"
                + "No one ever survived Soul Fury...\n";
    }

}
