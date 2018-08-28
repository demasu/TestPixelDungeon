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

package com.watabou.gltextures;

import java.util.HashMap;

import android.graphics.RectF;

public class Atlas {

    private final SmartTexture tx;

    private final HashMap<Object, RectF> namedFrames;

    private float uvLeft;
    private float uvTop;
    private float uvWidth;
    private float uvHeight;
    private int cols;

    public Atlas(SmartTexture tx) {

        this.tx = tx;
        tx.atlas = this;

        namedFrames = new HashMap<>();
    }

// --Commented out by Inspection START (8/28/18, 6:38 PM):
//    public void add(Object key, int left, int top, int right, int bottom) {
//        add(key, uvRect(tx, left, top, right, bottom));
//    }
// --Commented out by Inspection STOP (8/28/18, 6:38 PM)

    private void add(Object key, RectF rect) {
        namedFrames.put(key, rect);
    }

// --Commented out by Inspection START (8/28/18, 6:38 PM):
//    public void grid(int width) {
//        grid(width, tx.height);
//    }
// --Commented out by Inspection STOP (8/28/18, 6:38 PM)

    private void grid(int width, int height) {
        grid(width, height, tx.width / width);
    }

    //This had two extra variables in commit 6832ea116027 and earlier
    private void grid(int width, int height, int cols) {
        uvLeft = (float) 0 / tx.width;
        uvTop = (float) 0 / tx.height;
        uvWidth = (float) width / tx.width;
        uvHeight = (float) height / tx.height;
        this.cols = cols;
    }

// --Commented out by Inspection START (8/28/18, 6:38 PM):
//    public RectF get(int index) {
//        float x = index % cols;
//        float y = index / cols;
//        float l = uvLeft + x * uvWidth;
//        float t = uvTop + y * uvHeight;
//        return new RectF(l, t, l + uvWidth, t + uvHeight);
//    }
// --Commented out by Inspection STOP (8/28/18, 6:38 PM)

// --Commented out by Inspection START (8/28/18, 6:38 PM):
//    public RectF get(Object key) {
//        return namedFrames.get(key);
//    }
// --Commented out by Inspection STOP (8/28/18, 6:38 PM)

// --Commented out by Inspection START (8/28/18, 6:38 PM):
//    public float width(RectF rect) {
//        return rect.width() * tx.width;
//    }
// --Commented out by Inspection STOP (8/28/18, 6:38 PM)

// --Commented out by Inspection START (8/28/18, 6:39 PM):
//    public float height(RectF rect) {
//        return rect.height() * tx.height;
//    }
// --Commented out by Inspection STOP (8/28/18, 6:39 PM)

    private static RectF uvRect(SmartTexture tx, int left, int top, int right, int bottom) {
        return new RectF(
                (float) left / tx.width,
                (float) top / tx.height,
                (float) right / tx.width,
                (float) bottom / tx.height);
    }
}
