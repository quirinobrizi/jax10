package codesketch.x10.bus.usb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codesketch.x10.bus.Device;
import codesketch.x10.bus.Scanner;
import codesketch.x10.controllers.X10Controller;
import codesketch.x10.controllers.impl.CM15;

public class UsbScanner implements Scanner {

    @SuppressWarnings("unchecked")
    @Override
    public <C extends X10Controller> List<C> scan() {
        Device device = new UsbDevice();
        CM15 controller = new CM15(device);
        return (List<C>) Arrays.asList(controller);
    }

    @Override
    public <C extends X10Controller> C scan(short vendorId, short productId, Class<C> type) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<Object> retrieveKnownDevices() {
        List<Object> devices = new ArrayList<Object>();
        KnownDevice[] knownDevices = KnownDevice.values();
        for (KnownDevice knownDevice : knownDevices) {
            //
        }
        return devices;
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
    }
}
