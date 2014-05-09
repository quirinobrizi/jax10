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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.usb.UsbException;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import codesketch.driver.x10.bus.BusEventListener;
import codesketch.driver.x10.bus.usb.UsbControllerProvider;
import codesketch.driver.x10.bus.usb.UsbServicesProvider;
import codesketch.driver.x10.bus.usb.exception.UsbOperationException;

/**
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class UsbControllerProviderTest {

	@Mock
	private UsbServicesProvider usbServicesProvider;

	private UsbControllerProvider testObj;

	@Before
	public void beforeMethod() {
		this.testObj = new UsbControllerProvider(this.usbServicesProvider);
	}

	/**
	 * Test method for
	 * {@link codesketch.driver.x10.bus.usb.UsbControllerProvider#registerEventListener(codesketch.driver.x10.bus.BusEventListener)}
	 * .
	 * 
	 * @throws UsbException
	 */
	@Test
	public void testRegisterEventListener() throws UsbException {
		// setup
		UsbServices usbServices = mock(UsbServices.class);
		when(this.usbServicesProvider.getUsbServices()).thenReturn(usbServices);
		BusEventListener busEventListener = mock(BusEventListener.class);
		// act
		this.testObj.registerEventListener(busEventListener);
		// verify
		verify(this.usbServicesProvider).getUsbServices();
		verify(usbServices).addUsbServicesListener(any(UsbServicesListener.class));
	}

	/**
	 * Test method for
	 * {@link codesketch.driver.x10.bus.usb.UsbControllerProvider#registerEventListener(codesketch.driver.x10.bus.BusEventListener)}
	 * throw {@link UsbOperationException}.
	 * 
	 * @throws UsbException
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = UsbOperationException.class)
	public void testRegisterEventListener_throwsUsbOperationException() throws UsbException {
		// setup
		when(this.usbServicesProvider.getUsbServices()).thenThrow(UsbException.class);
		BusEventListener busEventListener = mock(BusEventListener.class);
		// act
		this.testObj.registerEventListener(busEventListener);
	}

}
