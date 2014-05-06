package codesketch.x10.bus;

import java.util.List;

import codesketch.x10.controller.X10Controller;

public interface ControllerProvider {

	<C extends X10Controller> List<C> provideAllAvailableControllers();

	<C extends X10Controller> C provideControllerBy(ControllerProvider.Definition definition);

	public enum Definition {

		CM15((short) 0x0BC7, (short) 0x0001, codesketch.x10.controller.impl.CM15.class);

		private final short vendorId;
		private final short productId;
		private final Class<? extends X10Controller> type;

		private Definition(short vendorId, short productId, Class<? extends X10Controller> type) {
			this.vendorId = vendorId;
			this.productId = productId;
			this.type = type;
		}

		public short getVendorId() {
			return vendorId;
		}

		public short getProductId() {
			return productId;
		}

		/**
		 * @return the type
		 */
		public Class<? extends X10Controller> getType() {
			return type;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return String.format("[Definition] - name: %s, vendorId: %d, productId: %s", name(), vendorId, productId);
		}
	}
}
