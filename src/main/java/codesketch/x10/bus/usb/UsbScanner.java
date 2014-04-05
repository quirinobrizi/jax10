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
import codesketch.x10.controller.X10Controller;
import codesketch.x10.controller.impl.CM15;

public class UsbScanner implements Scanner {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsbScanner.class);

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
		try {
			UsbHub retrieveUsbHub = this.retrieveUsbHub();
			UsbDevice usbDevice = this.findDevice(retrieveUsbHub, vendorId, productId);
			codesketch.x10.bus.usb.UsbDevice device = new codesketch.x10.bus.usb.UsbDevice(usbDevice);

			// Constructor<C> constructor =
			// type.getConstructor(codesketch.x10.bus.usb.UsbDevice.class);
			// return constructor.newInstance(device);
			return (C) new CM15(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * codesketch.x10.bus.Scanner#scanFor(codesketch.x10.bus.usb.UsbScanner.
	 * KnownDevice)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends X10Controller> C scanFor(KnownDevice knownDevice) {
		return (C) this.scan(knownDevice.getVendorId(), knownDevice.getProductId(), knownDevice.getType());
	}

	public UsbHub retrieveUsbHub() throws UsbException {
		return getUsbServices().getRootUsbHub();
	}

	protected UsbServices getUsbServices() throws UsbException {
		return UsbHostManager.getUsbServices();
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
				LOGGER.warn("exception looking for device {} message {}", knownDevice, e.getMessage());
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

	public enum KnownDevice {

		CM15((short) 0x0BC7, (short) 0x0001, codesketch.x10.controller.impl.CM15.class);

        private final short vendorId;
        private final short productId;
		private final Class<? extends X10Controller> type;

		private KnownDevice(short vendorId, short productId, Class<? extends X10Controller> type) {
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
		 * @return the type
		 */
		public Class<? extends X10Controller> getType() {
			return type;
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
