/**
 * 
 */
package codesketch.driver.x10;

/**
 * @author quirino.brizi (quirino.brizi@gmail.com)
 * 
 */
public final class Utils {

	public static void prettyPrint(String prefix, byte[] sequence) {
		StringBuffer buffer = new StringBuffer(prefix);
		buffer.append(": [");
		for (byte b : sequence) {
			buffer.append(String.format("0x%02X,", b));
		}
		buffer.append("]");
		System.out.println(buffer.toString());
	}
}
