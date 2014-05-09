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

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import codesketch.driver.Controller;
import codesketch.driver.x10.bus.BusEventListener;
import codesketch.driver.x10.bus.Definition;

/**
 * Wraps the custom {@link BusEventListener} in order to bubble-up events when
 * fired on the USB bus.
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public class DefaultUsbServicesListener implements UsbServicesListener {

	private final BusEventListener busEventListener;

	/**
	 * Create a new {@link DefaultUsbServicesListener} setting the requested
	 * {@link BusEventListener}.
	 * 
	 * @param busEventListener
	 *            the {@link BusEventListener} implementation.
	 */
	public DefaultUsbServicesListener(BusEventListener busEventListener) {
		this.busEventListener = busEventListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.usb.event.UsbServicesListener#usbDeviceAttached(javax.usb.event
	 * .UsbServicesEvent)
	 */
	@Override
	public void usbDeviceAttached(UsbServicesEvent event) {
		if (isGeneratedByAKnownDevice(event)) {
			Controller controller = extractUsbController(event);
			this.busEventListener.onConnect(controller);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.usb.event.UsbServicesListener#usbDeviceDetached(javax.usb.event
	 * .UsbServicesEvent)
	 */
	@Override
	public void usbDeviceDetached(UsbServicesEvent event) {
		if (isGeneratedByAKnownDevice(event)) {
			Controller controller = extractUsbController(event);
			this.busEventListener.onDisconnect(controller);
		}
	}

	/**
	 * Create a {@link codesketch.driver.x10.bus.usb.UsbDevice} starting from
	 * the received event.
	 * 
	 * @param event
	 *            the event sent from the USB bus.
	 * @return a new {@link codesketch.driver.x10.bus.usb.UsbDevice} instance
	 */
	private Controller extractUsbController(UsbServicesEvent event) {
		UsbDevice usbDevice = event.getUsbDevice();
		return Definition.buildControllerFor(usbDevice);
	}

	private Boolean isGeneratedByAKnownDevice(UsbServicesEvent event) {
		UsbDevice usbDevice = event.getUsbDevice();
		UsbDeviceDescriptor usbDeviceDescriptor = usbDevice.getUsbDeviceDescriptor();
		return Definition.isValidDevice(usbDeviceDescriptor.idVendor(), usbDeviceDescriptor.idProduct());
	}
}
