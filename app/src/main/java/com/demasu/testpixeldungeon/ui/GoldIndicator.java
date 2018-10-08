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

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

public class GoldIndicator extends Component {

    private static final float TIME = 2f;

    private int lastValue = 0;

    private BitmapText tf;

    private float time;

    @Override
    protected void createChildren () {
        tf = new BitmapText( PixelScene.font1x );
        tf.hardlight( 0xFFFF00 );
        add( tf );

        setVisible( false );
    }

    @Override
    protected void layout () {
        tf.setX( getX() + ( getWidth() - tf.width() ) / 2 );
        tf.setY( bottom() - tf.height() );
    }

    @Override
    public void update () {
        super.update();

        if ( getVisible() ) {

            time -= Game.getElapsed();
            if ( time > 0 ) {
                tf.alpha( time > TIME / 2 ? 1f : time * 2 / TIME );
            } else {
                setVisible( false );
            }

        }

        if ( Dungeon.getGold() != lastValue ) {

            lastValue = Dungeon.getGold();

            tf.text( Integer.toString( lastValue ) );
            tf.measure();

            setVisible( true );
            time = TIME;

            layout();
        }
    }
}
