/**
 *
 */
package codesketch.driver.x10;

/**
 * @author quirino.brizi (quirino.brizi@gmail.com)
 *
 */
public final class Utils {

    public static String formatHexToString(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (byte b : bytes) {
            buffer.append(String.format("0x%02X,", b));
        }
        buffer.append("]");
        return buffer.toString();
    }

	public static void prettyPrint(String prefix, byte[] sequence) {
		System.out.println(prefix + ": " + formatHexToString(sequence));
	}
}
