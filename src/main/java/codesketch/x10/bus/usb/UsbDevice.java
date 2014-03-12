package codesketch.x10.bus.usb;

import javax.usb.UsbConfiguration;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbIrp;
import javax.usb.UsbPipe;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.x10.Utils;
import codesketch.x10.bus.Device;
import codesketch.x10.bus.usb.exception.UsbOperationException;

public class UsbDevice implements Device {

	private final static Logger LOGGER = LoggerFactory.getLogger(UsbDevice.class);

	private final javax.usb.UsbDevice device;
	private UsbConfiguration configuration;
	private UsbInterface iface;

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
		this.iface = (UsbInterface) this.configuration.getUsbInterfaces().get(0);
		try {
			this.iface.claim();
		} catch (Exception e) {
			throw new UsbOperationException(e);
		}
	}

    @Override
    public void close() {
		try {
			this.iface.release();
		} catch (Exception e) {
			throw new UsbOperationException(e);
		}
    }

    @Override
    public byte[] read(byte readEndpoint, int lenght) {
    	UsbPipe pipe = null;
    	try {
			pipe = retrieveAndOpenPipe(readEndpoint);
			pipe.addUsbPipeListener(new UsbPipeListener() {

				@Override
				public void errorEventOccurred(UsbPipeErrorEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void dataEventOccurred(UsbPipeDataEvent event) {
					System.out.println(event);
				}
			});
    	} catch (Exception e) {
			throw new UsbOperationException(e);
    	}  finally {
			this.silentlyReleasePipe(pipe);
		}
    	return null;
    }

    @Override
    public int write(byte writeEndpoint, byte[] sequence) {

		UsbPipe pipe = null;
		try {
			pipe = retrieveAndOpenPipe(writeEndpoint);
			UsbIrp irp = buildUsbIrp(sequence, pipe);

			pipe.syncSubmit(irp);

			Utils.prettyPrint("Received: ", irp.getData());
			return irp.getActualLength();
		} catch (Exception e) {
			throw new UsbOperationException(e);
		} finally {
			this.silentlyReleasePipe(pipe);
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
    }

	private UsbPipe retrieveAndOpenPipe(byte writeEndpoint) throws UsbException {
		UsbEndpoint endpoint = this.iface.getUsbEndpoint(writeEndpoint);
		UsbPipe pipe = endpoint.getUsbPipe();
		if (!pipe.isOpen()) {
			pipe.open();
		}
		return pipe;
	}

	/**
	 * @param pipe
	 */
	private void silentlyReleasePipe(UsbPipe pipe) {
		try {
			if (null != pipe && pipe.isOpen()) {
				pipe.abortAllSubmissions();
				pipe.close();
			}
		} catch (Exception e) {
			LOGGER.warn("unable closing pipe {}", e.getMessage());
		}
	}

	private UsbIrp buildUsbIrp(byte[] sequence, UsbPipe pipe) {
	    UsbIrp irp = pipe.createUsbIrp();
	    irp.setData(sequence);
	    irp.setLength(sequence.length);
	    irp.setAcceptShortPacket(true);
	    return irp;
    }

}
