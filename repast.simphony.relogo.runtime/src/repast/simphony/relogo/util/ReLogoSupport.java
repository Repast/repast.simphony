package repast.simphony.relogo.util;

import java.awt.Color;
import java.util.LinkedList;

/**
 *
 * @author CBURKE
 */
public class ReLogoSupport  {

    static protected LinkedList<Color> colors;

    static {
    	initColors();
    }
    
    static public void initColors() {
        //Color 
        /*
         * 
         * NOTE: Although the NetLogo documentation refers to HSB,
         * the color map implies that it is the hue-saturation-lightness
         * transformation rather than hue-saturation-value which is being
         * used. Therefore, the standard Java methods for transforming 
         * color spaces can't be used, and new ones must be crafted.
         * 
         * It looks like NetLogo's colors were crafted as a table,
         * and the transformation stuff was layered on top. So the
         * numbered colors are not evenly spaced around the color space.
         */
        /*
        black = 0 
        gray = 5 
        white = 9.9 
        red = 15 
        orange = 25 
        brown = 35 
        yellow = 45 
        green = 55 
        lime = 65 
        turquoise = 75 
        cyan = 85 
        sky = 95 
        blue = 105 
        violet = 115 
        magenta = 125 
        pink = 135 
         */
        colors = new LinkedList<Color>();
        colors.add(new Color(0, 0, 0)); // 0
        colors.add(new Color(31, 31, 31));
        colors.add(new Color(59, 59, 59));
        colors.add(new Color(87, 87, 87));
        colors.add(new Color(114, 114, 114));
        colors.add(new Color(141, 141, 141));
        colors.add(new Color(164, 164, 164));
        colors.add(new Color(186, 186, 186));
        colors.add(new Color(209, 209, 209));
        colors.add(new Color(232, 232, 232));
        colors.add(new Color(5, 2, 1)); // 10
        colors.add(new Color(48, 11, 9));
        colors.add(new Color(90, 21, 17));
        colors.add(new Color(133, 30, 25));
        colors.add(new Color(175, 40, 32));
        colors.add(new Color(215, 50, 41));
        colors.add(new Color(223, 91, 84));
        colors.add(new Color(231, 132, 137));
        colors.add(new Color(239, 173, 169));
        colors.add(new Color(247, 214, 212));
        colors.add(new Color(6, 3, 1)); // 20
        colors.add(new Color(53, 24, 5));
        colors.add(new Color(101, 44, 8));
        colors.add(new Color(149, 65, 12));
        colors.add(new Color(197, 86, 16));
        colors.add(new Color(241, 106, 21));
        colors.add(new Color(243, 136, 68));
        colors.add(new Color(246, 166, 115));
        colors.add(new Color(249, 195, 162));
        colors.add(new Color(252, 225, 208));
        colors.add(new Color(4, 3, 2)); // 30
        colors.add(new Color(35, 24, 16));
        colors.add(new Color(66, 46, 30));
        colors.add(new Color(97, 68, 44));
        colors.add(new Color(127, 89, 57));
        colors.add(new Color(157, 110, 72));
        colors.add(new Color(176, 139, 108));
        colors.add(new Color(196, 168, 145));
        colors.add(new Color(216, 197, 182));
        colors.add(new Color(235, 226, 218));
        colors.add(new Color(6, 6, 2)); // 40
        colors.add(new Color(53, 53, 11));
        colors.add(new Color(99, 99, 20));
        colors.add(new Color(146, 146, 29));
        colors.add(new Color(193, 193, 39));
        colors.add(new Color(237, 237, 49));
        colors.add(new Color(240, 240, 90));
        colors.add(new Color(244, 244, 131));
        colors.add(new Color(247, 247, 173));
        colors.add(new Color(251, 251, 214));
        colors.add(new Color(2, 4, 2)); // 50
        colors.add(new Color(20, 39, 13));
        colors.add(new Color(37, 74, 25));
        colors.add(new Color(54, 109, 36));
        colors.add(new Color(71, 144, 48));
        colors.add(new Color(89, 176, 60));
        colors.add(new Color(122, 192, 99));
        colors.add(new Color(155, 208, 138));
        colors.add(new Color(188, 223, 177));
        colors.add(new Color(222, 239, 216));
        colors.add(new Color(1, 5, 2)); // 60
        colors.add(new Color(10, 46, 13));
        colors.add(new Color(18, 88, 24));
        colors.add(new Color(26, 129, 36));
        colors.add(new Color(35, 171, 47));
        colors.add(new Color(44, 209, 59));
        colors.add(new Color(86, 218, 98));
        colors.add(new Color(128, 227, 137));
        colors.add(new Color(171, 236, 177));
        colors.add(new Color(213, 246, 216));
        colors.add(new Color(1, 4, 3)); // 70
        colors.add(new Color(6, 35, 27));
        colors.add(new Color(12, 66, 50));
        colors.add(new Color(17, 98, 74));
        colors.add(new Color(22, 129, 97));
        colors.add(new Color(29, 159, 120));
        colors.add(new Color(74, 178, 147));
        colors.add(new Color(120, 197, 174));
        colors.add(new Color(165, 216, 201));
        colors.add(new Color(210, 236, 228));
        colors.add(new Color(2, 5, 5)); // 80
        colors.add(new Color(19, 44, 44));
        colors.add(new Color(35, 82, 82));
        colors.add(new Color(51, 121, 121));
        colors.add(new Color(67, 160, 160));
        colors.add(new Color(84, 196, 196));
        colors.add(new Color(118, 208, 208));
        colors.add(new Color(152, 220, 220));
        colors.add(new Color(186, 231, 231));
        colors.add(new Color(221, 243, 243));
        colors.add(new Color(1, 4, 5)); // 90
        colors.add(new Color(10, 31, 42));
        colors.add(new Color(18, 59, 80));
        colors.add(new Color(27, 87, 118));
        colors.add(new Color(35, 114, 155));
        colors.add(new Color(45, 141, 190));
        colors.add(new Color(87, 164, 203));
        colors.add(new Color(129, 186, 216));
        colors.add(new Color(171, 209, 229));
        colors.add(new Color(213, 232, 242));
        colors.add(new Color(2, 2, 4)); // 100
        colors.add(new Color(11, 21, 37));
        colors.add(new Color(21, 39, 71));
        colors.add(new Color(31, 57, 104));
        colors.add(new Color(41, 75, 137));
        colors.add(new Color(52, 93, 169));
        colors.add(new Color(93, 126, 186));
        colors.add(new Color(133, 158, 203));
        colors.add(new Color(174, 190, 220));
        colors.add(new Color(214, 223, 237));
        colors.add(new Color(3, 2, 4)); // 110
        colors.add(new Color(28, 18, 36));
        colors.add(new Color(52, 33, 69));
        colors.add(new Color(76, 49, 101));
        colors.add(new Color(101, 64, 133));
        colors.add(new Color(124, 80, 164));
        colors.add(new Color(150, 115, 182));
        colors.add(new Color(176, 150, 200));
        colors.add(new Color(203, 185, 218));
        colors.add(new Color(229, 220, 237));
        colors.add(new Color(4, 1, 3)); // 120
        colors.add(new Color(37, 6, 24));
        colors.add(new Color(70, 11, 44));
        colors.add(new Color(103, 16, 65));
        colors.add(new Color(136, 21, 86));
        colors.add(new Color(167, 27, 106));
        colors.add(new Color(184, 73, 136));
        colors.add(new Color(202, 118, 166));
        colors.add(new Color(219, 164, 195));
        colors.add(new Color(237, 210, 225));
        colors.add(new Color(5, 3, 4)); // 130
        colors.add(new Color(50, 28, 33));
        colors.add(new Color(94, 53, 63));
        colors.add(new Color(138, 78, 92));
        colors.add(new Color(183, 103, 122));
        colors.add(new Color(224, 127, 150));
        colors.add(new Color(230, 153, 171));
        colors.add(new Color(236, 178, 192));
        colors.add(new Color(242, 204, 213));
        colors.add(new Color(248, 229, 234));
    	
    }

