/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:158
 * @astdecl BreakStatement : Statement ::= [Label:Identifier];
 * @production BreakStatement : {@link Statement} ::= <span class="component">[Label:{@link Identifier}]</span>;

 */
public class BreakStatement extends Statement implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public BreakStatement() {
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
    name = {"Label"},
    type = {"Opt<Identifier>"},
    kind = {"Opt"}
  )
  public BreakStatement(Opt<Identifier> p0) {
    setChild(p0, 0);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:23
   */
  protected int numChildren() {
    return 1;
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
  public BreakStatement clone() throws CloneNotSupportedException {
    BreakStatement node = (BreakStatement) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:46
   */
  public BreakStatement copy() {
    try {
      BreakStatement node = (BreakStatement) clone();
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
   * @declaredat ASTNode:65
   */
  @Deprecated
  public BreakStatement fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:75
   */
  public BreakStatement treeCopyNoTransform() {
    BreakStatement tree = (BreakStatement) copy();
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
   * @declaredat ASTNode:95
   */
  public BreakStatement treeCopy() {
    BreakStatement tree = (BreakStatement) copy();
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
   * @declaredat ASTNode:109
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);    
  }
  /**
   * Replaces the optional node for the Label child. This is the <code>Opt</code>
   * node containing the child Label, not the actual child!
   * @param opt The new node to be used as the optional node for the Label child.
   * @apilevel low-level
   */
  public void setLabelOpt(Opt<Identifier> opt) {
    setChild(opt, 0);
  }
  /**
   * Replaces the (optional) Label child.
   * @param node The new node to be used as the Label child.
   * @apilevel high-level
   */
  public void setLabel(Identifier node) {
    getLabelOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional Label child exists.
   * @return {@code true} if the optional Label child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasLabel() {
    return getLabelOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) Label child.
   * @return The Label child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Identifier getLabel() {
    return (Identifier) getLabelOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the Label child. This is the <code>Opt</code> node containing the child Label, not the actual child!
   * @return The optional node for child the Label child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="Label")
  public Opt<Identifier> getLabelOpt() {
    return (Opt<Identifier>) getChild(0);
  }
  /**
   * Retrieves the optional node for child Label. This is the <code>Opt</code> node containing the child Label, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child Label.
   * @apilevel low-level
   */
  public Opt<Identifier> getLabelOptNoTransform() {
    return (Opt<Identifier>) getChildNoTransform(0);
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
