package byteback.core.context;

public interface Context {

    public void loadClass(String canonicalName);

    public void loadClassAndSupport(String canonicalName);

    public int getClassesCount();

}
