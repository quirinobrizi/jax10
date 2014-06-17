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
package codesketch.driver.x10.bus.usb;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.usb.UsbDeviceDescriptor;
import javax.usb.event.UsbServicesEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import codesketch.driver.Controller;
import codesketch.driver.listener.ConnectionListener;
import codesketch.driver.x10.Module;
import codesketch.driver.x10.controller.X10Controller;
import codesketch.driver.x10.usb.X10ServicesListener;

/**
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultUsbServicesListenerTest {

	@Mock
	private ConnectionListener busEventListener;
	@Captor
	private ArgumentCaptor<Controller> deviceCaptor;

	@Mock
	private UsbServicesEvent event;
	@Mock
	private javax.usb.UsbDevice usbDevice;
	@Mock
	private UsbDeviceDescriptor usbDeviceDescriptor;

	private X10ServicesListener testObj;

	@Before
	public void beforeMethod() {
		this.testObj = new X10ServicesListener(this.busEventListener);
		when(this.event.getUsbDevice()).thenReturn(usbDevice);
		when(this.usbDevice.getUsbDeviceDescriptor()).thenReturn(usbDeviceDescriptor);
	}

	/**
	 * Test method for
	 * {@link codesketch.driver.x10.usb.X10ServicesListener#usbDeviceAttached(javax.usb.event.UsbServicesEvent)}
	 * .
	 */
	@Test
	public void testUsbDeviceAttached_notPropagatedIfEventFromUnknownDevice() {
		// act
		this.testObj.usbDeviceAttached(event);
		// verify
		verify(this.busEventListener, never()).onConnect(any(Controller.class));
	}

	/**
	 * Test method for
	 * {@link codesketch.driver.x10.usb.X10ServicesListener#usbDeviceAttached(javax.usb.event.UsbServicesEvent)}
	 * and propagated device is not null and built accordingly to received event
	 * .
	 */
	@Test
	public void testUsbDeviceAttached_deviceIsBuilt() {
		when(this.usbDeviceDescriptor.idVendor()).thenReturn(Module.CM15.getVendorId());
		when(this.usbDeviceDescriptor.idProduct()).thenReturn(Module.CM15.getProductId());
		// act
		this.testObj.usbDeviceAttached(event);
		// verify
		verify(this.busEventListener).onConnect(this.deviceCaptor.capture());
		Controller device = this.deviceCaptor.getValue();
		assertTrue(device instanceof X10Controller);
	}

	/**
	 * Test method for
	 * {@link codesketch.driver.x10.usb.X10ServicesListener#usbDeviceDetached(javax.usb.event.UsbServicesEvent)}
	 * .
	 */
	@Test
	public void testUsbDeviceDetached_notPropagatedIfEventFromUnknownDevice() {
		// act
		this.testObj.usbDeviceDetached(event);
		// verify
		verify(this.busEventListener, never()).onDisconnect(any(Controller.class));
	}

	/**
	 * Test method for
	 * {@link codesketch.driver.x10.usb.X10ServicesListener#usbDeviceDetached(javax.usb.event.UsbServicesEvent)}
	 * .
	 */
	@Test
	public void testUsbDeviceDetached_deviceIsBuilt() {
		when(this.usbDeviceDescriptor.idVendor()).thenReturn(Module.CM15.getVendorId());
		when(this.usbDeviceDescriptor.idProduct()).thenReturn(Module.CM15.getProductId());
		// act
		this.testObj.usbDeviceDetached(event);
		// verify
		verify(this.busEventListener).onDisconnect(this.deviceCaptor.capture());
		Controller controller = this.deviceCaptor.getValue();
		assertTrue(controller instanceof X10Controller);
	}

}
