/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:92
 * @astdecl BitVector : Literal ::= <Value:Integer> <Length:Integer>;
 * @production BitVector : {@link Literal} ::= <span class="component">&lt;Value:Integer&gt;</span> <span class="component">&lt;Length:Integer&gt;</span>;

 */
public class BitVector extends Literal implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public BitVector() {
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
    name = {"Value", "Length"},
    type = {"Integer", "Integer"},
    kind = {"Token", "Token"}
  )
  public BitVector(Integer p0, Integer p1) {
    setValue(p0);
    setLength(p1);
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
  public BitVector clone() throws CloneNotSupportedException {
    BitVector node = (BitVector) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:45
   */
  public BitVector copy() {
    try {
      BitVector node = (BitVector) clone();
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
  public BitVector fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:74
   */
  public BitVector treeCopyNoTransform() {
    BitVector tree = (BitVector) copy();
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
  public BitVector treeCopy() {
    BitVector tree = (BitVector) copy();
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
    return super.is$Equal(node) && (tokenInteger_Value == ((BitVector) node).tokenInteger_Value) && (tokenInteger_Length == ((BitVector) node).tokenInteger_Length);    
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
   * Replaces the lexeme Length.
   * @param value The new value for the lexeme Length.
   * @apilevel high-level
   */
  public void setLength(Integer value) {
    tokenInteger_Length = value;
  }
  /** @apilevel internal 
   */
  protected Integer tokenInteger_Length;
  /**
   * Retrieves the value for the lexeme Length.
   * @return The value for the lexeme Length.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Length")
  public Integer getLength() {
    return tokenInteger_Length;
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
