package com.demasu.testpixeldungeon.actors.skills;

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.hero.Hero;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Moussa on 22-Jan-17.
 */
public class ActiveSkill extends Skill {

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<>();
        if (!active && level > 0)
            actions.add(AC_ACTIVATE);
        else if (level > 0)
            actions.add(AC_DEACTIVATE);

        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        Dungeon.hero.heroSkills.lastUsed = this;
        if (Objects.equals(action, Skill.AC_ACTIVATE)) {
            active = true;
        } else if (Objects.equals(action, Skill.AC_DEACTIVATE)) {
            active = false;
        }
    }
}
