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

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.driver.Controller;
import codesketch.driver.ControllerProvider;
import codesketch.driver.listener.ConnectionListener;
import codesketch.driver.usb.UsbServicesProvider;
import codesketch.driver.x10.Module;
import codesketch.driver.x10.usb.exception.UsbOperationException;

/**
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public class X10UsbControllerProvider implements ControllerProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(X10UsbControllerProvider.class);

	private final UsbServicesProvider usbServiceProvider;

	/**
	 * Create a new {@link X10UsbControllerProvider} instance whit default
	 * {@link UsbServicesProvider}
	 */
	public X10UsbControllerProvider() {
		this.usbServiceProvider = new UsbServicesProvider();
	}

	/**
	 * Create a new {@link X10UsbControllerProvider} instance whit provided
	 * {@link UsbServicesProvider}
	 * 
	 * @param usbServiceProvider
	 *            a custom {@link UsbServicesProvider} implementation.
	 */
	public X10UsbControllerProvider(UsbServicesProvider usbServiceProvider) {
		this.usbServiceProvider = usbServiceProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * codesketch.x10.bus.ControllerProvider#provideAllAvailableControllers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Controller> List<C> provideAllAvailableControllers() {
		try {
			return (List<C>) this.retrieveKnownDevices();
		} catch (UsbException e) {
			throw new UsbOperationException("unable to scan USB bus and provide controllers", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * codesketch.x10.bus.ControllerProvider#scanFor(codesketch.x10.bus.usb.
	 * UsbControllerProvider. Definition)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Controller> C provideControllerBy(Module definition) {
		return (C) this.scan(definition, definition.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * codesketch.x10.bus.ControllerProvider#registerEventListener(codesketch
	 * .x10.bus.BusEventListener)
	 */
	@Override
	public void registerEventListener(ConnectionListener busEventListener) {
		try {
			UsbServices usbServices = this.usbServiceProvider.getUsbServices();
			X10ServicesListener defaultUsbServicesListener = new X10ServicesListener(busEventListener);
			usbServices.addUsbServicesListener(defaultUsbServicesListener);
		} catch (UsbException e) {
			String message = String.format("unable regiater requested  listener: %s", busEventListener);
			throw new UsbOperationException(message, e);
		}
	}

	@SuppressWarnings("unchecked")
	private <C extends Controller> C scan(Module definition, Class<C> type) {
		try {
			UsbHub retrieveUsbHub = this.usbServiceProvider.retrieveUsbHub();
			UsbDevice usbDevice = this.findDevice(retrieveUsbHub, definition);
			return (C) definition.buildController(usbDevice);
		} catch (Exception e) {
			throw new UsbOperationException("unable to scan USB bus and provide controllers", e);
		}
	}

	private List<Controller> retrieveKnownDevices() throws UsbException {
		List<Controller> controllers = new ArrayList<Controller>();
		Module[] knownDevices = Module.values();
		UsbHub retrieveUsbHub = this.usbServiceProvider.retrieveUsbHub();
		for (Module knownDevice : knownDevices) {
			LOGGER.info("inspecting USB bus for device {}", knownDevice);
			try {
				UsbDevice device = this.findDevice(retrieveUsbHub, knownDevice);
				if (null != device) {
					LOGGER.debug("found connected device for {}", knownDevice);
					controllers.add(Module.buildControllerFor(device));
				}
			} catch (Exception e) {
				LOGGER.warn("exception looking for device {} message {}", knownDevice, e.getMessage());
			}
		}
		return controllers;
	}

	@SuppressWarnings("unchecked")
	private UsbDevice findDevice(UsbHub usbHub, Module definition) throws UsbException {
		List<UsbDevice> attachedUsbDevices = usbHub.getAttachedUsbDevices();
		for (UsbDevice usbDevice : attachedUsbDevices) {
			UsbDeviceDescriptor desc = usbDevice.getUsbDeviceDescriptor();
			if (definition.isBasedOn(desc.idVendor(), desc.idProduct())) {
				LOGGER.info("found device: {}", definition);
				return usbDevice;
			}
			if (usbDevice.isUsbHub()) {
				usbDevice = findDevice((UsbHub) usbDevice, definition);
				if (usbDevice != null) {
					return usbDevice;
				}
			}
		}
		return null;
	}
}
