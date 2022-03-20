package byteback.dummy;

import byteback.annotations.Contract.Pure;

public class VirtualMethods {

    @Pure
    VirtualMethods getThis() {
        return this;
    }

}
