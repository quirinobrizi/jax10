package codesketch.driver.x10.bus;

public interface Device {

	void open();

	void claim();

	void close();

	byte[] read(byte readEndpoint, int lenght);

	int write(byte writeEndpoint, byte[] sequence);
}
