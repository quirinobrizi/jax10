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
package codesketch.driver.x10.usb;

import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import codesketch.driver.Controller;
import codesketch.driver.listener.ConnectionListener;

/**
 * Wraps the custom {@link ConnectionListener} in order to bubble-up events when
 * fired on the USB bus.
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public class X10ServicesListener extends AbstractX10Listener implements UsbServicesListener {

	private final ConnectionListener connectionListener;

	/**
	 * Create a new {@link X10ServicesListener} setting the requested
	 * {@link ConnectionListener}.
	 * 
	 * @param connectionListener
	 *            the {@link ConnectionListener} implementation.
	 */
	public X10ServicesListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
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
		if (isGeneratedByAKnownDevice(event.getUsbDevice())) {
			Controller controller = extractUsbController(event.getUsbDevice());
			this.connectionListener.onConnect(controller);
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
		if (isGeneratedByAKnownDevice(event.getUsbDevice())) {
			Controller controller = extractUsbController(event.getUsbDevice());
			this.connectionListener.onDisconnect(controller);
		}
	}
}
