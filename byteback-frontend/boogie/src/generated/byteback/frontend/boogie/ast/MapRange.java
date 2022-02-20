/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:86
 * @astdecl MapRange : MapExpression ::= RangeStart:Number RangeEnd:Number;
 * @production MapRange : {@link MapExpression} ::= <span class="component">RangeStart:{@link Number}</span> <span class="component">RangeEnd:{@link Number}</span>;

 */
public class MapRange extends MapExpression implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public MapRange() {
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
    name = {"RangeStart", "RangeEnd"},
    type = {"Number", "Number"},
    kind = {"Child", "Child"}
  )
  public MapRange(Number p0, Number p1) {
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
  public MapRange clone() throws CloneNotSupportedException {
    MapRange node = (MapRange) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:46
   */
  public MapRange copy() {
    try {
      MapRange node = (MapRange) clone();
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
  public MapRange fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:75
   */
  public MapRange treeCopyNoTransform() {
    MapRange tree = (MapRange) copy();
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
  public MapRange treeCopy() {
    MapRange tree = (MapRange) copy();
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
   * Replaces the RangeStart child.
   * @param node The new node to replace the RangeStart child.
   * @apilevel high-level
   */
  public void setRangeStart(Number node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the RangeStart child.
   * @return The current node used as the RangeStart child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="RangeStart")
  public Number getRangeStart() {
    return (Number) getChild(0);
  }
  /**
   * Retrieves the RangeStart child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the RangeStart child.
   * @apilevel low-level
   */
  public Number getRangeStartNoTransform() {
    return (Number) getChildNoTransform(0);
  }
  /**
   * Replaces the RangeEnd child.
   * @param node The new node to replace the RangeEnd child.
   * @apilevel high-level
   */
  public void setRangeEnd(Number node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the RangeEnd child.
   * @return The current node used as the RangeEnd child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="RangeEnd")
  public Number getRangeEnd() {
    return (Number) getChild(1);
  }
  /**
   * Retrieves the RangeEnd child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the RangeEnd child.
   * @apilevel low-level
   */
  public Number getRangeEndNoTransform() {
    return (Number) getChildNoTransform(1);
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
