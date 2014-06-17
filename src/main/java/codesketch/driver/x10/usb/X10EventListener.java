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

import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.driver.Controller;
import codesketch.driver.listener.Event;
import codesketch.driver.listener.EventListener;
import codesketch.driver.usb.listener.UsbEvent;

/**
 * Default implementation for of a device event listener.<br />
 * Forward event in a know fashion to an {@link EventListener}.
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public class X10EventListener extends AbstractX10Listener implements UsbDeviceListener {

	private static final Logger logger = LoggerFactory.getLogger(X10EventListener.class);

	private final EventListener eventListener;

	/**
	 * Create a new {@link X10EventListener}.
	 * 
	 * @param eventListener
	 *            the event listener event are going to be forwarded.
	 */
	public X10EventListener(EventListener eventListener) {
		this.eventListener = eventListener;
	}

	/* (non-Javadoc)
	 * @see javax.usb.event.UsbDeviceListener#usbDeviceDetached(javax.usb.event.UsbDeviceEvent)
	 */
	@Override
	public void usbDeviceDetached(UsbDeviceEvent event) {
		logger.debug("received detach event from device {}", event);
		if (isGeneratedByAKnownDevice(event.getUsbDevice())) {
			Controller controller = extractUsbController(event.getUsbDevice());
			this.eventListener.onDisconnect(controller);
		}
	}

	/* (non-Javadoc)
	 * @see javax.usb.event.UsbDeviceListener#errorEventOccurred(javax.usb.event.UsbDeviceErrorEvent)
	 */
	@Override
	public void errorEventOccurred(UsbDeviceErrorEvent usbEvent) {
		logger.debug("received error event from device {}", usbEvent);
		if (isGeneratedByAKnownDevice(usbEvent.getUsbDevice())) {
			Controller controller = extractUsbController(usbEvent.getUsbDevice());
			Event event = UsbEvent.failure(usbEvent.getUsbException());
			this.eventListener.onEvent(controller, event);
		}
	}

	/* (non-Javadoc)
	 * @see javax.usb.event.UsbDeviceListener#dataEventOccurred(javax.usb.event.UsbDeviceDataEvent)
	 */
	@Override
	public void dataEventOccurred(UsbDeviceDataEvent usbEvent) {
		logger.debug("received data event from device {}", usbEvent);
		if (isGeneratedByAKnownDevice(usbEvent.getUsbDevice())) {
			Controller controller = extractUsbController(usbEvent.getUsbDevice());
			Event event = UsbEvent.success(usbEvent.getData());
			this.eventListener.onEvent(controller, event);
		}
	}

}
