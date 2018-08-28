package com.demasu.testpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.demasu.testpixeldungeon.actors.hero.Hero;
import com.demasu.testpixeldungeon.effects.ArcherMaidenHalo;
import com.demasu.testpixeldungeon.scenes.GameScene;

/**
 * Created by Moussa on 04-Feb-17.
 */
public class LegendSprite extends HeroSprite {


    public ArcherMaidenHalo halo = null;
    public boolean hasHalo = false;

    public void haloUp() {
        if (hasHalo)
            return;

        hasHalo = true;
        add(State.ARCHERMAIDEN);
        GameScene.effect(halo = new ArcherMaidenHalo(this));
    }


    @Override
    public void updateArmor() {

        TextureFilm film = new TextureFilm(tiers(), 0, FRAME_WIDTH, FRAME_HEIGHT);

        idle = new Animation(1, true);
        idle.frames(film, 0, 0, 0, 1, 0, 0, 1, 1);

        run = new Animation(RUN_FRAMERATE, true);
        run.frames(film, 2, 3, 4, 5, 6, 7);

        die = new Animation(20, false);
        die.frames(film, 8, 9, 10, 11, 12, 11);

        attack = new Animation(15, false);
        attack.frames(film, 13, 14, 15, 0);

        zap = attack.clone();
        operate = attack.clone();


        fly = new Animation(1, true);
        fly.frames(film, 18);

        read = new Animation(20, false);
        read.frames(film, 19, 20, 20, 20, 20, 20, 20, 20, 20, 19);
    }

}
