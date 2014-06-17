/**
 * 
 */
package codesketch.driver;

import java.util.List;

/**
 * @author quirino.brizi (quirino.brizi@gmail.com)
 * 
 */
public interface Command {

	List<byte[]> toBytePayload();
}
