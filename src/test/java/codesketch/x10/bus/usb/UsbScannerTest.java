/**
 * 
 */
package codesketch.x10.bus.usb;

import java.util.List;

import org.junit.Test;

import codesketch.x10.Address;
import codesketch.x10.actuator.Actuator;
import codesketch.x10.controller.X10Controller;

/**
 * @author quirino.brizi (quirino.brizi@gmail.com)
 *
 */
public class UsbScannerTest {

	private final UsbScanner testObj = new UsbScanner();

	@Test
	public void test() throws Exception {

		List<X10Controller> controllers = testObj.scan();
		X10Controller x10Controller = controllers.get(0);
		Address address = Address.forModule("A", "1");
		Actuator actuator = x10Controller.actuator(address);
		Boolean executed = actuator.off();
		System.out.println(executed);
	}

}
