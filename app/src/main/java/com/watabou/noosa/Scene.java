/*
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

package com.watabou.noosa;

import com.watabou.input.Keys;
import com.watabou.utils.Signal;

public class Scene extends Group {

    private Signal.Listener<Keys.Key> keyListener;

    public void create () {
        keyListener = new Signal.Listener<Keys.Key>() {
            @Override
            public void onSignal ( Keys.Key key ) {
                if ( Game.getInstance() != null && key.pressed ) {
                    switch ( key.code ) {
                        case Keys.BACK:
                            onBackPressed();
                            break;
                        case Keys.MENU:
                            onMenuPressed();
                            break;
                    }
                }
            }
        };
        Keys.event.add( keyListener );
    }

    @Override
    public void destroy () {
        Keys.event.remove( keyListener );
        super.destroy();
    }

    public void pause () {

    }

    public void resume () {

    }

    @Override
    public void update () {
        super.update();
    }

    @Override
    public Camera camera () {
        return Camera.getMain();
    }

    protected void onBackPressed () {
        Game.getInstance().finish();
    }

    protected void onMenuPressed () {

    }

}
