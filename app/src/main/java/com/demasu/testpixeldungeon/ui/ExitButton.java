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

import com.demasu.testpixeldungeon.Assets;
import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.scenes.TitleScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class ExitButton extends Button {

    private Image image;

    public ExitButton () {
        super();

        setWidth( image.getWidth() );
        setHeight( image.getHeight() );
    }

    @Override
    protected void createChildren () {
        super.createChildren();

        image = Icons.EXIT.get();
        add( image );
    }

    @Override
    protected void layout () {
        super.layout();

        image.setX( getX() );
        image.setY( getY() );
    }

    @Override
    protected void onTouchDown () {
        image.brightness( 1.5f );
        Sample.INSTANCE.play( Assets.SND_CLICK );
    }

    @Override
    protected void onTouchUp () {
        image.resetColor();
    }

    @Override
    protected void onClick () {
        if ( Game.scene() instanceof TitleScene ) {
            Game.getInstance().finish();
        } else {
            PixelDungeon.switchNoFade( TitleScene.class );
        }
    }
}