    static public Color lookupColor(double cnum) {
        while (cnum < 0.0) {
            cnum += 140.0;
        }
        while (cnum >= 140.0) {
            cnum -= 140.0;
        }
        int idx = (int) cnum;
        Color baseColor = colors.get(idx);
        double rem = cnum - (double) idx;
        if (rem > 0.0) {
            int idx2 = idx + 1;
            Color nextColor;
            if (idx2 % 10 == 0) {
                // top of each color band is white
                nextColor = Color.white;
                if (rem > 0.9) {  // by definition, xx9.9+ is pure white.
                    return Color.white;
                }
            } else {
                nextColor = colors.get(idx2);
            }
            int newRed = baseColor.getRed() + (int) (rem * (nextColor.getRed() - baseColor.getRed()));
            int newGreen = baseColor.getGreen() + (int) (rem * (nextColor.getGreen() - baseColor.getGreen()));
            int newBlue = baseColor.getBlue() + (int) (rem * (nextColor.getBlue() - baseColor.getBlue()));
            return new Color(newRed, newGreen, newBlue);
        } else {
            return baseColor;
        }
    }

    static public int lookupNearestColor(Color c) {
        int minIdx = -1;
        double minDist = Double.MAX_VALUE;
        int idx = 0;
        for (Color ci : colors) {
            double rdist = c.getRed() - ci.getRed();
            double gdist = c.getGreen() - ci.getGreen();
            double bdist = c.getBlue() - ci.getBlue();
            double dist = rdist * rdist + gdist * gdist + bdist * bdist;
            if (dist < minDist) {
                minDist = dist;
                minIdx = idx;
            }
            idx++;
        }
        return minIdx;
    }
    
