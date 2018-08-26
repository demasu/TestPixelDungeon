package com.demasu.pixeldungeonskills.actors.skills;


import com.demasu.noosa.tweeners.AlphaTweener;
import com.demasu.pixeldungeonskills.Dungeon;
import com.demasu.pixeldungeonskills.actors.Actor;
import com.demasu.pixeldungeonskills.actors.hero.Hero;
import com.demasu.pixeldungeonskills.actors.hero.Legend;
import com.demasu.pixeldungeonskills.actors.mobs.npcs.SummonedPet;
import com.demasu.pixeldungeonskills.effects.CellEmitter;
import com.demasu.pixeldungeonskills.effects.particles.ElmoParticle;
import com.demasu.pixeldungeonskills.items.wands.WandOfBlink;
import com.demasu.pixeldungeonskills.levels.Level;
import com.demasu.pixeldungeonskills.scenes.GameScene;
import com.demasu.pixeldungeonskills.sprites.LegendSprite;
import com.demasu.pixeldungeonskills.sprites.MirrorSprite;
import com.demasu.pixeldungeonskills.ui.StatusPane;
import com.demasu.utils.Random;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class Echo extends ActiveSkill3{


    {
        name = "Echo";
        castText = "The past will haunt me forever";
        tier = 3;
        image = 121;
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

            int nImages = 1;
            while (nImages > 0 && respawnPoints.size() > 0) {
                int index = Random.index(respawnPoints);

                SummonedPet minion = new SummonedPet(MirrorSprite.class);
                minion.name = "Hatsune's Echo";
                minion.screams = false;
                minion.HT = 50;
                minion.HP = 50;
                minion.defenseSkill = Dungeon.hero.defenseSkill(Dungeon.hero);
                GameScene.add(minion);
                WandOfBlink.appear(minion, respawnPoints.get(index));
                minion.sprite.alpha(0);
                minion.sprite.parent.add(new AlphaTweener(minion.sprite, 1, 0.15f));
                CellEmitter.get(minion.pos).burst(ElmoParticle.FACTORY, 4);

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
        return (int)Math.ceil(mana * (1 + 0.7 * level));
    }

    @Override
    protected boolean upgrade()
    {
        return true;
    }


    @Override
    public String info()
    {
        return "An echo of a memory you should not have survived.\n"
                + costUpgradeInfo();
    }

}
