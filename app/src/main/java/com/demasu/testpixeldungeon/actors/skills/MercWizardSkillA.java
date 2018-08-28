package com.demasu.testpixeldungeon.actors.skills;

import com.watabou.noosa.tweeners.AlphaTweener;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.Actor;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.actors.mobs.npcs.SummonedPet;
import com.demasu.testpixeldungeon.effects.Pushing;
import com.demasu.testpixeldungeon.levels.Level;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by Moussa on 25-Jan-17.
 */
public class MercWizardSkillA extends SummonRat {
    {
        tag = "mercA";
    }

    @Override
    protected boolean upgrade() {
        return false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<String>();
        return actions;
    }


    @Override
    public void mercSummon() {
        boolean spawned = false;
        for (int nu = 0; nu < 1; nu++) {
            int newPos = Dungeon.hero.hiredMerc.pos;
            if (Actor.findChar(newPos) != null) {
                ArrayList<Integer> candidates = new ArrayList<Integer>();
                boolean[] passable = Level.passable;

                for (int n : Level.NEIGHBOURS4) {
                    int c = Dungeon.hero.hiredMerc.pos + n;
                    if (passable[c] && Actor.findChar(c) == null) {
                        candidates.add(c);
                    }
                }
                newPos = candidates.size() > 0 ? Random.element(candidates) : -1;
                if (newPos != -1) {
                    spawned = true;
                    SummonedPet rat = new SummonedPet(SummonedPet.PET_TYPES.RAT);
                    rat.spawn(level);
                    rat.pos = newPos;
                    GameScene.add(rat);
                    Actor.addDelayed(new Pushing(rat, Dungeon.hero.hiredMerc.pos, newPos), -1);
                    rat.sprite.alpha(0);
                    rat.sprite.parent.add(new AlphaTweener(rat.sprite, 1, 0.15f));
                }
            }
        }

        if (spawned == true) {
            castTextYell();
        }
    }

    @Override
    public void castTextYell() {
        if (castText != "")
            Dungeon.hero.hiredMerc.sprite.showStatus(CharSprite.NEUTRAL, castText);
    }


    @Override
    public String info() {
        return "Summons rats for your service.\n";
    }
}
