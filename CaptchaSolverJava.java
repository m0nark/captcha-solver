import java.util.*;
import java.util.regex.*;

public class CaptchaSolverJava {

    static final String a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    static final Map<String, Character> asciiToCharMap = new HashMap<>();

    static final String[][] t = {
        {"   *    ", "  * *   ", "  * *   ", " *   *  ", " *****  ", "*     * ", "*     * "},  // A
        {"******  ", "*     * ", "*     * ", "******  ", "*     * ", "*     * ", "******  "},  // B
        {" *****  ", "*     * ", "*       ", "*       ", "*       ", "*     * ", " *****  "},  // C
        {"*****   ", "*    *  ", "*     * ", "*     * ", "*     * ", "*    *  ", "*****   "},  // D
        {"******* ", "*       ", "*       ", "****    ", "*       ", "*       ", "******* "},  // E
        {"******* ", "*       ", "*       ", "****    ", "*       ", "*       ", "*       "},  // F
        {" *****  ", "*     * ", "*       ", "*       ", "*   *** ", "*     * ", " *****  "},  // G
        {"*     * ", "*     * ", "*     * ", "******* ", "*     * ", "*     * ", "*     * "},  // H
        {"******* ", "   *    ", "   *    ", "   *    ", "   *    ", "   *    ", "******* "},  // I
        {"      * ", "      * ", "      * ", "      * ", "      * ", "*     * ", " *****  "},  // J
        {"*    *  ", "*   *   ", "*  *    ", "***     ", "*  *    ", "*   *   ", "*    *  "},  // K
        {"*       ", "*       ", "*       ", "*       ", "*       ", "*       ", "******* "},  // L
        {"*     * ", "**   ** ", "* * * * ", "*  *  * ", "*     * ", "*     * ", "*     * "},  // M
        {"*     * ", "**    * ", "* *   * ", "*  *  * ", "*   * * ", "*    ** ", "*     * "},  // N
        {" *****  ", "*     * ", "*     * ", "*     * ", "*     * ", "*     * ", " *****  "},  // O
        {"******  ", "*     * ", "*     * ", "******  ", "*       ", "*       ", "*       "},  // P
        {" *****  ", "*     * ", "*     * ", "*     * ", "*   * * ", "*    *  ", " **** * "},  // Q
        {"******  ", "*     * ", "*     * ", "******  ", "*   *   ", "*    *  ", "*     * "},  // R
        {" *****  ", "*     * ", "*       ", " *****  ", "      * ", "*     * ", " *****  "},  // S
        {"******* ", "   *    ", "   *    ", "   *    ", "   *    ", "   *    ", "   *    "},  // T
        {"*     * ", "*     * ", "*     * ", "*     * ", "*     * ", "*     * ", " *****  "},  // U
        {"*     * ", "*     * ", "*     * ", " *   *  ", "  * *   ", "   *    ", "   *    "},  // V
        {"*     * ", "*     * ", "*     * ", "*  *  * ", "* * * * ", "**   ** ", "*     * "},  // W
        {"*     * ", " *   *  ", "  * *   ", "   *    ", "  * *   ", " *   *  ", "*     * "},  // X
        {"*     * ", " *   *  ", "  * *   ", "   *    ", "   *    ", "   *    ", "   *    "},  // Y
        {"******* ", "     *  ", "    *   ", "   *    ", "  *     ", " *      ", "******* "},  // Z
        {"  ***   ", " *   *  ", "*   * * ", "*  *  * ", "* *   * ", " *   *  ", "  ***   "},  // 0
        {"   *    ", "  **    ", " * *    ", "   *    ", "   *    ", "   *    ", "******* "},  // 1
        {" *****  ", "*     * ", "      * ", "     *  ", "   **   ", " **     ", "******* "},  // 2
        {" *****  ", "*     * ", "      * ", "    **  ", "      * ", "*     * ", " *****  "},  // 3
        {"    *   ", "   **   ", "  * *   ", " *  *   ", "******* ", "    *   ", "    *   "},  // 4
        {"******* ", "*       ", "******  ", "      * ", "      * ", "*     * ", " *****  "},  // 5
        {"  ****  ", " *      ", "*       ", "******  ", "*     * ", "*     * ", " *****  "},  // 6
        {"******* ", "     *  ", "    *   ", "   *    ", "  *     ", " *      ", "*       "},  // 7
        {" *****  ", "*     * ", "*     * ", " *****  ", "*     * ", "*     * ", " *****  "},  // 8
        {" *****  ", "*     * ", "*     * ", " ****** ", "      * ", "     *  ", " ****   "}   // 9
    };
    
    static {
        for (int i = 0; i < t.length; i++) {
            StringBuilder keyBuilder = new StringBuilder();
            for (String line : t[i]) {
                keyBuilder.append(line).append("\n");
            }
            asciiToCharMap.put(keyBuilder.toString(), a.charAt(i));
        }
    }

    static String cleanHtmlToAscii(String html) {
        String cleaned = html.replace("&nbsp;", "█");
        cleaned = cleaned.replaceAll("</div>|<div.*?>", "");
        cleaned = cleaned.replace("<br>", "\n");
        cleaned = cleaned.replace("█", " ");
        return cleaned;
    }

    static String generateCaptcha() {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(a.charAt(rand.nextInt(a.length())));
        }
        return sb.toString();
    }

    static String fetchStringFromAscii(String asciiArt) {
        String[] lines = asciiArt.split("\n");
        int maxLen = 0;
        for (String line : lines) {
            maxLen = Math.max(maxLen, line.length());
            System.out.println(line);
        }
        for (int i = 0; i < lines.length; i++) {
            lines[i] = String.format("%-" + maxLen + "s", lines[i]);
        }

        int numChars = lines[0].length() / 9;
        StringBuilder result = new StringBuilder();

        for (int c = 0; c < numChars; c++) {
            String[] block = new String[7];
            for (int r = 0; r < 7; r++) {
                int start = c * 9;
                block[r] = lines[r].substring(start, Math.min(start + 8, lines[r].length()));
            }

            StringBuilder keyBuilder = new StringBuilder();
            for (String line : block) {
                keyBuilder.append(line).append("\n");
            }
            
            Character ch = asciiToCharMap.get(keyBuilder.toString());
            result.append(ch != null ? ch : '?');
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String htmlResponse = "<div class=\"realperson-text\">&nbsp;*****&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;*****&nbsp;&nbsp;&nbsp;*******&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;<br>*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;**&nbsp;&nbsp;&nbsp;**&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;**&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;<br>*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;*&nbsp;*&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;*&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;<br>&nbsp;*****&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;*****&nbsp;&nbsp;&nbsp;****&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;<br>*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;*&nbsp;*&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;*&nbsp;*&nbsp;&nbsp;<br>*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**&nbsp;&nbsp;&nbsp;**&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;**&nbsp;&nbsp;<br>&nbsp;*****&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;*****&nbsp;&nbsp;&nbsp;*******&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;<br></div>";

        String asciiArt = cleanHtmlToAscii(htmlResponse);
        String captcha = generateCaptcha();

        System.out.println("Generated CAPTCHA: " + captcha);
        System.out.println("\nASCII Art:\n" + asciiArt);
        System.out.println("\nDecoded String: " + fetchStringFromAscii(asciiArt));
    }
}
