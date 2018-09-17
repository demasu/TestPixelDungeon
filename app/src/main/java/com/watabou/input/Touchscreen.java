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

package com.watabou.input;

import android.view.MotionEvent;

import com.watabou.utils.PointF;
import com.watabou.utils.Signal;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

public class Touchscreen {

    private static final Signal<Touch> event = new Signal<>( true );

    private static final SparseArray<Touch> pointers = new SparseArray<>();

    @SuppressWarnings ( "FeatureEnvy" )
    public static void processTouchEvents ( ArrayList<MotionEvent> events ) {

        int size = events.size();
        for ( int i = 0; i < size; i++ ) {

            MotionEvent e = events.get( i );
            Touch touch;

            switch ( e.getAction() & MotionEvent.ACTION_MASK ) {

                case MotionEvent.ACTION_DOWN:
                    touch = new Touch( e, 0 );
                    pointers.put( e.getPointerId( 0 ), touch );
                    getEvent().dispatch( touch );
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    int index = e.getActionIndex();
                    touch = new Touch( e, index );
                    pointers.put( e.getPointerId( index ), touch );
                    getEvent().dispatch( touch );
                    break;

                case MotionEvent.ACTION_MOVE:
                    int count = e.getPointerCount();
                    for ( int j = 0; j < count; j++ ) {
                        pointers.get( e.getPointerId( j ) ).update( e, j );
                    }
                    getEvent().dispatch( null );
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    getEvent().dispatch( pointers.get( e.getPointerId( e.getActionIndex() ) ).up() );
                    pointers.delete( e.getPointerId( e.getActionIndex() ) );
                    break;

                case MotionEvent.ACTION_UP:
                    getEvent().dispatch( pointers.get( e.getPointerId( 0 ) ).up() );
                    pointers.delete( e.getPointerId( 0 ) );
                    break;

            }

            e.recycle();
        }
    }

    public static Signal<Touch> getEvent () {
        return event;
    }

    // TODO: Look in to breaking this class out
    @SuppressWarnings ( "PublicInnerClass" )
    public static class Touch {

        private PointF start;
        private PointF current;
        private boolean down;

        Touch ( MotionEvent e, int index ) {

            float x = e.getX( index );
            float y = e.getY( index );

            setStart( new PointF( x, y ) );
            setCurrent( new PointF( x, y ) );

            setDown( true );
        }

        void update ( MotionEvent e, int index ) {
            getCurrent().set( e.getX( index ), e.getY( index ) );
        }

        Touch up () {
            setDown( false );
            return this;
        }

        public PointF getStart () {
            return start;
        }

        void setStart ( PointF start ) {
            this.start = start;
        }

        public PointF getCurrent () {
            return current;
        }

        void setCurrent ( PointF current ) {
            this.current = current;
        }

        public boolean isDown () {
            return down;
        }

        void setDown ( boolean down ) {
            this.down = down;
        }
    }

}
