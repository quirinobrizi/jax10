package codesketch.driver.x10.actuators;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import codesketch.driver.Address;
import codesketch.driver.Device;
import codesketch.driver.x10.actuator.DefaultX10Actuator;
import codesketch.driver.x10.controller.impl.CM15;

public class SwitchableX10ActuatorTest {

	@Test
	public void testOn_success() {
		Device device = mock(Device.class);
		// write
		when(device.write(anyByte(), any(byte[].class))).thenReturn(2);
		// ack success
		when(device.read(anyByte(), anyInt())).thenReturn(new byte[] { 0x55 });

		CM15 controller = new CM15(device);
		Address address = Address.forModule("A", "1");

		DefaultX10Actuator testObj = new DefaultX10Actuator(address, controller);
		Boolean success = testObj.on();

		verify(device, times(3)).open();
		verify(device, times(3)).claim();
		verify(device).write(anyByte(), eq(new byte[] { 0x04, 0x66 }));
		verify(device).write(anyByte(), eq(new byte[] { 0x06, 0x62 }));
		assertTrue(success);
	}

	@Test
	public void testOff_success() {
		Device device = mock(Device.class);
		// write
		when(device.write(anyByte(), any(byte[].class))).thenReturn(2);
		// ack success
		when(device.read(anyByte(), anyInt())).thenReturn(new byte[] { 0x55 });

		CM15 controller = new CM15(device);
		Address address = Address.forModule("A", "1");

		DefaultX10Actuator testObj = new DefaultX10Actuator(address, controller);
		Boolean success = testObj.off();

		verify(device, times(3)).open();
		verify(device, times(3)).claim();
		verify(device).write(anyByte(), eq(new byte[] { 0x04, 0x66 }));
		verify(device).write(anyByte(), eq(new byte[] { 0x06, 0x63 }));
		assertTrue(success);
	}
}
