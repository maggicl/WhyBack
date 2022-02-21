/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:23
 * @astdecl ParentEdge : ASTNode ::= <Unique:Boolean> Parent:Constant;
 * @production ParentEdge : {@link ASTNode} ::= <span class="component">&lt;Unique:Boolean&gt;</span> <span class="component">Parent:{@link Constant}</span>;

 */
public class ParentEdge extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public ParentEdge() {
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
  }
  /**
   * @declaredat ASTNode:13
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Unique", "Parent"},
    type = {"Boolean", "Constant"},
    kind = {"Token", "Child"}
  )
  public ParentEdge(Boolean p0, Constant p1) {
    setUnique(p0);
    setChild(p1, 0);
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
  public ParentEdge clone() throws CloneNotSupportedException {
    ParentEdge node = (ParentEdge) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:46
   */
  public ParentEdge copy() {
    try {
      ParentEdge node = (ParentEdge) clone();
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
  public ParentEdge fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:75
   */
  public ParentEdge treeCopyNoTransform() {
    ParentEdge tree = (ParentEdge) copy();
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
  public ParentEdge treeCopy() {
    ParentEdge tree = (ParentEdge) copy();
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
    return super.is$Equal(node) && (tokenBoolean_Unique == ((ParentEdge) node).tokenBoolean_Unique);    
  }
  /**
   * Replaces the lexeme Unique.
   * @param value The new value for the lexeme Unique.
   * @apilevel high-level
   */
  public void setUnique(Boolean value) {
    tokenBoolean_Unique = value;
  }
  /** @apilevel internal 
   */
  protected Boolean tokenBoolean_Unique;
  /**
   * Retrieves the value for the lexeme Unique.
   * @return The value for the lexeme Unique.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Unique")
  public Boolean getUnique() {
    return tokenBoolean_Unique;
  }
  /**
   * Replaces the Parent child.
   * @param node The new node to replace the Parent child.
   * @apilevel high-level
   */
  public void setParent(Constant node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Parent child.
   * @return The current node used as the Parent child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Parent")
  public Constant getParent() {
    return (Constant) getChild(0);
  }
  /**
   * Retrieves the Parent child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Parent child.
   * @apilevel low-level
   */
  public Constant getParentNoTransform() {
    return (Constant) getChildNoTransform(0);
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
