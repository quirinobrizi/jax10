package codesketch.x10.bus.usb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.x10.bus.Scanner;
import codesketch.x10.bus.usb.exception.UsbOperationException;
import codesketch.x10.controllers.X10Controller;
import codesketch.x10.controllers.impl.CM15;

public class UsbScanner implements Scanner {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsbScanner.class);

	private final UsbServices usbServices;

	/**
     * 
     */
	public UsbScanner() {
		try {
			this.usbServices = UsbHostManager.getUsbServices();
		} catch (UsbException e) {
			throw new UsbOperationException(e);
		}
	}

    @SuppressWarnings("unchecked")
    @Override
    public <C extends X10Controller> List<C> scan() {
		// Device device = new codesketch.x10.bus.usb.UsbDevice();
		List<codesketch.x10.bus.usb.UsbDevice> devices;
		try {
			devices = this.retrieveKnownDevices();
			CM15 controller = new CM15(devices.get(0));
			return (List<C>) Arrays.asList(controller);
		} catch (UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    @Override
    public <C extends X10Controller> C scan(short vendorId, short productId, Class<C> type) {
        // TODO Auto-generated method stub
        return null;
    }

	public UsbHub retrieveUsbHub() throws UsbException {
		return getUsbServices().getRootUsbHub();
	}

	protected UsbServices getUsbServices() {
		return this.usbServices;
	}

	private List<codesketch.x10.bus.usb.UsbDevice> retrieveKnownDevices() throws UsbException {
        List<codesketch.x10.bus.usb.UsbDevice> devices = new ArrayList<codesketch.x10.bus.usb.UsbDevice>();
        KnownDevice[] knownDevices = KnownDevice.values();
		UsbHub retrieveUsbHub = this.retrieveUsbHub();
        for (KnownDevice knownDevice : knownDevices) {
			LOGGER.info("inspecting USB bus for device {}", knownDevice);
			try {
				short vendorId = knownDevice.getVendorId();
				short productId = knownDevice.getProductId();
				UsbDevice device = this.findDevice(retrieveUsbHub, vendorId, productId);
				if(null != device) {
					LOGGER.debug("found connected device for {}", knownDevice);
					devices.add(new codesketch.x10.bus.usb.UsbDevice(device));
				}
			} catch (UsbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return devices;
    }

	private UsbDevice findDevice(UsbHub usbHub, short vendorId, short productId) throws UsbException {
		@SuppressWarnings("unchecked")
		List<UsbDevice> attachedUsbDevices = usbHub.getAttachedUsbDevices();
		for (UsbDevice usbDevice : attachedUsbDevices) {
			UsbDeviceDescriptor desc = usbDevice.getUsbDeviceDescriptor();
			if (desc.idVendor() == vendorId && desc.idProduct() == productId) {
				LOGGER.info("found device: {} : {}", vendorId, productId);
				return usbDevice;
			}
			if (usbDevice.isUsbHub()) {
				usbDevice = findDevice((UsbHub) usbDevice, vendorId, productId);
				if (usbDevice != null) {
					return usbDevice;
				}
			}
		}
		return null;
	}

    private enum KnownDevice {

        CM15((short) 0x0BC7, (short) 0x0001);

        private final short vendorId;
        private final short productId;

        private KnownDevice(short vendorId, short productId) {
            this.vendorId = vendorId;
            this.productId = productId;
        }

        public short getVendorId() {
            return vendorId;
        }

        public short getProductId() {
            return productId;
        }

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return String.format("[KnownDevice] - name: %s, vendorId: %d, productId: %s", name(), vendorId, productId);
		}
    }
}
