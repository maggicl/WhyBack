package byteback.core.identifier;

public class MethodName extends MemberName {

    public MethodName(final String name) {
        super(name);
    }

    @Override
    public boolean validate() {
        final String name = toString();

        return (super.validate() || name.equals("<init>") || name.equals("<clinit>"));
    }

}
