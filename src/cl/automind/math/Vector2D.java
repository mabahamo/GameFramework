package cl.automind.math;

/**
 * Vector2D.java
 * Implementaci—n original de Mike Anderson.
 * 
 * Descargada desde 
 * http://home.student.uu.se/laan8601/oopjava/Vector2D.java
 */

public class Vector2D
{
    public double x = 0.0;
    public double y = 0.0;

    public Vector2D() {}
    public Vector2D( double x, double y ) { this.x = x; this.y = y; }
    public Vector2D( Vector2D vec ) { this.x = vec.x; this.y = vec.y; }


    public Vector2D(Coordinate pos) {
		this.x = pos.x;
		this.y = pos.y;
	}

	//  BASIC ASSIGNMENT METHODS
//-------------------------------
//
    void setTo( Vector2D vec )
    {
        x = vec.x;
        y = vec.y;
    }

    void setTo( double x, double y )
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return new String( "[" + x + "," + y + "]" + "  "  + this.length());
    }



//  MATHEMATICAL OPERATIONS
//-------------------------------
//  The core functionality of
//  geometrical vectors
//

//  1. Vector length and normalization
//-----------------------------------------
//
    public double length()
    {
        return Math.sqrt( (x*x) + (y*y) );
    }


    public Vector2D normalize()
    {
        double len = length();

        if( len != 0.0 )
        {
            x /= len;
            y /= len;
        }
        else
        {
            x = 0.0;
            y = 0.0;
        }

        return new Vector2D( this );
    }


//  2. Addition methods
//-------------------
//
    public Vector2D add( Vector2D vec )
    {
        this.x += vec.x;
        this.y += vec.y;

        return new Vector2D( this );
    }

    public Vector2D add( double x, double y )
    {
        this.x += x;
        this.y += y;

        return new Vector2D( this );
    }


//  3. Subtraction methods
//-----------------------
//
    public Vector2D sub( Vector2D vec )
    {
        this.x -= vec.x;
        this.y -= vec.y;

        return new Vector2D( this );
    }

    public Vector2D sub( double x, double y )
    {
        this.x -= x;
        this.y -= y;

        return new Vector2D( this );
    }


//  4. Multiplication methods
//-------------------------
//
    public Vector2D mul( Vector2D vec )
    {
        this.x *= vec.x;
        this.y *= vec.y;

        return new Vector2D( this );
    }

    public Vector2D mul( double x, double y )
    {
        this.x += x;
        this.y += y;

        return new Vector2D( this );
    }

    public Vector2D mul( double scalar )
    {
        x *= scalar;
        y *= scalar;

        return new Vector2D( this );
    }
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

}

