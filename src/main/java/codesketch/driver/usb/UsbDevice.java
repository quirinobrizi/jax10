package codesketch.driver.usb;

import codesketch.driver.Device;
import codesketch.driver.listener.EventListener;
import codesketch.driver.x10.Utils;
import codesketch.driver.x10.usb.X10EventListener;
import codesketch.driver.x10.usb.exception.UsbOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.usb.*;
import javax.usb.event.UsbDeviceListener;

public class UsbDevice implements Device {

	private final static Logger LOGGER = LoggerFactory.getLogger(UsbDevice.class);

	private final javax.usb.UsbDevice device;
	private UsbConfiguration configuration;
	private UsbInterface usbInterface;

	/**
	 * @param device
	 */
	public UsbDevice(javax.usb.UsbDevice device) {
		this.device = device;
	}

	@Override
	public void open() {
		if(null == this.configuration) {
			this.configuration = this.device.getActiveUsbConfiguration();
		}
	}

	@Override
	public void claim() {
		if(null == this.usbInterface) {
			this.usbInterface = (UsbInterface) this.configuration.getUsbInterfaces().get(0);
			try {
				if (!this.usbInterface.isClaimed()) {
					this.usbInterface.claim();
				}
			} catch (Exception e) {
				throw new UsbOperationException(e);
			}
		}
	}

	@Override
	public void close() {
		try {
			this.usbInterface.release();
			this.usbInterface = null;
		} catch (Exception e) {
			throw new UsbOperationException(e);
		}
	}

	@Override
	public byte[] read(byte readEndpoint, int length) {
		UsbPipe pipe = null;
		try {
			pipe = retrieveAndOpenPipe(readEndpoint);
			UsbIrp irp = buildUsbIrp(new byte[length], pipe);
			pipe.syncSubmit(irp);
			return irp.getData();
		} catch (Exception e) {
			throw new UsbOperationException(e);
		} finally {
			this.silentlyReleasePipe(pipe);
		}
	}

	@Override
	public int write(byte writeEndpoint, byte[] sequence) {
		UsbPipe pipe = null;
		try {
			pipe = retrieveAndOpenPipe(writeEndpoint);
			UsbIrp irp = buildUsbIrp(sequence, pipe);
			pipe.syncSubmit(irp);
			LOGGER.debug("received on IRP: {}", Utils.formatHexToString(irp.getData()));
			return irp.getActualLength();
		} catch (Exception e) {
			throw new UsbOperationException(e);
		} finally {
			this.silentlyReleasePipe(pipe);
		}
	}

	private UsbPipe retrieveAndOpenPipe(byte endpoint) throws UsbException {
		UsbEndpoint usbEndpoint = this.usbInterface.getUsbEndpoint(endpoint);
		UsbPipe pipe = usbEndpoint.getUsbPipe();
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

	@Override
	public void attachListener(EventListener eventListener) {
		UsbDeviceListener listener = new X10EventListener(eventListener);
		this.device.addUsbDeviceListener(listener);
	}

}
