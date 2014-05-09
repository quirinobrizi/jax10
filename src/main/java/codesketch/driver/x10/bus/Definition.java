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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;

import codesketch.driver.Controller;
import codesketch.driver.x10.bus.usb.exception.UsbOperationException;
import codesketch.driver.x10.controller.X10Controller;

/**
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public enum Definition {

	CM15((short) 0x0BC7, (short) 0x0001, codesketch.driver.x10.controller.impl.CM15.class);

	private final short vendorId;
	private final short productId;
	private final Class<? extends X10Controller> type;

	private Definition(short vendorId, short productId, Class<? extends X10Controller> type) {
		this.vendorId = vendorId;
		this.productId = productId;
		this.type = type;
	}

	public short getVendorId() {
		return vendorId;
	}

	public short getProductId() {
		return productId;
	}

	/**
	 * Verifies if this definition is for the provided vendor and product
	 * identifiers.
	 * 
	 * @param vendorId
	 *            the vendor identifier
	 * @param productId
	 *            the product identifier
	 * @return true if provided vendor and product identifiers matches whit the
	 *         defined vendor and product identifiers.
	 */
	public boolean isBasedOn(short vendorId, short productId) {
		return this.vendorId == vendorId && this.productId == productId;
	}

	/**
	 * Build a controller for the provided {@link UsbDevice}.
	 * 
	 * @param usbDevice
	 *            the {@link UsbDevice} implementation.
	 * @return a new {@link X10Controller}
	 */
	@SuppressWarnings("unchecked")
	public <C extends Controller> C buildController(UsbDevice usbDevice) {
		try {
			return (C) buildControllerForDevice(getType(), usbDevice);
		} catch (Exception e) {
			throw new UsbOperationException(e);
		}
	}

	/**
	 * @return the type
	 */
	public Class<? extends Controller> getType() {
		return type;
	}

	/**
	 * Uses reflection in order to create a controller for the provided device.
	 * 
	 * @param type
	 *            the controller type
	 * @param usbDevice
	 *            the {@link UsbDevice} to wrap
	 * @return a new {@link X10Controller} instance.
	 * 
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private <C extends Controller> C buildControllerForDevice(Class<C> type, UsbDevice usbDevice) throws NoSuchMethodException,
	        InstantiationException, IllegalAccessException, InvocationTargetException {
		codesketch.driver.x10.bus.usb.UsbDevice device = new codesketch.driver.x10.bus.usb.UsbDevice(usbDevice);
		Constructor<C> constructor = type.getConstructor(codesketch.driver.x10.bus.Device.class);
		return constructor.newInstance(device);
	}

	/**
	 * Verifies whether there is any definition for the provided vendor and
	 * product identifiers.
	 * 
	 * @param vendorId
	 *            the vendor identifier
	 * @param productId
	 *            the product identifiers
	 * @return true if provided vendor and product identifiers matches any valid
	 *         definition.
	 */
	public static boolean isValidDevice(short vendorId, short productId) {
		Definition[] definitions = values();
		for (Definition definition : definitions) {
			return definition.isBasedOn(vendorId, productId);
		}
		return false;
	}

	public static <C extends Controller> C buildControllerFor(UsbDevice usbDevice) {
		UsbDeviceDescriptor usbDeviceDescriptor = usbDevice.getUsbDeviceDescriptor();
		Definition[] definitions = values();
		for (Definition definition : definitions) {
			if (definition.isBasedOn(usbDeviceDescriptor.idVendor(), usbDeviceDescriptor.idProduct())) {
				return definition.buildController(usbDevice);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return String.format("[Definition] - name: %s, vendorId: %d, productId: %s", name(), vendorId, productId);
	}
}