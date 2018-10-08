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

import com.demasu.testpixeldungeon.Dungeon;
import com.demasu.testpixeldungeon.Journal;
import com.demasu.testpixeldungeon.PixelDungeon;
import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.demasu.testpixeldungeon.ui.Icons;
import com.demasu.testpixeldungeon.ui.ScrollPane;
import com.demasu.testpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.Collections;

public class WndJournal extends Window {

    private static final int WIDTH = 112;
    private static final int HEIGHT_P = 160;
    private static final int HEIGHT_L = 144;

    private static final int ITEM_HEIGHT = 18;

    private static final String TXT_TITLE = "Journal";

    private BitmapText txtTitle;
    private ScrollPane list;

    public WndJournal () {

        super();
        resize( WIDTH, PixelDungeon.landscape() ? HEIGHT_L : HEIGHT_P );

        txtTitle = PixelScene.createText( TXT_TITLE, 9 );
        txtTitle.hardlight( Window.TITLE_COLOR );
        txtTitle.measure();
        txtTitle.setX( PixelScene.align( PixelScene.uiCamera, ( WIDTH - txtTitle.width() ) / 2 ) );
        add( txtTitle );

        Component content = new Component();

        Collections.sort( Journal.records );

        float pos = 0;
        for ( Journal.Record rec : Journal.records ) {
            ListItem item = new ListItem( rec.feature, rec.depth );
            item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
            content.add( item );

            pos += item.height();
        }

        content.setSize( WIDTH, pos );

        list = new ScrollPane( content );
        add( list );

        list.setRect( 0, txtTitle.height(), WIDTH, height - txtTitle.height() );
    }

    private static class ListItem extends Component {

        private BitmapText feature;
        private BitmapText depth;

        private Image icon;

        public ListItem ( Journal.Feature f, int d ) {
            super();

            feature.text( f.desc );
            feature.measure();

            depth.text( Integer.toString( d ) );
            depth.measure();

            if ( d == Dungeon.getDepth() ) {
                feature.hardlight( TITLE_COLOR );
                depth.hardlight( TITLE_COLOR );
            }
        }

        @Override
        protected void createChildren () {
            feature = PixelScene.createText( 9 );
            add( feature );

            depth = new BitmapText( PixelScene.font1x );
            add( depth );

            icon = Icons.get( Icons.DEPTH );
            add( icon );
        }

        @Override
        protected void layout () {

            icon.setX( getWidth() - icon.getWidth() );

            depth.setX( icon.getX() - 1 - depth.width() );
            depth.setY( PixelScene.align( getY() + ( getHeight() - depth.height() ) / 2 ) );

            icon.setY( depth.getY() - 1 );

            feature.setY( PixelScene.align( depth.getY() + depth.baseLine() - feature.baseLine() ) );
        }
    }
}
