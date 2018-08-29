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

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Button;
import com.demasu.testpixeldungeon.actors.skills.Skill;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.SkillSprite;

import java.util.Objects;

public class SkillSlot extends Button {

    // --Commented out by Inspection (8/28/18, 6:47 PM):public static final int DEGRADED = 0xFF4444;
    // --Commented out by Inspection (8/28/18, 6:53 PM):public static final int UPGRADED = 0x44FF44;
    // --Commented out by Inspection (8/28/18, 6:53 PM):public static final int WARNING = 0xFF8800;

    private static final float ENABLED = 1.0f;
    private static final float DISABLED = 0.3f;

    private SkillSprite icon;
    private BitmapText activeText;


    protected SkillSlot() {
        super();
    }

    protected SkillSlot(Skill skill) {
        this();
        if (skill == null) {

            active = false;
            icon.visible = false;

        } else {
            active = true;
            icon.visible = true;

            icon.view(skill.image());

            float alpha = skill.getAlpha();
            icon.alpha(alpha);
        }

        if (Objects.requireNonNull(skill).active) {
            activeText = new BitmapText(PixelScene.font1x);
            activeText.text("Active");
            activeText.hardlight(Window.TITLE_COLOR);
            add(activeText);
        }

        layout();
    }

    @Override
    protected void createChildren() {

        super.createChildren();

        icon = new SkillSprite();
        add(icon);


    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = x + (width - icon.width) / 2;
        icon.y = y + (height - icon.height) / 2;

        if (activeText != null) {
            activeText.x = x + 3;
            activeText.y = y + 11;
        }
    }


// --Commented out by Inspection START (8/28/18, 6:53 PM):
//    public void enable(boolean value) {
//
//        active = value;
//
//        float alpha = value ? ENABLED : DISABLED;
//        icon.alpha(alpha);
//    }
// --Commented out by Inspection STOP (8/28/18, 6:53 PM)
}
