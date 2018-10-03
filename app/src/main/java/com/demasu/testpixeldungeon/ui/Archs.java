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
import com.watabou.noosa.Game;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.ui.Component;

public class Archs extends Component {

    private static final float SCROLL_SPEED = 20f;
    private static float offsB = 0;
    private static float offsF = 0;
    public boolean reversed = false;
    private SkinnedBlock arcsBg;
    private SkinnedBlock arcsFg;

    @Override
    protected void createChildren () {
        arcsBg = new SkinnedBlock( 1, 1, Assets.ARCS_BG );
        arcsBg.autoAdjust = true;
        arcsBg.offsetTo( 0, offsB );
        add( arcsBg );

        arcsFg = new SkinnedBlock( 1, 1, Assets.ARCS_FG );
        arcsFg.autoAdjust = true;
        arcsFg.offsetTo( 0, offsF );
        add( arcsFg );
    }

    @Override
    protected void layout () {
        arcsBg.size( getWidth(), getHeight() );
        arcsBg.offset( arcsBg.texture.getWidth() / 4 - ( getWidth() % arcsBg.texture.getWidth() ) / 2, 0 );

        arcsFg.size( getWidth(), getHeight() );
        arcsFg.offset( arcsFg.texture.getWidth() / 4 - ( getWidth() % arcsFg.texture.getWidth() ) / 2, 0 );
    }

    @Override
    public void update () {

        super.update();

        float shift = Game.getElapsed() * SCROLL_SPEED;
        if ( reversed ) {
            shift = -shift;
        }

        arcsBg.offset( 0, shift );
        arcsFg.offset( 0, shift * 2 );

        offsB = arcsBg.offsetY();
        offsF = arcsFg.offsetY();
    }
}
