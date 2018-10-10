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

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Matrix;
import com.watabou.glwrap.Quad;

import java.nio.FloatBuffer;

public class BitmapText extends Visual {

    private final int VERTICIES_SIZE = 16;

    private int realLength;
    private String text;
    private Font font;
    private float[] vertices = new float[VERTICIES_SIZE];
    private FloatBuffer quads;
    private boolean dirty = true;

    public BitmapText () {
        this( "", null );
    }

    public BitmapText ( Font font ) {
        this( "", font );
    }

    public BitmapText ( String text, Font font ) {
        super( 0, 0, 0, 0 );

        this.setText( text );
        this.setFont( font );
    }

    @Override
    public void destroy () {
        setText( null );
        setFont( null );
        setVertices();
        setQuads( null );
        super.destroy();
    }

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    protected void updateMatrix () {
        // "origin" field is ignored
        Matrix.setIdentity( getMatrix() );
        Matrix.translate( getMatrix(), getX(), getY() );
        Matrix.scale( getMatrix(), getScale().getX(), getScale().getY() );
        Matrix.rotate( getMatrix(), getAngle() );
    }

    @SuppressWarnings ( "FeatureEnvy" )
    @Override
    public void draw () {

        super.draw();

        NoosaScript script = NoosaScript.get();

        getFont().getTexture().bind();

        if ( isDirty() ) {
            updateVertices();
        }

        script.camera( camera() );

        script.getuModel().valueM4( getMatrix() );
        script.lighting(
                getRm(), getGm(), getBm(), getAm(),
                getRa(), getGa(), getBa(), getAa() );
        script.drawQuadSet( getQuads(), getRealLength() );

    }

    @SuppressWarnings ( "MagicNumber" )
    void updateVertices () {

        setWidth( 0 );
        setHeight( 0 );

        if ( getText() == null ) {
            setText( "" );
        }

        setQuads( Quad.createSet( getText().length() ) );
        setRealLength( 0 );

        int length = getText().length();
        for ( int i = 0; i < length; i++ ) {
            RectF rect = getFont().get( getText().charAt( i ) );

            float w = getFont().width( rect );
            float h = getFont().height( rect );

            getVertices()[0] = getWidth();
            getVertices()[1] = 0;

            getVertices()[2] = rect.left;
            getVertices()[3] = rect.top;

            getVertices()[4] = getWidth() + w;
            getVertices()[5] = 0;

            getVertices()[6] = rect.right;
            getVertices()[7] = rect.top;

            getVertices()[8] = getWidth() + w;
            getVertices()[9] = h;

            getVertices()[10] = rect.right;
            getVertices()[11] = rect.bottom;

            getVertices()[12] = getWidth();
            getVertices()[13] = h;

            getVertices()[14] = rect.left;
            getVertices()[15] = rect.bottom;

            getQuads().put( getVertices() );
            setRealLength( getRealLength() + 1 );

            setWidth( getWidth() + w + getFont().getTracking() );
            if ( h > getHeight() ) {
                setHeight( h );
            }
        }

        if ( length > 0 ) {
            setWidth( getWidth() - getFont().getTracking() );
        }

        setDirty( false );

    }

    public void measure () {

        setWidth( 0 );
        setHeight( 0 );

        if ( getText() == null ) {
            setText( "" );
        }

        int length = getText().length();
        for ( int i = 0; i < length; i++ ) {
            RectF rect = getFont().get( getText().charAt( i ) );

            float w = getFont().width( rect );
            float h = getFont().height( rect );

            setWidth( getWidth() + w + getFont().getTracking() );
            if ( h > getHeight() ) {
                setHeight( h );
            }
        }

        if ( length > 0 ) {
            setWidth( getWidth() - getFont().getTracking() );
        }
    }

    public float baseLine () {
        return getFont().getBaseLine() * getScale().getY();
    }

    public String text () {
        return getText();
    }

    public void text ( String str ) {
        setText( str );
        setDirty( true );
    }

    int getRealLength () {
        return realLength;
    }

