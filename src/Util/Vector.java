package Util;

/**
 * Directory: WarmVector_Client_Singleplayer/Util/
 * Created by Wyatt on 12/14/2015.
 */
public class Vector {

    public double x, y, z;

    public double[] array;

    public Vector() {
        x = y = z = 0.0;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0.0;
    }

    public Vector(Vector copy) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public void set(double[] source) {
        if (source.length >= 2) {
            x = source[0];
            y = source[1];
        }
        if (source.length >= 3) {
            z = source[2];
        }
    }

    static public Vector random2D() {
        return new Vector(MyMath.random(-1,1),MyMath.random(-1,1));
    }

    static public Vector random3D() {
        return new Vector(MyMath.random(-1,1),MyMath.random(-1,1),MyMath.random(-1,1));
    }

    static public Vector fromAngle(double angle) {
        return fromAngle(angle, null);
    }

    static public Vector fromAngle(double angle, Vector target) {
        if (target == null) {
            target = new Vector(Math.cos(angle),Math.sin(angle),0);
        } else {
            target.set(Math.cos(angle),Math.sin(angle),0);
        }
        return target;
    }

    public Vector get() {
        return new Vector(x, y, z);
    }

    public double[] get(double[] target) {
        if (target == null) {
            return new double[] { x, y, z };
        }
        if (target.length >= 2) {
            target[0] = x;
            target[1] = y;
        }
        if (target.length >= 3) {
            target[2] = z;
        }
        return target;
    }

    public double mag() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public double magSq() {
        return (x*x + y*y + z*z);
    }

    public void add(Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    static public Vector add(Vector v1, Vector v2) {
        return add(v1, v2, null);
    }

    static public Vector add(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x + v2.x,v1.y + v2.y, v1.z + v2.z);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }
        return target;
    }

    public void sub(Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    public void sub(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    static public Vector sub(Vector v1, Vector v2) {
        return sub(v1, v2, null);
    }

    static public Vector sub(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }
        return target;
    }

    public void mult(double n) {
        x *= n;
        y *= n;
        z *= n;
    }

    static public Vector mult(Vector v, double n) {
        return mult(v, n, null);
    }

    static public Vector mult(Vector v, double n, Vector target) {
        if (target == null) {
            target = new Vector(v.x*n, v.y*n, v.z*n);
        } else {
            target.set(v.x*n, v.y*n, v.z*n);
        }
        return target;
    }

    public void mult(Vector v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
    }

    static public Vector mult(Vector v1, Vector v2) {
        return mult(v1, v2, null);
    }


    static public Vector mult(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x*v2.x, v1.y*v2.y, v1.z*v2.z);
        } else {
            target.set(v1.x*v2.x, v1.y*v2.y, v1.z*v2.z);
        }
        return target;
    }

    public void div(double n) {
        x /= n;
        y /= n;
        z /= n;
    }

    static public Vector div(Vector v, double n) {
        return div(v, n, null);
    }

    static public Vector div(Vector v, double n, Vector target) {
        if (target == null) {
            target = new Vector(v.x/n, v.y/n, v.z/n);
        } else {
            target.set(v.x/n, v.y/n, v.z/n);
        }
        return target;
    }

    public void div(Vector v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
    }


    static public Vector div(Vector v1, Vector v2) {
        return div(v1, v2, null);
    }

    static public Vector div(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x/v2.x, v1.y/v2.y, v1.z/v2.z);
        } else {
            target.set(v1.x/v2.x, v1.y/v2.y, v1.z/v2.z);
        }
        return target;
    }

    public double dist(Vector v) {
        double dx = x - v.x;
        double dy = y - v.y;
        double dz = z - v.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    static public double dist(Vector v1, Vector v2) {
        double dx = v1.x - v2.x;
        double dy = v1.y - v2.y;
        double dz = v1.z - v2.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public double dot(Vector v) {
        return x*v.x + y*v.y + z*v.z;
    }

    public double dot(double x, double y, double z) {
        return this.x*x + this.y*y + this.z*z;
    }

    static public double dot(Vector v1, Vector v2) {
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }

    public Vector cross(Vector v) {
        return cross(v, null);
    }

    public Vector cross(Vector v, Vector target) {
        double crossX = y * v.z - v.y * z;
        double crossY = z * v.x - v.z * x;
        double crossZ = x * v.y - v.x * y;

        if (target == null) {
            target = new Vector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }

    static public Vector cross(Vector v1, Vector v2, Vector target) {
        double crossX = v1.y * v2.z - v2.y * v1.z;
        double crossY = v1.z * v2.x - v2.z * v1.x;
        double crossZ = v1.x * v2.y - v2.x * v1.y;

        if (target == null) {
            target = new Vector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }

    public void normalize() {
        double m = mag();
        if (m != 0 && m != 1) {
            div(m);
        }
    }

    public Vector normalize(Vector target) {
        if (target == null) {
            target = new Vector();
        }
        double m = mag();
        if (m > 0) {
            target.set(x/m, y/m, z/m);
        } else {
            target.set(x, y, z);
        }
        return target;
    }

    public void limit(double max) {
        if (magSq() > max*max) {
            normalize();
            mult(max);
        }
    }

    public void setMag(double len) {
        normalize();
        mult(len);
    }

    public Vector setMag(Vector target, double len) {
        target = normalize(target);
        target.mult(len);
        return target;
    }

    public double heading() {
        double angle = Math.atan2(-y, x);
        return -1*angle;
    }

    @Deprecated
    public double heading2D() {
        return heading();
    }

    public void rotate(double theta) {
        double xTemp = x;
        // Might need to check for rounding errors like with angleBetween function?
        x = x*Math.cos(theta) - y*Math.sin(theta);
        y = xTemp*Math.sin(theta) + y*Math.cos(theta);
    }

    public void lerp(Vector v, double amt) {
        x = MyMath.lerp(x,v.x,amt);
        y = MyMath.lerp(y,v.y,amt);
        z = MyMath.lerp(z,v.z,amt);
    }

    public static Vector lerp(Vector v1, Vector v2, double amt) {
        Vector v = v1.get();
        v.lerp(v2, amt);
        return v;
    }

    public void lerp(double x, double y, double z, double amt) {
        this.x = MyMath.lerp(this.x,x,amt);
        this.y = MyMath.lerp(this.y,y,amt);
        this.z = MyMath.lerp(this.z,z,amt);
    }

    static public double angleBetween(Vector v1, Vector v2) {
        double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
        double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
        // This should be a number between -1 and 1, since it's "normalized"
        double amt = dot / (v1mag * v2mag);
        // But if it's not due to rounding error, then we need to fix it
        // http://code.google.com/p/processing/issues/detail?id=340
        // Otherwise if outside the range, acos() will return NaN
        // http://www.cppreference.com/wiki/c/math/acos
        if (amt <= -1) {
            return Math.PI;
        } else if (amt >= 1) {
            // http://code.google.com/p/processing/issues/detail?id=435
            return 0;
        }
        return Math.acos(amt);
    }

    @Override
    public String toString() {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }

    public double[] array() {
        if (array == null) {
            array = new double[3];
        }
        array[0] = x;
        array[1] = y;
        array[2] = z;
        return array;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector))
            return false;
        final Vector p = (Vector) obj;
        return x == p.x && y == p.y && z == p.z;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.floatToIntBits((float)x);
        result = 31 * result + Float.floatToIntBits((float)y);
        result = 31 * result + Float.floatToIntBits((float)z);
        return result;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0.0;
    }
}
