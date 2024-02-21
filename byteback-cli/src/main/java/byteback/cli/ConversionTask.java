package byteback.cli;

import byteback.frontend.boogie.ast.Printable;

public interface ConversionTask {
    Printable run();
}
