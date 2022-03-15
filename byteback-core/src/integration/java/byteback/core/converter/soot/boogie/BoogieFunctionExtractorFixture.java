package byteback.core.converter.soot.boogie;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import byteback.core.ResourcesUtil;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.core.representation.unit.soot.SootMethodUnitFixture;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.Program;

public class BoogieFunctionExtractorFixture extends SootMethodUnitFixture {

    public Map<SootMethodUnit, FunctionDeclaration> getFunctionRegressionSet(final String jarName) {
        try {
            final Map<SootMethodUnit, FunctionDeclaration> map = new HashMap<>();
            final List<Path> regressionPaths = ResourcesUtil.getRegressionPaths(jarName, "boogie/function");

            for (Path path : regressionPaths) {
                if (!path.toString().endsWith(".bpl")) {
                    continue;
                }

                final String fileName = path.getFileName().toString();
                final String boogieName = fileName.substring(0, fileName.lastIndexOf("."));
                final String[] totalParts = boogieName.split("#");
                final String name = totalParts[0];
                final String[] nameParts = name.split("\\.");
                final String className = String.join(".", Arrays.copyOfRange(nameParts, 0, nameParts.length - 1));
                final String methodName = nameParts[nameParts.length - 1];
                final String[] argumentParts = Arrays.copyOfRange(totalParts, 1, totalParts.length);
                final String methodIdentifier = methodName + "(" + String.join(",", argumentParts) + ")";
                final SootMethodUnit methodUnit = getMethodUnit(jarName, className, methodIdentifier);
                final Program program = ResourcesUtil.parseBoogieProgram(path);
                final FunctionDeclaration functionDeclaration = (FunctionDeclaration) program.getDeclaration(0);
                map.put(methodUnit, functionDeclaration);
            }

            return map;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
