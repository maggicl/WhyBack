package byteback.core.type.soot;

import byteback.core.type.TypeVisitor;
import soot.TypeSwitch;

/**
 * Base class for a {@link SootType} visitor.
 */
public abstract class SootTypeVisitor extends TypeSwitch implements TypeVisitor {
}