    void setRealLength ( int realLength ) {
        this.realLength = realLength;
    }

    String getText () {
        return text;
    }

    void setText ( String text ) {
        this.text = text;
    }

    Font getFont () {
        return font;
    }

    public void setFont ( Font font ) {
        this.font = font;
    }

    @SuppressWarnings ( "AssignmentOrReturnOfFieldWithMutableType" )
    float[] getVertices () {
        return vertices;
    }

    @SuppressWarnings ( "AssignmentToNull" )
    private void setVertices () {
        this.vertices = null;
    }

    FloatBuffer getQuads () {
        return quads;
    }

    void setQuads ( FloatBuffer quads ) {
        this.quads = quads;
    }

    private boolean isDirty () {
        return dirty;
    }

    void setDirty ( boolean dirty ) {
        this.dirty = dirty;
    }

    // TODO: Move in to own file
    @SuppressWarnings ( "PublicInnerClass" )
    public static class Font extends TextureFilm {

        static final String LATIN_UPPER =
                " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static final String LATIN_FULL =
                " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007F";

        private final SmartTexture texture;

        private float tracking = 0;
        private float baseLine;

        private boolean autoUppercase = false;

        private float lineHeight;

        Font ( SmartTexture tx ) {
            super( tx );

            texture = tx;
        }

        public static Font colorMarked ( Bitmap bmp, int color, String chars ) {
            Font font = new Font( TextureCache.get( bmp ) );
            font.splitBy( bmp, bmp.getHeight(), color, chars );
            return font;
        }

        public static Font colorMarked ( Bitmap bmp, int height, int color, String chars ) {
            Font font = new Font( TextureCache.get( bmp ) );
            font.splitBy( bmp, height, color, chars );
            return font;
        }

        void splitBy ( Bitmap bitmap, int height, int color, String chars ) {

            setAutoUppercase( chars.equals( LATIN_UPPER ) );

            int width = bitmap.getWidth();
            float vHeight = (float) height / bitmap.getHeight();

            int pos;

            spaceMeasuring:
            for ( pos = 0; pos < width; pos++ ) {
                for ( int j = 0; j < height; j++ ) {
                    if ( bitmap.getPixel( pos, j ) != color ) {
                        break spaceMeasuring;
                    }
                }
            }
            add( ' ', new RectF( 0, 0, (float) pos / width, vHeight ) );

            int length = chars.length();
            for ( int i = 0; i < length; i++ ) {

                char ch = chars.charAt( i );
                if ( ch != ' ' ) {
                    boolean found;
                    int separator = pos;

                    do {
                        ++separator;
                        if ( separator >= width ) {
                            break;
                        }
                        found = true;
                        for ( int j = 0; j < height; j++ ) {
                            if ( bitmap.getPixel( separator, j ) != color ) {
                                found = false;
                                break;
                            }
                        }
                    } while ( !found );

                    add( ch, new RectF( (float) pos / width, 0, (float) separator / width, vHeight ) );
                    pos = separator + 1;
                }
            }

            setLineHeight( height( getFrames().get( chars.charAt( 0 ) ) ) );
            setBaseLine( height( getFrames().get( chars.charAt( 0 ) ) ) );
        }

        RectF get ( char ch ) {
            return super.get( isAutoUppercase() ? Character.toUpperCase( ch ) : ch );
        }

        SmartTexture getTexture () {
            return texture;
        }

        public float getTracking () {
            return tracking;
        }

        public void setTracking ( float tracking ) {
            this.tracking = tracking;
        }

        float getBaseLine () {
            return baseLine;
        }

        public void setBaseLine ( float baseLine ) {
            this.baseLine = baseLine;
        }

        boolean isAutoUppercase () {
            return autoUppercase;
        }

        void setAutoUppercase ( boolean autoUppercase ) {
            this.autoUppercase = autoUppercase;
        }

        float getLineHeight () {
            return lineHeight;
        }

        void setLineHeight ( float lineHeight ) {
            this.lineHeight = lineHeight;
        }
    }
}
