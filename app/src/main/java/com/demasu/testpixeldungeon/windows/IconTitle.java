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

import com.demasu.testpixeldungeon.items.Item;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.sprites.ItemSprite;
import com.demasu.testpixeldungeon.ui.HealthBar;
import com.demasu.testpixeldungeon.ui.Window;
import com.demasu.testpixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class IconTitle extends Component {

    private static final float FONT_SIZE = 9;

    private static final float GAP = 2;

    protected Image imIcon;
    protected BitmapTextMultiline tfLabel;
    protected HealthBar health;

    private float healthLvl = Float.NaN;

    public IconTitle () {
        super();
    }

    public IconTitle ( Item item ) {
        this(
                new ItemSprite( item.image(), item.glowing() ),
                Utils.capitalize( item.toString() ) );
    }

    public IconTitle ( Image icon, String label ) {
        super();

        icon( icon );
        label( label );
    }

    @Override
    protected void createChildren () {
        imIcon = new Image();
        add( imIcon );

        tfLabel = PixelScene.createMultiline( FONT_SIZE );
        tfLabel.hardlight( Window.TITLE_COLOR );
        add( tfLabel );

        health = new HealthBar();
        add( health );
    }

    @Override
    protected void layout () {

        health.setVisible( !Float.isNaN( healthLvl ) );

        imIcon.setX( getX() );
        imIcon.setY( getY() );

        tfLabel.setX( PixelScene.align( PixelScene.uiCamera, imIcon.getX() + imIcon.width() + GAP ) );
        tfLabel.setMaxWidth( (int) ( getWidth() - tfLabel.getX() ) );
        tfLabel.measure();
        tfLabel.setY( PixelScene.align( PixelScene.uiCamera,
                imIcon.getHeight() > tfLabel.height() ?
                        imIcon.getY() + ( imIcon.height() - tfLabel.baseLine() ) / 2 :
                        imIcon.getY() ) );

        if ( health.getVisible() ) {
            health.setRect( tfLabel.getX(), Math.max( tfLabel.getY() + tfLabel.height(), imIcon.getY() + imIcon.height() - health.height() ), tfLabel.getMaxWidth(), 0 );
            setHeight( health.bottom() );
        } else {
            setHeight( Math.max( imIcon.getY() + imIcon.height(), tfLabel.getY() + tfLabel.height() ) );
        }
    }

    public void icon ( Image icon ) {
        remove( imIcon );
        add( imIcon = icon );
    }

    public void label ( String label ) {
        tfLabel.text( label );
    }

    public void label ( String label, int color ) {
        tfLabel.text( label );
        tfLabel.hardlight( color );
    }

    public void color ( int color ) {
        tfLabel.hardlight( color );
    }

    public void health ( float value ) {
        health.level( healthLvl = value );
        layout();
    }
}
