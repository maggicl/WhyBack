package byteback.core.identifier;

public class MemberName extends Name {

    public MemberName(String name) {
        super(name);
    }

    @Override
    public boolean validate() {
        return super.validate() && getParts().size() == 1 && Character.isLowerCase(toString().charAt(0));
    }

}
