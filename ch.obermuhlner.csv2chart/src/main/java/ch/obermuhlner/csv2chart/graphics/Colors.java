package ch.obermuhlner.csv2chart.graphics;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Colors {

    private static Random random = new Random();

    private static Map<String, Color> namedColors = new HashMap<>();

    public static Color gray10 = new Color(0x1a1a1a);
    public static Color gray20 = new Color(0x333333);
    public static Color gray30 = new Color(0x4d4d4d);
    public static Color gray40 = new Color(0x666666);
    public static Color gray50 = new Color(0x7f7f7f);
    public static Color gray60 = new Color(0x999999);
    public static Color gray70 = new Color(0xb3b3b3);
    public static Color gray80 = new Color(0xcccccc);
    public static Color gray90 = new Color(0xe5e5e5);

    public static Color solarizedBrBlack = new Color(0x002b36);
    public static Color solarizedBlack = new Color(0x073642);
    public static Color solarizedWhite = new Color(0xeee8d5);
    public static Color solarizedBrWhite= new Color(0xfdf6e3);
    public static Color solarizedBrGreen = new Color(0x585858);
    public static Color solarizedBrYellow = new Color(0x657b83);
    public static Color solarizedBrBlue= new Color(0x839496);
    public static Color solarizedBrCyan = new Color(0x93a1a1);
    public static Color solarizedYellow = new Color(0xb58900);
    public static Color solarizedOrange = new Color(0xcb4b16);
    public static Color solarizedRed = new Color(0xdc322f);
    public static Color solarizedMagenta = new Color(0xd33682);
    public static Color solarizedViolet = new Color(0x6c71c4);
    public static Color solarizedBlue = new Color(0x268bd2);
    public static Color solarizedCyan = new Color(0x2aa198);
    public static Color solarizedGreen = new Color(0x859900);

    public static Color solarizedBackgroundDarkest = solarizedBrBlack;
    public static Color solarizedBackgroundDark = solarizedBlack;
    public static Color solarizedBackgroundLight = solarizedWhite;
    public static Color solarizedBackgroundLightest = solarizedBrWhite;
    public static Color solarizedContentDarkest = solarizedBrGreen;
    public static Color solarizedContentDark = solarizedBrYellow;
    public static Color solarizedContentLight= solarizedBrBlue;
    public static Color solarizedContentLightest = solarizedBrCyan;

    static {
        namedColors.put("black", Color.black);
        namedColors.put("blue", Color.blue);
        namedColors.put("cyan", Color.cyan);
        namedColors.put("darkgray", Color.darkGray);
        namedColors.put("gray", Color.gray);
        namedColors.put("green", Color.green);
        namedColors.put("lightgray", Color.lightGray);
        namedColors.put("magenta", Color.magenta);
        namedColors.put("orange", Color.orange);
        namedColors.put("pink", Color.pink);
        namedColors.put("red", Color.red);
        namedColors.put("white", Color.white);
        namedColors.put("yellow", Color.yellow);
        namedColors.put("gray10", gray10);
        namedColors.put("gray20", gray20);
        namedColors.put("gray30", gray30);
        namedColors.put("gray40", gray40);
        namedColors.put("gray50", gray50);
        namedColors.put("gray60", gray60);
        namedColors.put("gray70", gray70);
        namedColors.put("gray80", gray80);
        namedColors.put("gray90", gray90);
        namedColors.put("solarizedBackgroundDarkest", solarizedBackgroundDarkest);
        namedColors.put("solarizedBackgroundDark", solarizedBackgroundDark);
        namedColors.put("solarizedBackgroundLight", solarizedBackgroundLight);
        namedColors.put("solarizedBackgroundLightest", solarizedBackgroundLightest);
        namedColors.put("solarizedContentDarkest", solarizedContentDarkest);
        namedColors.put("solarizedContentDark", solarizedContentDark);
        namedColors.put("solarizedContentLight", solarizedContentLight);
        namedColors.put("solarizedContentLightest", solarizedContentLightest);
        namedColors.put("solarizedYellow", solarizedYellow);
        namedColors.put("solarizedOrange", solarizedOrange);
        namedColors.put("solarizedRed", solarizedRed);
        namedColors.put("solarizedMagenta", solarizedMagenta);
        namedColors.put("solarizedViolet", solarizedViolet);
        namedColors.put("solarizedBlue", solarizedBlue);
        namedColors.put("solarizedCyan", solarizedCyan);
        namedColors.put("solarizedGreen", solarizedGreen);
    }

    public static Color parseColor(String string) {
        Color color = namedColors.get(string);

        if (string.equals("random")) {
            color = new Color(random.nextInt(0x1000000));
        }

        if (color == null) {
            color = new Color(Integer.parseInt(string, 16));
        }

        if (color == null) {
            color = Color.red;
        }
        return color;
    }

    public static Color hsbToColor(double hue, double saturation, double brightness) {
        return new Color(Color.HSBtoRGB((float) hue, (float) saturation, (float) brightness));
    }

    public static Color ahsbToColor(double alpha, double hue, double saturation, double brightness) {
        int a = (int) (alpha * 255.0 + 0.5);
        int rgb = 0xffffff & Color.HSBtoRGB((float) hue, (float) saturation, (float) brightness);
        int argb = (a << 24) | rgb;
        return new Color(argb, true);
    }
}
