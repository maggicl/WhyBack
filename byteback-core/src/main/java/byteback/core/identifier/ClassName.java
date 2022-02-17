package byteback.core.identifier;

public class ClassName extends Name {

    public ClassName(final String name) {
        super(name);
    }

    @Override
    public boolean validate() {
        final String head = getHead().toString();

        return super.validate() && Character.isUpperCase(head.charAt(0));
    }

}
