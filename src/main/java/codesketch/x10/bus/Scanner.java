package codesketch.x10.bus;

import java.util.List;

import codesketch.x10.controller.X10Controller;

public interface Scanner {

    <C extends X10Controller> List<C> scan();

    <C extends X10Controller> C scan(short vendorId, short productId, Class<C> type);
}
