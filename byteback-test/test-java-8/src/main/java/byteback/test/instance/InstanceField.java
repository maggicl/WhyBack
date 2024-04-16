/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.instance.InstanceField -o %t.mlw
 */
package byteback.test.instance;

public class InstanceField {

	boolean booleanField;

	byte byteField;

	short shortField;

	int intField;

	long longField;

	float floatField;

	double doubleField;

	Object referenceField;

}
/**
 * RUN: %{verify} %t.mlw
 * CHECK-IGNORE: Boogie program verifier finished with 1 verified, 0 errors
 */
