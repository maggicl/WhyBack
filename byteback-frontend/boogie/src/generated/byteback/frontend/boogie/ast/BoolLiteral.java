/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:133
 * @astdecl BoolLiteral : Literal ::= <Value:Boolean>;
 * @production BoolLiteral : {@link Literal} ::= <span class="component">&lt;Value:Boolean&gt;</span>;

 */
public class BoolLiteral extends Literal implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public BoolLiteral() {
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
  }
  /**
   * @declaredat ASTNode:12
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Value"},
    type = {"Boolean"},
    kind = {"Token"}
  )
  public BoolLiteral(Boolean p0) {
    setValue(p0);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:21
   */
  protected int numChildren() {
    return 0;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:27
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:31
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:35
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:39
   */
  public BoolLiteral clone() throws CloneNotSupportedException {
    BoolLiteral node = (BoolLiteral) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:44
   */
  public BoolLiteral copy() {
    try {
      BoolLiteral node = (BoolLiteral) clone();
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
   * @declaredat ASTNode:63
   */
  @Deprecated
  public BoolLiteral fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:73
   */
  public BoolLiteral treeCopyNoTransform() {
    BoolLiteral tree = (BoolLiteral) copy();
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
   * @declaredat ASTNode:93
   */
  public BoolLiteral treeCopy() {
    BoolLiteral tree = (BoolLiteral) copy();
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
   * @declaredat ASTNode:107
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) && (tokenBoolean_Value == ((BoolLiteral) node).tokenBoolean_Value);    
  }
  /**
   * Replaces the lexeme Value.
   * @param value The new value for the lexeme Value.
   * @apilevel high-level
   */
  public void setValue(Boolean value) {
    tokenBoolean_Value = value;
  }
  /** @apilevel internal 
   */
  protected Boolean tokenBoolean_Value;
  /**
   * Retrieves the value for the lexeme Value.
   * @return The value for the lexeme Value.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Value")
  public Boolean getValue() {
    return tokenBoolean_Value;
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
