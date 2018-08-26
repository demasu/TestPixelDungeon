package com.demasu.pixeldungeonskills.actors.skills;


import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.actors.Actor;
import com.demasu.pixeldungeonskills.actors.hero.Hero;
import com.demasu.pixeldungeonskills.actors.mobs.npcs.MirrorImage;
import com.demasu.pixeldungeonskills.actors.mobs.npcs.NPC;
import com.demasu.pixeldungeonskills.effects.CellEmitter;
import com.demasu.pixeldungeonskills.effects.particles.ElmoParticle;
import com.demasu.pixeldungeonskills.items.wands.WandOfBlink;
import com.demasu.pixeldungeonskills.levels.Level;
import com.demasu.pixeldungeonskills.scenes.GameScene;
import com.demasu.pixeldungeonskills.ui.StatusPane;
import com.demasu.utils.Random;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class ShadowClone extends ActiveSkill3{


    {
        name = "Shadow Clone";
        castText = "Wait till you the harem version...";
        tier = 3;
        image = 67;
        mana = 3;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = new ArrayList<String>();
        if(level > 0 && hero.MP >= getManaCost())
            actions.add(AC_CAST);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == Skill.AC_CAST) {
            ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

            for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
                int p = hero.pos + Level.NEIGHBOURS8[i];
                if(p < 0 || p >= Level.passable.length)
                    continue;
                if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
                    respawnPoints.add(p);
                }
            }

            int nImages = level;
            while (nImages > 0 && respawnPoints.size() > 0) {
                int index = Random.index(respawnPoints);

                com.demasu.pixeldungeonskills.actors.mobs.npcs.ShadowClone mob = new  com.demasu.pixeldungeonskills.actors.mobs.npcs.ShadowClone();
                mob.duplicate(hero);

                GameScene.add(mob);
                WandOfBlink.appear(mob, respawnPoints.get(index));
                CellEmitter.get(mob.pos).burst(ElmoParticle.FACTORY, 4);
                respawnPoints.remove(index);
                nImages--;
            }

            hero.MP -= getManaCost();
            StatusPane.manaDropping += getManaCost();
            castTextYell();
            Dungeon.hero.heroSkills.lastUsed = this;
            hero.spend( TIME_TO_USE );
            hero.busy();
            hero.sprite.operate( hero.pos );
        }
    }

    @Override
    public int getManaCost()
    {
        return (int)Math.ceil(mana * (1 + 0.55 * level));
    }

    @Override
    protected boolean upgrade()
    {
        return true;
    }


    @Override
    public String info()
    {
        return "Creates clones to fight for you.\n"
                + costUpgradeInfo();
    }

}
