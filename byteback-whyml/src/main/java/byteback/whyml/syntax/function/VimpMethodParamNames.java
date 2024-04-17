package byteback.whyml.syntax.function;

import java.util.List;
import java.util.Optional;

public record VimpMethodParamNames(Optional<String> thisName, List<String> parameterNames) {
}
