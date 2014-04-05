package codesketch.x10.bus.usb;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.x10.bus.ControllerProvider;
import codesketch.x10.controller.X10Controller;
import codesketch.x10.controller.impl.CM15;

public class UsbControllerProvider implements ControllerProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsbControllerProvider.class);

    private UsbServicesProvider usbServiceProvider = new UsbServicesProvider();

    @SuppressWarnings("unchecked")
    @Override
    public <C extends X10Controller> List<C> provideAllAvailableControllers() {
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * codesketch.x10.bus.ControllerProvider#scanFor(codesketch.x10.bus.usb.
     * UsbControllerProvider. Definition)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends X10Controller> C provideControllerBy(Definition definition) {
        return (C) this.scan(definition.getVendorId(), definition.getProductId(), definition.getType());
    }

    private <C extends X10Controller> C scan(short vendorId, short productId, Class<C> type) {
        try {
            UsbHub retrieveUsbHub = this.usbServiceProvider.retrieveUsbHub();
            UsbDevice usbDevice = this.findDevice(retrieveUsbHub, vendorId, productId);
            codesketch.x10.bus.usb.UsbDevice device = new codesketch.x10.bus.usb.UsbDevice(usbDevice);
            Constructor<C> constructor = type.getConstructor(codesketch.x10.bus.Device.class);
            return constructor.newInstance(device);
        } catch (Exception e) {
            LOGGER.error("unable to find requested device", e);
        }
        return null;
    }

    private List<codesketch.x10.bus.usb.UsbDevice> retrieveKnownDevices() throws UsbException {
        List<codesketch.x10.bus.usb.UsbDevice> devices = new ArrayList<codesketch.x10.bus.usb.UsbDevice>();
        Definition[] knownDevices = Definition.values();
        UsbHub retrieveUsbHub = this.usbServiceProvider.retrieveUsbHub();
        for (Definition knownDevice : knownDevices) {
            LOGGER.info("inspecting USB bus for device {}", knownDevice);
            try {
                short vendorId = knownDevice.getVendorId();
                short productId = knownDevice.getProductId();
                UsbDevice device = this.findDevice(retrieveUsbHub, vendorId, productId);
                if (null != device) {
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

    // Testing purpose
    void setUsbServiceProvider(UsbServicesProvider usbServiceProvider) {
        this.usbServiceProvider = usbServiceProvider;
    }
}
