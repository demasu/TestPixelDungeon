/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.demasu.testpixeldungeon.ui;

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.BitmaskEmitter;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.mobs.ColdGirl;
import com.demasu.testpixeldungeon.effects.Speck;
import com.demasu.testpixeldungeon.effects.particles.BloodParticle;
import com.demasu.testpixeldungeon.items.keys.IronKey;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.MissionScene;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.HeroSprite;
import com.demasu.testpixeldungeon.windows.WndGame;
import com.demasu.testpixeldungeon.windows.WndHero;

public class MissionStatusPane extends Component {

    private NinePatch shield;
    private Image avatar;
    private Emitter blood;

    private int lastTier = 0;

    private Image hp;
    private Image hp_dropping;
    private Image mp_dropping;
    private Image mp;
    private Image exp;

    public static float takingDamage = 0;
    public static float manaDropping = 0;

    private int takingDamageCooldownCounter = 0;
    private int manaDroppingCooldownCounter = 0;
    private static final int TAKING_DAMAGE_COOLDOWN_INTERVAL = 3;

    private int lastLvl = -1;


    private DangerIndicator danger;
    private LootIndicator loot;
    private ResumeButton resume;
    private BuffIndicator buffs;


    private MenuButton btnMenu;

    @Override
    protected void createChildren() {

        shield = new NinePatch(Assets.STATUS_MISSION, 80, 0, 30 + 18, 0);
        add(shield);

        add(new TouchArea(0, 1, 30, 30) {
            @Override
            protected void onClick(Touch touch) {
                Image sprite = Dungeon.hero.sprite;
                if (!sprite.isVisible()) {
                    Camera.main.focusOn(sprite);
                }
                GameScene.show(new WndHero());
            }

        });

        btnMenu = new MenuButton();
        add(btnMenu);

        avatar = HeroSprite.avatar(Dungeon.hero.heroClass, 0);
        add(avatar);

        blood = new BitmaskEmitter(avatar);
        blood.pour(BloodParticle.FACTORY, 0.3f);
        blood.autoKill = false;
        blood.on = false;
        add(blood);


        hp = new Image(Assets.HP_BAR);
        add(hp);

        hp_dropping = new Image(Assets.HP_BAR_DROPPING);
        add(hp_dropping);

        mp = new Image(Assets.MP_BAR);
        add(mp);

        mp_dropping = new Image(Assets.MP_BAR_DROPPING);
        add(mp_dropping);

        exp = new Image(Assets.XP_BAR);
        add(exp);


        danger = new DangerIndicator();
        add(danger);

        loot = new LootIndicator();
        add(loot);

        resume = new ResumeButton();
        add(resume);

        buffs = new BuffIndicator(Dungeon.hero);
        add(buffs);

        takingDamage = 0;
        manaDropping = 0;
    }

    @Override
    protected void layout() {

        height = 32;

        shield.size(width, shield.height);

        avatar.x = PixelScene.align(camera(), shield.x + 15 - avatar.width / 2);
        avatar.y = PixelScene.align(camera(), shield.y + 16 - avatar.height / 2);


        hp.x = 30;
        hp.y = 3;
        hp_dropping.x = 30;
        hp_dropping.y = 3;
        mp.x = 30;
        mp.y = 8;
        mp_dropping.x = 30;
        mp_dropping.y = 8;


        layoutTags();

        buffs.setPos(36, 16);

        btnMenu.setPos(width - btnMenu.width(), 1);
    }

    private void layoutTags() {

        float pos = 18;

        if (tagDanger) {
            danger.setPos(width - danger.width(), pos);
            pos = danger.bottom() + 1;
        }

        if (tagLoot) {
            loot.setPos(width - loot.width(), pos);
            pos = loot.bottom() + 1;
        }

        if (tagResume) {
            resume.setPos(width - resume.width(), pos);
        }
    }

    private boolean tagDanger = false;
    private boolean tagLoot = false;
    private boolean tagResume = false;

    @Override
    public void update() {
        super.update();
        visible = !MissionScene.scenePause;

        if (tagDanger != danger.visible || tagLoot != loot.visible || tagResume != resume.visible) {

            tagDanger = danger.visible;
            tagLoot = loot.visible;
            tagResume = resume.visible;

            layoutTags();
        }

        float health = (float) (Dungeon.hero.HP) / Dungeon.hero.HT;

        if (Dungeon.hero.HP == 0)
            takingDamage = 0;

        float health_drop = takingDamage / Dungeon.hero.HT;

        if (takingDamage > 0) {
            takingDamageCooldownCounter++;
            if (takingDamageCooldownCounter % TAKING_DAMAGE_COOLDOWN_INTERVAL == 0)
                takingDamage -= 0.1f;
        }

        if (health == 0) {
            avatar.tint(0x000000, 0.6f);
            blood.on = false;
        } else if (health < 0.25f) {
            avatar.tint(0xcc0000, 0.4f);
            blood.on = true;
        } else {
            avatar.resetColor();
            blood.on = false;
        }

        hp.scale.x = health;
        hp_dropping.x = hp.x + hp.width() - 20 * health;
        hp_dropping.scale.x = health_drop;

        exp.scale.x = (width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();

        float mana = (float) Dungeon.hero.MP / Dungeon.hero.MMP;

        float mana_drop = manaDropping / Dungeon.hero.MMP;

        mp.scale.x = mana;
        mp_dropping.x = mp.x + mp.width() - 20 * mana;
        mp_dropping.scale.x = mana_drop;

        if (Dungeon.hero.MMP == 0)
            manaDropping = 0;

        if (manaDropping > 0) {
            manaDroppingCooldownCounter++;
            if (manaDroppingCooldownCounter % TAKING_DAMAGE_COOLDOWN_INTERVAL == 0)
                manaDropping -= 0.1f;
        }

        if (Dungeon.hero.lvl != lastLvl) {

            if (lastLvl != -1) {
                Emitter emitter = (Emitter) recycle(Emitter.class);
                emitter.revive();
                emitter.pos(27, 27);
                emitter.burst(Speck.factory(Speck.STAR), 12);
            }

            lastLvl = Dungeon.hero.lvl;

        }


    }

    private static class MenuButton extends Button {

        private Image image;

        public MenuButton() {
            super();

            width = image.width + 4;
            height = image.height + 4;
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            image = new Image(Assets.STATUS_MISSION, 114, 3, 12, 11);
            add(image);
        }

        @Override
        protected void layout() {
            super.layout();

            image.x = x + 2;
            image.y = y + 2;
        }

        @Override
        protected void onTouchDown() {
            image.brightness(1.5f);
            Sample.INSTANCE.play(Assets.SND_CLICK);
        }

        @Override
        protected void onTouchUp() {
            image.resetColor();
        }

        @Override
        protected void onClick() {
            GameScene.show(new WndGame());
        }
    }
}
