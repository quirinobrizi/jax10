package codesketch.x10.bus.usb;

import javax.usb.UsbConfiguration;
import javax.usb.UsbEndpoint;
import javax.usb.UsbInterface;
import javax.usb.UsbIrp;
import javax.usb.UsbPipe;

import codesketch.x10.bus.Device;

public class UsbDevice implements Device {

	private final javax.usb.UsbDevice device;
	private UsbConfiguration configuration;
	private UsbInterface iface;

	private UsbEndpoint read;
	private UsbEndpoint write;
	private UsbPipe readPipe;
	private UsbPipe writePipe;

	/**
	 * @param device
	 */
	public UsbDevice(javax.usb.UsbDevice device) {
		this.device = device;
	}

	@Override
    public void open() {
		this.configuration = this.device.getActiveUsbConfiguration();
	}

	@Override
    public void claim() {
    	if(null != this.configuration) {
			this.iface = (UsbInterface) this.configuration.getUsbInterfaces().get(0);
			try {
				this.iface.claim();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// List<UsbEndpoint> endpoints = iface.getUsbEndpoints();
			// for (UsbEndpoint endpoint : endpoints) {
			// System.out.println("Ep " + endpoint.getType() + " - " +
			// endpoint.getDirection());
			// if (endpoint.getType() == UsbConst.ENDPOINT_TYPE_BULK) {
			// if(endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_IN) {
			// read = endpoint;
			// } else {
			// write = endpoint;
			// }
			// }
			// }
			//
			// if (null != read && null != write) {
			// try {
			// this.iface.claim();
			// this.readPipe = this.read.getUsbPipe();
			// this.writePipe = this.write.getUsbPipe();
			//
			// this.readPipe.open();
			// this.writePipe.open();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
		}
    }

    @Override
    public void close() {
		// TODO Auto-generated method stub

    }

    @Override
    public byte[] read(byte readEndpoint, int lenght) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int write(byte writeEndpoint, byte[] sequence) {

		UsbEndpoint endpoint = this.iface.getUsbEndpoint(writeEndpoint);
		UsbPipe pipe = endpoint.getUsbPipe();
		try {
			if (!pipe.isOpen()) {
				pipe.open();
			}
			UsbIrp irp = pipe.createUsbIrp();
			irp.setData(sequence);
			irp.setLength(sequence.length);
			irp.setAcceptShortPacket(true);

			pipe.syncSubmit(irp);

			this.print("Received: ", irp.getData());
			return irp.getActualLength();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if (this.claimedInterface.containsUsbEndpoint(writeEndpoint)) {
		// byte bmRequestType = (byte) (UsbConst.REQUESTTYPE_DIRECTION_OUT |
		// UsbConst.REQUESTTYPE_DIRECTION_IN
		// | UsbConst.REQUESTTYPE_TYPE_STANDARD |
		// UsbConst.REQUESTTYPE_RECIPIENT_DEVICE);
		// byte bRequest = (byte) (UsbConst.REQUESTTYPE_DIRECTION_IN |
		// UsbConst.REQUESTTYPE_DIRECTION_OUT);
		// short wValue = writeEndpoint;
		// short wIndex = 0;
		// UsbControlIrp controlIrp =
		// this.device.createUsbControlIrp(bmRequestType, bRequest, wValue,
		// wIndex);
		// controlIrp.setData(sequence);
		// try {
		// this.device.syncSubmit(controlIrp);
		// byte[] received = controlIrp.getData();
		// this.print("Received: ", received);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// } else {
		//
		// }
		return 0;
    }

	private void print(String prefix, byte[] sequence) {
		StringBuffer buffer = new StringBuffer(prefix);
		buffer.append(": [");
		for (byte b : sequence) {
			buffer.append(String.format("0x%02X,", b));
		}
		buffer.append("]");
		System.out.println(buffer.toString());
	}
}