    static public LinkedList<Color> baseColors() {
        LinkedList<Color> baseColors = new LinkedList<Color>();
        baseColors.addAll(colors);
        return baseColors;    	
    }

    static protected String unquote(String s) {
        if (s.startsWith("\"")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    static protected String unescape(String s) {
        if (s == null) {
            return s;
        }
        StringBuffer buf = new StringBuffer(s);
        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '\\') {
                // process an escape code
                //   \n	New line
                //   \t	Tab
                //   \b	Backspace
                //   \r	Carriage return
                //   \f	Formfeed
                //   \\	Backslash
                //   \'	Single quotation mark
                //   \"	Double quotation mark
                //   \d	Octal
                //   \xd	Hexadecimal
                //   \ ud	Unicode character
                char esc = buf.charAt(i + 1);
                switch (esc) {
                    case '0':
                    case '1':
                    case '2':
                    case '3': {
                        char esc2 = buf.charAt(i + 2);
                        char esc3 = buf.charAt(i + 3);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        int ch = 0;
                        int tmp = esc - '0';
                        int tmp2 = esc2 - '0';
                        int tmp3 = esc3 - '0';
                        ch = (tmp * 8 + tmp2) * 8 + tmp3;
                        if (tmp > 7 || tmp2 > 7 || tmp3 > 7 || tmp < 0 || tmp2 < 0 || tmp3 < 0) {
                            System.err.println("Unexpected character in Unicode escape.");
                        } else {
                            buf.setCharAt(i, (char) ch);
                        }
                        break;
                    }
                    case 'x': {
                        char esc2 = buf.charAt(i + 2);
                        char esc3 = buf.charAt(i + 3);
                        char esc4 = buf.charAt(i + 4);
                        char esc5 = buf.charAt(i + 5);
                        int tmp2 = "0123456789ABCDEF".indexOf(esc2);
                        int tmp3 = "0123456789ABCDEF".indexOf(esc3);
                        int tmp4 = "0123456789ABCDEF".indexOf(esc4);
                        int tmp5 = "0123456789ABCDEF".indexOf(esc5);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        int ch = 0;
                        ch = ((tmp2 * 16 + tmp3) * 16 + tmp4) * 16 + tmp5;
                        if (tmp2 > 15 || tmp3 > 15 || tmp4 > 15 || tmp5 > 15 || tmp2 < 0 || tmp3 < 0 || tmp4 < 0 || tmp5 < 0) {
                            System.err.println("Unexpected character in Unicode escape.");
                        } else {
                            buf.setCharAt(i, (char) ch);
                        }
                        break;
                    }
                    case 'u': {
                        char esc2 = buf.charAt(i + 2);
                        char esc3 = buf.charAt(i + 3);
                        char esc4 = buf.charAt(i + 4);
                        char esc5 = buf.charAt(i + 5);
                        int tmp2 = "0123456789ABCDEF".indexOf(esc2);
                        int tmp3 = "0123456789ABCDEF".indexOf(esc3);
                        int tmp4 = "0123456789ABCDEF".indexOf(esc4);
                        int tmp5 = "0123456789ABCDEF".indexOf(esc5);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        int ch = 0;
                        ch = ((tmp2 * 16 + tmp3) * 16 + tmp4) * 16 + tmp5;
                        if (tmp2 > 15 || tmp3 > 15 || tmp4 > 15 || tmp5 > 15 || tmp2 < 0 || tmp3 < 0 || tmp4 < 0 || tmp5 < 0) {
                            System.err.println("Unexpected character in Unicode escape.");
                        } else {
                            buf.setCharAt(i, (char) ch);
                        }
                        break;
                    }
                    case 'n':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\n');
                        break;
                    case 't':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\t');
                        break;
                    case 'b':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\b');
                        break;
                    case 'r':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\r');
                        break;
                    case 'f':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\f');
                        break;
                    case '\\':
                    case '\'':
                    case '\"':
                        buf.deleteCharAt(i);
                        break;
                }
            }
        }
        return buf.toString();
    }

}
