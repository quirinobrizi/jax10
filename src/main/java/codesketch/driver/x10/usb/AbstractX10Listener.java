/**
 * Copyright [2014] [Quirino Brizi (quirino.brizi@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package codesketch.driver.x10.usb;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;

import codesketch.driver.Controller;
import codesketch.driver.x10.Module;

/**
 * Groups common methods for listeners.
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public abstract class AbstractX10Listener {

	/**
	 * Create a {@link codesketch.driver.usb.UsbDevice} starting from the
	 * received event.
	 * 
	 * @param event
	 *            the event sent from the USB bus.
	 * @return a new {@link codesketch.driver.usb.UsbDevice} instance
	 */
	protected Controller extractUsbController(UsbDevice device) {
		return Module.buildControllerFor(device);
	}

	protected Boolean isGeneratedByAKnownDevice(UsbDevice device) {
		UsbDeviceDescriptor descriptor = device.getUsbDeviceDescriptor();
		return Module.isValidDevice(descriptor.idVendor(), descriptor.idProduct());
	}
}
