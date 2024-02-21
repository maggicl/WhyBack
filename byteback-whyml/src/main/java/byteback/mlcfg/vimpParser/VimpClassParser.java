package byteback.mlcfg.vimpParser;

import byteback.mlcfg.syntax.RecordDeclaration;
import byteback.mlcfg.syntax.RecordFieldDeclaration;
import soot.SootClass;

import java.util.List;

public class VimpClassParser {
    public RecordDeclaration parseClassDeclaration(SootClass clazz) {
        final String className = clazz.getName();
        final List<RecordFieldDeclaration> fields = clazz.getFields().stream()
                .map(f -> new RecordFieldDeclaration(f.getName(), f.getType().toQuotedString()))
                .toList();
        return new RecordDeclaration(className, fields);
    }
}
