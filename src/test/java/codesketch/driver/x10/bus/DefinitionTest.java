/**
 * Copyright [2014] [Quirino Brizi (quirino.brizi@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codesketch.driver.x10.bus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;

import org.junit.Test;

import codesketch.driver.Controller;
import codesketch.driver.x10.controller.impl.CM15;

/**
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public class DefinitionTest {

	/**
	 * Test method for
	 * {@link codesketch.driver.x10.bus.Definition#isValidDevice(short, short)}.
	 */
	@Test
	public void testIsKnownDevice_validDefinition() {
		Definition definition = Definition.CM15;
		boolean valid = Definition.isValidDevice(definition.getVendorId(), definition.getProductId());
		assertTrue(valid);
	}

	/**
	 * Test method for
	 * {@link codesketch.driver.x10.bus.Definition#isValidDevice(short, short)}.
	 */
	@Test
	public void testIsKnownDevice_invalidDefinition() {
		boolean valid = Definition.isValidDevice((short) 0x12ab4, (short) 0x3c44);
		assertFalse(valid);
	}

	@Test
	public void testBuildControllerFor_validDefinition() {
		Definition definition = Definition.CM15;
		UsbDevice usbDevice = mock(UsbDevice.class);
		UsbDeviceDescriptor usbDeviceDescriptor = mock(UsbDeviceDescriptor.class);
		when(usbDevice.getUsbDeviceDescriptor()).thenReturn(usbDeviceDescriptor);
		when(usbDeviceDescriptor.idVendor()).thenReturn(definition.getVendorId());
		when(usbDeviceDescriptor.idProduct()).thenReturn(definition.getProductId());
		// act
		Controller controller = Definition.buildControllerFor(usbDevice);
		// assert
		assertNotNull(controller);
		assertTrue(controller instanceof CM15);
	}

	@Test
	public void testBuildControllerFor_invalidDefinition() {
		UsbDevice usbDevice = mock(UsbDevice.class);
		UsbDeviceDescriptor usbDeviceDescriptor = mock(UsbDeviceDescriptor.class);
		when(usbDevice.getUsbDeviceDescriptor()).thenReturn(usbDeviceDescriptor);
		when(usbDeviceDescriptor.idVendor()).thenReturn((short) 0x12ab4);
		when(usbDeviceDescriptor.idProduct()).thenReturn((short) 0x3c44);
		// act
		Controller controller = Definition.buildControllerFor(usbDevice);
		// assert
		assertNull(controller);
	}
}
