package byteback.core.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClassNameTest {

    @Test
    public void StringConstructor_OnValidName_ReturnsValidClassName() {
        final ClassName className = new ClassName("soot.SootClass");
        assertTrue(className.isPrefixedBy("soot", "SootClass"));
        assertEquals(className.toString(), "soot.SootClass");
    }

    @Test
    public void ToString_OnSimpleQualifiedName_ReturnsCorrectPackageName() {
        final ClassName className = new ClassName("simple", "Name");
        assertEquals(className.toString(), "simple.Name");
    }

    @Test
    public void Validate_OnInvalidQualifiedName_ReturnsFalse() {
        ClassName className;
        className = new ClassName("Simple", "name");
        assertFalse(className.validate());
        className = new ClassName("", "Name");
        assertFalse(className.validate());
        className = new ClassName("666", "Name");
        assertFalse(className.validate());
        className = new ClassName("42simple", "Name");
        assertFalse(className.validate());
        className = new ClassName("simple*", "Name");
        assertFalse(className.validate());
        className = new ClassName("simple.", "Name");
        assertFalse(className.validate());
    }

    @Test
    public void Validate_OnValidQualifiedName_ReturnsTrue() {
        ClassName className;
        className = new ClassName("simple", "Name");
        assertTrue(className.validate());
        className = new ClassName("more", "complex", "Name");
        assertTrue(className.validate());
        className = new ClassName("more", "Complex", "Name");
        assertTrue(className.validate());
        className = new ClassName("more", "complex42", "Name");
        assertTrue(className.validate());
    }

    @Test
    public void IsPrefixedBy_OnPrefixPattern_ReturnsTrue() {
        ClassName className;
        className = new ClassName("simple", "prefix");
        assertTrue(className.isPrefixedBy("simple", "prefix"));
    }

}
