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

package com.watabou.glwrap;

import android.opengl.GLES20;

@SuppressWarnings("SpellCheckingInspection")
public class Framebuffer {

    // --Commented out by Inspection (8/28/18, 6:56 PM):public static final int COLOR = GLES20.GL_COLOR_ATTACHMENT0;
    // --Commented out by Inspection (8/28/18, 6:56 PM):public static final int DEPTH = GLES20.GL_DEPTH_ATTACHMENT;
    // --Commented out by Inspection (8/28/18, 6:56 PM):public static final int STENCIL = GLES20.GL_STENCIL_ATTACHMENT;

    // --Commented out by Inspection (8/28/18, 6:56 PM):public static final Framebuffer system = new Framebuffer();

    // --Commented out by Inspection (8/29/18, 3:44 PM):private final int id;

    private Framebuffer() {
        int[] buffers = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);
        //id = buffers[0];
    }

// --Commented out by Inspection START (8/29/18, 12:24 PM):
//    private void bind() {
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, id);
//    }
// --Commented out by Inspection STOP (8/29/18, 12:24 PM)

// --Commented out by Inspection START (8/28/18, 6:56 PM):
//    public void delete() {
//        int[] buffers = {id};
//        GLES20.glDeleteFramebuffers(1, buffers, 0);
//    }
// --Commented out by Inspection STOP (8/28/18, 6:56 PM)

// --Commented out by Inspection START (8/28/18, 6:56 PM):
//    public void attach(int point, Texture tex) {
//        bind();
//        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, point, GLES20.GL_TEXTURE_2D, tex.id, 0);
//    }
// --Commented out by Inspection STOP (8/28/18, 6:56 PM)

// --Commented out by Inspection START (8/28/18, 6:56 PM):
//    public void attach(int point, Renderbuffer buffer) {
//        bind();
//        GLES20.glFramebufferRenderbuffer(GLES20.GL_RENDERBUFFER, point, GLES20.GL_TEXTURE_2D, buffer.id());
//    }
// --Commented out by Inspection STOP (8/28/18, 6:56 PM)

// --Commented out by Inspection START (8/28/18, 6:56 PM):
//    public boolean status() {
//        bind();
//        return GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) == GLES20.GL_FRAMEBUFFER_COMPLETE;
//    }
// --Commented out by Inspection STOP (8/28/18, 6:56 PM)
}
