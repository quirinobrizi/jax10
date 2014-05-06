package codesketch.x10.bus.usb;

import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

public class UsbServicesProvider {

	public UsbHub retrieveUsbHub() throws UsbException {
		return getUsbServices().getRootUsbHub();
	}

	protected UsbServices getUsbServices() throws UsbException {
		return UsbHostManager.getUsbServices();
	}
}
