/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:6
 * @astdecl Attribute : ASTNode ::= <Label:String> [Argument:Expression];
 * @production Attribute : {@link ASTNode} ::= <span class="component">&lt;Label:String&gt;</span> <span class="component">[Argument:{@link Expression}]</span>;

 */
public class Attribute extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public Attribute() {
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
    children = new ASTNode[1];
    setChild(new Opt(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Label", "Argument"},
    type = {"String", "Opt<Expression>"},
    kind = {"Token", "Opt"}
  )
  public Attribute(String p0, Opt<Expression> p1) {
    setLabel(p0);
    setChild(p1, 0);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:24
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:30
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:34
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:38
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:42
   */
  public Attribute clone() throws CloneNotSupportedException {
    Attribute node = (Attribute) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:47
   */
  public Attribute copy() {
    try {
      Attribute node = (Attribute) clone();
      node.parent = null;
      if (children != null) {
        node.children = (ASTNode[]) children.clone();
      }
      return node;
    } catch (CloneNotSupportedException e) {
      throw new Error("Error: clone not supported for " + getClass().getName());
    }
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   * @declaredat ASTNode:66
   */
  @Deprecated
  public Attribute fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:76
   */
  public Attribute treeCopyNoTransform() {
    Attribute tree = (Attribute) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        ASTNode child = (ASTNode) children[i];
        if (child != null) {
          child = child.treeCopyNoTransform();
          tree.setChild(child, i);
        }
      }
    }
    return tree;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:96
   */
  public Attribute treeCopy() {
    Attribute tree = (Attribute) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        ASTNode child = (ASTNode) getChild(i);
        if (child != null) {
          child = child.treeCopy();
          tree.setChild(child, i);
        }
      }
    }
    return tree;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:110
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) && (tokenString_Label == ((Attribute) node).tokenString_Label);    
  }
  /**
   * Replaces the lexeme Label.
   * @param value The new value for the lexeme Label.
   * @apilevel high-level
   */
  public void setLabel(String value) {
    tokenString_Label = value;
  }
  /** @apilevel internal 
   */
  protected String tokenString_Label;
  /**
   * Retrieves the value for the lexeme Label.
   * @return The value for the lexeme Label.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Label")
  public String getLabel() {
    return tokenString_Label != null ? tokenString_Label : "";
  }
  /**
   * Replaces the optional node for the Argument child. This is the <code>Opt</code>
   * node containing the child Argument, not the actual child!
   * @param opt The new node to be used as the optional node for the Argument child.
   * @apilevel low-level
   */
  public void setArgumentOpt(Opt<Expression> opt) {
    setChild(opt, 0);
  }
  /**
   * Replaces the (optional) Argument child.
   * @param node The new node to be used as the Argument child.
   * @apilevel high-level
   */
  public void setArgument(Expression node) {
    getArgumentOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional Argument child exists.
   * @return {@code true} if the optional Argument child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasArgument() {
    return getArgumentOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) Argument child.
   * @return The Argument child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Expression getArgument() {
    return (Expression) getArgumentOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the Argument child. This is the <code>Opt</code> node containing the child Argument, not the actual child!
   * @return The optional node for child the Argument child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="Argument")
  public Opt<Expression> getArgumentOpt() {
    return (Opt<Expression>) getChild(0);
  }
  /**
   * Retrieves the optional node for child Argument. This is the <code>Opt</code> node containing the child Argument, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child Argument.
   * @apilevel low-level
   */
  public Opt<Expression> getArgumentOptNoTransform() {
    return (Opt<Expression>) getChildNoTransform(0);
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
