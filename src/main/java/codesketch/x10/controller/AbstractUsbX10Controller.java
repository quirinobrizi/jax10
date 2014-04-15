package codesketch.x10.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.x10.Utils;
import codesketch.x10.bus.Device;

public abstract class AbstractUsbX10Controller extends AbstractX10Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUsbX10Controller.class);

    public AbstractUsbX10Controller(Device device) {
        super(device);
    }

    @Override
    public void open() {
        Device device = getDevice();
        if (null != device) {
            device.open();
            device.claim();
        }
    }

    @Override
    public void close() {
        Device device = getDevice();
        if (null != device) {
            device.close();
        }
    }

    @Override
    public byte[] read(int lenght) {
		this.open();
		Device device = getDevice();
        if (null != device) {
            byte[] response = device.read(this.readEndpoint(), lenght);
            this.close();
            return response;
        }
        return new byte[] {};
    }

    @Override
    public void write(byte[] sequence) {
		this.open();
		Utils.prettyPrint("Writing: ", sequence);
        Device device = getDevice();
        if (null != device) {
            int written = device.write(this.writeEndpoint(), sequence);
            if (written != sequence.length) {
                LOGGER.error("written {} byte of {}", written, sequence.length);
            }
            this.close();
        }
    }

    protected abstract byte readEndpoint();

    protected abstract byte writeEndpoint();
}
