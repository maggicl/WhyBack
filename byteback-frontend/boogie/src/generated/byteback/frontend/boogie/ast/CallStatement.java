/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:148
 * @astdecl CallStatement : Statement ::= Label:Identifier Callee:Identifier;
 * @production CallStatement : {@link Statement} ::= <span class="component">Callee:{@link Identifier}</span>;

 */
public abstract class CallStatement extends Statement implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public CallStatement() {
    super();
  }
  /**
   * Initializes the child array to the correct size.
   * Initializes List and Opt nta children.
   * @apilevel internal
   * @ast method
   * @declaredat ASTNode:10
   */
  public void init$Children() {
    children = new ASTNode[2];
  }
  /**
   * @declaredat ASTNode:13
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Label", "Callee"},
    type = {"Identifier", "Identifier"},
    kind = {"Child", "Child"}
  )
  public CallStatement(Identifier p0, Identifier p1) {
    setChild(p0, 0);
    setChild(p1, 1);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:23
   */
  protected int numChildren() {
    return 2;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:29
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:33
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:37
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:41
   */
  public CallStatement clone() throws CloneNotSupportedException {
    CallStatement node = (CallStatement) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   * @declaredat ASTNode:52
   */
  @Deprecated
  public abstract CallStatement fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:60
   */
  public abstract CallStatement treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:68
   */
  public abstract CallStatement treeCopy();
  /**
   * Replaces the Label child.
   * @param node The new node to replace the Label child.
   * @apilevel high-level
   */
  public void setLabel(Identifier node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Label child.
   * @return The current node used as the Label child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Label")
  public Identifier getLabel() {
    return (Identifier) getChild(0);
  }
  /**
   * Retrieves the Label child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Label child.
   * @apilevel low-level
   */
  public Identifier getLabelNoTransform() {
    return (Identifier) getChildNoTransform(0);
  }
  /**
   * Replaces the Callee child.
   * @param node The new node to replace the Callee child.
   * @apilevel high-level
   */
  public void setCallee(Identifier node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the Callee child.
   * @return The current node used as the Callee child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Callee")
  public Identifier getCallee() {
    return (Identifier) getChild(1);
  }
  /**
   * Retrieves the Callee child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Callee child.
   * @apilevel low-level
   */
  public Identifier getCalleeNoTransform() {
    return (Identifier) getChildNoTransform(1);
  }
  /** @apilevel internal */
  public ASTNode rewriteTo() {
    return super.rewriteTo();
  }
  /** @apilevel internal */
  public boolean canRewrite() {
    return false;
  }
}
