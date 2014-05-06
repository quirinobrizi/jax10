package codesketch.x10.controllers.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import codesketch.x10.Address;
import codesketch.x10.actuator.Actuator;
import codesketch.x10.bus.Device;
import codesketch.x10.controller.impl.CM15;

public class CM15Test {

	@Test
	public void test() {
		Device device = mock(Device.class);
		// write
		when(device.write(anyByte(), any(byte[].class))).thenReturn(2);
		// ack success
		when(device.read(anyByte(), anyInt())).thenReturn(new byte[] { 0x55 });

		Address address = Address.forModule("A", "1");
		CM15 controller = new CM15(device);

		Actuator actuator = controller.actuator(address);
		Boolean success = actuator.on();

		assertTrue(success);
	}

}
