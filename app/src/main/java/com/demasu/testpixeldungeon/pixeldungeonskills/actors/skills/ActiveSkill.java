package com.demasu.pixeldungeonskills.actors.skills;

import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.actors.hero.Hero;

import java.util.ArrayList;

/**
 * Created by Moussa on 22-Jan-17.
 */
public class ActiveSkill extends Skill {

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = new ArrayList<String>();
        if(active == false && level > 0)
            actions.add(AC_ACTIVATE);
        else if(level > 0)
            actions.add(AC_DEACTIVATE);

        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        Dungeon.hero.heroSkills.lastUsed = this;
        if(action == Skill.AC_ACTIVATE)
        {
            active = true;
        }
        else    if(action == Skill.AC_DEACTIVATE)
        {
            active = false;
        }
    }
}
