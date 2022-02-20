/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:87
 * @astdecl MapRangedAccessOperation : UnaryExpression ::= Operand:Expression <RangeStart:Integer> <RangeEnd:Integer>;
 * @production MapRangedAccessOperation : {@link UnaryExpression} ::= <span class="component">&lt;RangeStart:Integer&gt;</span> <span class="component">&lt;RangeEnd:Integer&gt;</span>;

 */
public class MapRangedAccessOperation extends UnaryExpression implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public MapRangedAccessOperation() {
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
    name = {"Operand", "RangeStart", "RangeEnd"},
    type = {"Expression", "Integer", "Integer"},
    kind = {"Child", "Token", "Token"}
  )
  public MapRangedAccessOperation(Expression p0, Integer p1, Integer p2) {
    setChild(p0, 0);
    setRangeStart(p1);
    setRangeEnd(p2);
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
  public MapRangedAccessOperation clone() throws CloneNotSupportedException {
    MapRangedAccessOperation node = (MapRangedAccessOperation) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:47
   */
  public MapRangedAccessOperation copy() {
    try {
      MapRangedAccessOperation node = (MapRangedAccessOperation) clone();
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
  public MapRangedAccessOperation fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:76
   */
  public MapRangedAccessOperation treeCopyNoTransform() {
    MapRangedAccessOperation tree = (MapRangedAccessOperation) copy();
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
  public MapRangedAccessOperation treeCopy() {
    MapRangedAccessOperation tree = (MapRangedAccessOperation) copy();
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
    return super.is$Equal(node) && (tokenInteger_RangeStart == ((MapRangedAccessOperation) node).tokenInteger_RangeStart) && (tokenInteger_RangeEnd == ((MapRangedAccessOperation) node).tokenInteger_RangeEnd);    
  }
  /**
   * Replaces the Operand child.
   * @param node The new node to replace the Operand child.
   * @apilevel high-level
   */
  public void setOperand(Expression node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Operand child.
   * @return The current node used as the Operand child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Operand")
  public Expression getOperand() {
    return (Expression) getChild(0);
  }
  /**
   * Retrieves the Operand child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Operand child.
   * @apilevel low-level
   */
  public Expression getOperandNoTransform() {
    return (Expression) getChildNoTransform(0);
  }
  /**
   * Replaces the lexeme RangeStart.
   * @param value The new value for the lexeme RangeStart.
   * @apilevel high-level
   */
  public void setRangeStart(Integer value) {
    tokenInteger_RangeStart = value;
  }
  /** @apilevel internal 
   */
  protected Integer tokenInteger_RangeStart;
  /**
   * Retrieves the value for the lexeme RangeStart.
   * @return The value for the lexeme RangeStart.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="RangeStart")
  public Integer getRangeStart() {
    return tokenInteger_RangeStart;
  }
  /**
   * Replaces the lexeme RangeEnd.
   * @param value The new value for the lexeme RangeEnd.
   * @apilevel high-level
   */
  public void setRangeEnd(Integer value) {
    tokenInteger_RangeEnd = value;
  }
  /** @apilevel internal 
   */
  protected Integer tokenInteger_RangeEnd;
  /**
   * Retrieves the value for the lexeme RangeEnd.
   * @return The value for the lexeme RangeEnd.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="RangeEnd")
  public Integer getRangeEnd() {
    return tokenInteger_RangeEnd;
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
