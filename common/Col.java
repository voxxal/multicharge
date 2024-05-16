import static com.raylib.Raylib.Color;

import java.io.Serializable;

public class Col implements Serializable {

    public byte r;
    public byte g;
    public byte b;
    public byte a;

    public Col() {}

    public Col(byte r, byte g, byte b, byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Col(byte r, byte g, byte b) {
        Col(r, g, b, 255);
    }

    public Col(int r, int g, int b){
        Col((byte) r, (byte) g, (byte) b);
    }

    public Col(int r, int g, int b, int a){
        Col((byte) r, (byte) g, (byte) b, (byte)a);
    }

    public Color toRaylib() {
        return new Color().r(r).g(g).b(b).a(a);
    }

    public String toString() {
        return String.format("(%d, %d, %d, %d)", r, g, b, a);
    }
}
