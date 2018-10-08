package com.demasu.testpixeldungeon.ui;

import com.demasu.testpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Highlighter;

public class HighlightedText extends Component {

    protected BitmapTextMultiline normal;
    protected BitmapTextMultiline highlighted;

    protected int nColor = 0xFFFFFF;
    protected int hColor = 0xFFFF44;

    public HighlightedText ( float size ) {
        normal = PixelScene.createMultiline( size );
        add( normal );

        highlighted = PixelScene.createMultiline( size );
        add( highlighted );

        setColor( 0xFFFFFF, 0xFFFF44 );
    }

    @Override
    protected void layout () {
        float x = getX();
        float y = getY();
        normal.setX( x );
        highlighted.setX( x );
        normal.setY( y );
        highlighted.setY( y );
    }

    public void text ( String value, int maxWidth ) {
        Highlighter hl = new Highlighter( value );

        normal.text( hl.text );
        normal.setMaxWidth( maxWidth );
        normal.measure();

        if ( hl.isHighlighted() ) {
            normal.setMask( hl.inverted() );

            highlighted.text( hl.text );
            highlighted.setMaxWidth( maxWidth );
            highlighted.measure();

            highlighted.setMask( hl.mask );
            highlighted.setVisible( true );
        } else {
            highlighted.setVisible( false );
        }

        setWidth( normal.width() );
        setHeight( normal.height() );
    }

    public void setColor ( int n, int h ) {
        normal.hardlight( n );
        highlighted.hardlight( h );
    }
}
