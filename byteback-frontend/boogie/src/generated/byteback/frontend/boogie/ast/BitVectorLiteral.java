/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:128
 * @astdecl BitVectorLiteral : Literal ::= <Value:Integer> <Size:Integer>;
 * @production BitVectorLiteral : {@link Literal} ::= <span class="component">&lt;Value:Integer&gt;</span> <span class="component">&lt;Size:Integer&gt;</span>;

 */
public class BitVectorLiteral extends Literal implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public BitVectorLiteral() {
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
    name = {"Value", "Size"},
    type = {"Integer", "Integer"},
    kind = {"Token", "Token"}
  )
  public BitVectorLiteral(Integer p0, Integer p1) {
    setValue(p0);
    setSize(p1);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:22
   */
  protected int numChildren() {
    return 0;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:28
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:32
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:36
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:40
   */
  public BitVectorLiteral clone() throws CloneNotSupportedException {
    BitVectorLiteral node = (BitVectorLiteral) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:45
   */
  public BitVectorLiteral copy() {
    try {
      BitVectorLiteral node = (BitVectorLiteral) clone();
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
   * @declaredat ASTNode:64
   */
  @Deprecated
  public BitVectorLiteral fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:74
   */
  public BitVectorLiteral treeCopyNoTransform() {
    BitVectorLiteral tree = (BitVectorLiteral) copy();
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
   * @declaredat ASTNode:94
   */
  public BitVectorLiteral treeCopy() {
    BitVectorLiteral tree = (BitVectorLiteral) copy();
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
   * @declaredat ASTNode:108
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) && (tokenInteger_Value == ((BitVectorLiteral) node).tokenInteger_Value) && (tokenInteger_Size == ((BitVectorLiteral) node).tokenInteger_Size);    
  }
  /**
   * Replaces the lexeme Value.
   * @param value The new value for the lexeme Value.
   * @apilevel high-level
   */
  public void setValue(Integer value) {
    tokenInteger_Value = value;
  }
  /** @apilevel internal 
   */
  protected Integer tokenInteger_Value;
  /**
   * Retrieves the value for the lexeme Value.
   * @return The value for the lexeme Value.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Value")
  public Integer getValue() {
    return tokenInteger_Value;
  }
  /**
   * Replaces the lexeme Size.
   * @param value The new value for the lexeme Size.
   * @apilevel high-level
   */
  public void setSize(Integer value) {
    tokenInteger_Size = value;
  }
  /** @apilevel internal 
   */
  protected Integer tokenInteger_Size;
  /**
   * Retrieves the value for the lexeme Size.
   * @return The value for the lexeme Size.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Size")
  public Integer getSize() {
    return tokenInteger_Size;
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
