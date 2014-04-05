jax10
=====

A tentative to support CM15Pro X10 controller using Java, eventually will became a proper project.

I'm following what's reported at:  
 * http://www.linuxha.com/USB/cm15a.html  
 * http://www.eclipsehomeauto.com/cm15a_on_linux/cm15a.pdf  
 * http://home.comcast.net/~ncherry/common/cm15d/cm15d.html  

Integrating (at least trying to...) everithing whit personal experiments.

Any suggestion is welcome!

Output from lsusb -v

```bash

Bus 003 Device 106: ID 0bc7:0001 X10 Wireless Technology, Inc. ActiveHome (ACPI-compliant)
Device Descriptor:
  bLength                18
  bDescriptorType         1
  bcdUSB               1.10
  bDeviceClass            0 (Defined at Interface level)
  bDeviceSubClass         0 
  bDeviceProtocol         0 
  bMaxPacketSize0         8
  idVendor           0x0bc7 X10 Wireless Technology, Inc.
  idProduct          0x0001 ActiveHome (ACPI-compliant)
  bcdDevice            1.00
  iManufacturer           1 X10 Wireless Technology Inc
  iProduct                2 USB ActiveHome Interface
  iSerial                 0 
  bNumConfigurations      1
  Configuration Descriptor:
    bLength                 9
    bDescriptorType         2
    wTotalLength           32
    bNumInterfaces          1
    bConfigurationValue     1
    iConfiguration          0 
    bmAttributes         0xc0
      Self Powered
    MaxPower                2mA
    Interface Descriptor:
      bLength                 9
      bDescriptorType         4
      bInterfaceNumber        0
      bAlternateSetting       0
      bNumEndpoints           2
      bInterfaceClass         0 (Defined at Interface level)
      bInterfaceSubClass      0 
      bInterfaceProtocol      0 
      iInterface              0 
      Endpoint Descriptor:
        bLength                 7
        bDescriptorType         5
        bEndpointAddress     0x81  EP 1 IN
        bmAttributes            3
          Transfer Type            Interrupt
          Synch Type               None
          Usage Type               Data
        wMaxPacketSize     0x0008  1x 8 bytes
        bInterval              10
      Endpoint Descriptor:
        bLength                 7
        bDescriptorType         5
        bEndpointAddress     0x02  EP 2 OUT
        bmAttributes            3
          Transfer Type            Interrupt
          Synch Type               None
          Usage Type               Data
        wMaxPacketSize     0x0008  1x 8 bytes
        bInterval              10
Device Status:     0x0001
  Self Powered
```

 * This is now working for turning on and off appliances when no house/unit selection payload is sent
 * This is now working on RaspberryPi

Example:

```
UsbControllerProvider provider = new UsbControllerProvider();
X10Controller controller = provider.provideControllerBy(ControllerProvider.Definition.CM15);
// List<X10Controller> controllers = provider.provideAllAvailableControllers();
// X10Controller x10Controller = controllers.get(0);
Address address = Address.forModule("A", "1");
Actuator actuator = x10Controller.actuator(address);
Boolean executed = actuator.on();
```
