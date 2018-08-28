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
package com.demasu.testpixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.actors.skills.BranchSkill;
import com.demasu.testpixeldungeon.actors.skills.Negotiations;
import com.demasu.testpixeldungeon.actors.skills.Skill;
import com.demasu.testpixeldungeon.scenes.GameScene;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.SkillSprite;
import com.demasu.testpixeldungeon.ui.RedButton;
import com.demasu.testpixeldungeon.ui.Window;
import com.demasu.testpixeldungeon.utils.Utils;

public class WndSkill extends Window {

    private static final float BUTTON_WIDTH = 36;
    private static final float BUTTON_HEIGHT = 16;

    private static final float GAP = 2;

    private static final int WIDTH = 120;

    public WndSkill(final WndSkills owner, final Skill skill) {

        super();

        IconTitle titlebar = new IconTitle();
        titlebar.icon(new SkillSprite(skill.image()));
        titlebar.label(Utils.capitalize(skill.name));
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);


        BitmapTextMultiline info = PixelScene.createMultiline(skill.info(), 6);
        info.maxWidth = WIDTH;
        info.measure();
        info.x = titlebar.left();
        info.y = titlebar.bottom() + GAP;
        add(info);

        float y = info.y + info.height() + GAP;
        float x = 0;

        if (Dungeon.hero.isAlive()) {
            for (final String action : skill.actions(Dungeon.hero)) {

                RedButton btn = new RedButton(action) {
                    @Override
                    protected void onClick() {
                        skill.execute(Dungeon.hero, action);
                        hide();
                        if (owner != null) {
                            owner.hide();
                            if (skill instanceof BranchSkill)
                                GameScene.show(new WndSkills(null, null));
                        }
                    }

                    ;
                };
                btn.setSize(Math.max(BUTTON_WIDTH, btn.reqWidth()), BUTTON_HEIGHT);
                if (x + btn.width() > WIDTH) {
                    x = 0;
                    y += BUTTON_HEIGHT + GAP;
                }
                btn.setPos(x, y);


                if (action == Negotiations.TXT_HIRE_ARCHER_MAIDEN) {
                    btn.textColor(TITLE_COLOR);
                }


                add(btn);

                x += btn.width() + GAP;
            }
        }

        resize(WIDTH, (int) (y + (x > 0 ? BUTTON_HEIGHT : 0)));
    }
}
