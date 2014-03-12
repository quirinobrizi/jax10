package codesketch.x10;

/**
 * Defines the protocol functions.
 * 
 * @author quirino
 * 
 */
public enum Function {

    ALL_UNITS_OFF((byte) 0x00), ALL_LIGHTS_ON((byte) 0x01), ON((byte) 0x02), OFF((byte) 0x03), DIM((byte) 0x04), BRIGHT((byte) 0x05), ALL_LIGHTS_OFF(
            (byte) 0x06), EXTENDCODE((byte) 0x07), HAILREQ((byte) 0x08), HAILACK((byte) 0x09), PDIML((byte) 0x10), PDIMH((byte) 0x11), EXTENDDATA(
            (byte) 0x12), STATON((byte) 0x13), STATOFF((byte) 0x14), STATREQ((byte) 0x15);

    private byte nible;

    private Function(byte nible) {
        this.nible = nible;
    }

    public byte nibble() {
        return this.nible;
    }
}
