package codesketch.x10.controllers;

import codesketch.x10.bus.Device;

public abstract class AbstractUsbX10Controller extends AbstractX10Controller {

    public AbstractUsbX10Controller(Device device) {
        super(device);
    }

    @Override
    public void open() {
        Device device = getDevice();
        device.open();
        device.claim();
    }

    @Override
    public void close() {
        getDevice().close();
    }

    @Override
    public byte[] read(int lenght) {
        return getDevice().read(this.readEndpoint(), lenght);
    }

    @Override
    public void write(byte[] sequence) {
        this.print("Write: ", sequence);
        int written = getDevice().write(this.writeEndpoint(), sequence);
        if (written != sequence.length) {
            // TODO raise exception
        }
    }

    protected abstract byte readEndpoint();

    protected abstract byte writeEndpoint();

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
