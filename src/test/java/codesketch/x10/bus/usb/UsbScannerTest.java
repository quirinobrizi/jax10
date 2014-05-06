/**
 * 
 */
package codesketch.x10.bus.usb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import codesketch.x10.Address;
import codesketch.x10.actuator.Actuator;
import codesketch.x10.bus.ControllerProvider;
import codesketch.x10.bus.ControllerProvider.Definition;
import codesketch.x10.controller.X10Controller;

/**
 * @author quirino.brizi (quirino.brizi@gmail.com)
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class UsbScannerTest {

	@Mock
	private UsbServicesProvider usbServicesProvider;

	private final UsbControllerProvider testObj = new UsbControllerProvider();

	@Before
	public void setUp() {
		this.testObj.setUsbServiceProvider(usbServicesProvider);
	}

	@Ignore
	@Test
	public void test() throws Exception {

		UsbControllerProvider provider = new UsbControllerProvider();
		X10Controller x10Controller = provider.provideControllerBy(Definition.CM15);
		// X10Controller x10Controller = controllers.get(0);
		Address address = Address.forModule("A", "2");
		Actuator actuator = x10Controller.actuator(address);
		Boolean executed = actuator.off();
		System.out.println(executed);
	}

	@Test
	public void testProvideControllerFromDefinition() throws UsbException {
		Definition expectedControllerDefinition = ControllerProvider.Definition.CM15;
		// setup
		UsbServices usbServices = mock(UsbServices.class);
		UsbHub usbHub = mock(UsbHub.class);
		UsbDevice usbDevice = mock(UsbDevice.class);
		UsbDeviceDescriptor usbDeviceDescriptor = mock(UsbDeviceDescriptor.class);
		when(usbDeviceDescriptor.idVendor()).thenReturn(expectedControllerDefinition.getVendorId());
		when(usbDeviceDescriptor.idProduct()).thenReturn(expectedControllerDefinition.getProductId());
		when(usbDevice.getUsbDeviceDescriptor()).thenReturn(usbDeviceDescriptor);
		List<UsbDevice> attachedDevices = Arrays.asList(usbDevice);
		when(usbHub.getAttachedUsbDevices()).thenReturn(attachedDevices);
		when(usbServices.getRootUsbHub()).thenReturn(usbHub);
		when(this.usbServicesProvider.retrieveUsbHub()).thenReturn(usbHub);
		// act
		X10Controller actual = testObj.provideControllerBy(expectedControllerDefinition);
		// assert
		assertNotNull(actual);
		assertEquals(expectedControllerDefinition.getProductId(), actual.productId());
		assertEquals(expectedControllerDefinition.getVendorId(), actual.vendorId());
	}
}
