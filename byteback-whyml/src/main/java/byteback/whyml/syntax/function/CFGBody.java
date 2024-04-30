package byteback.whyml.syntax.function;

import java.util.List;

public record CFGBody(List<WhyLocal> locals,
					  List<CFGBlock> blocks) {}