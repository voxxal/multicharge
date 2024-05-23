import java.io.Serializable;

public class Color implements Serializable {

    public byte r;
    public byte g;
    public byte b;
    public byte a;

    public Color() {}

    public Color(byte r, byte g, byte b, byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(byte r, byte g, byte b) {
        this(r, g, b, 255);
    }

    public Color(int r, int g, int b) {
        this((byte) r, (byte) g, (byte) b);
    }

    public Color(int r, int g, int b, int a) {
        this((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public com.raylib.Raylib.Color toRaylib() {
        return new com.raylib.Raylib.Color().r(r).g(g).b(b).a(a);
    }

    public Color scale(double m) {
        return new Color((int) (m * ((int) r & 255)), (int) (m * ((int) g & 255)), (int) (m * ((int) b & 255)));
    }

    public String toString() {
        return String.format("(%d, %d, %d, %d)", r, g, b, a);
    }
}
