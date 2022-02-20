/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:89
 * @astdecl MapUpdateOperation : MapAccessOperation ::= Operand:Expression Indexes:Expression* Update:Expression;
 * @production MapUpdateOperation : {@link MapAccessOperation} ::= <span class="component">Update:{@link Expression}</span>;

 */
public class MapUpdateOperation extends MapAccessOperation implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public MapUpdateOperation() {
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
    children = new ASTNode[3];
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:14
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Operand", "Indexes", "Update"},
    type = {"Expression", "List<Expression>", "Expression"},
    kind = {"Child", "List", "Child"}
  )
  public MapUpdateOperation(Expression p0, List<Expression> p1, Expression p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:25
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:31
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:35
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:39
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:43
   */
  public MapUpdateOperation clone() throws CloneNotSupportedException {
    MapUpdateOperation node = (MapUpdateOperation) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:48
   */
  public MapUpdateOperation copy() {
    try {
      MapUpdateOperation node = (MapUpdateOperation) clone();
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
   * @declaredat ASTNode:67
   */
  @Deprecated
  public MapUpdateOperation fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:77
   */
  public MapUpdateOperation treeCopyNoTransform() {
    MapUpdateOperation tree = (MapUpdateOperation) copy();
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
   * @declaredat ASTNode:97
   */
  public MapUpdateOperation treeCopy() {
    MapUpdateOperation tree = (MapUpdateOperation) copy();
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
   * @declaredat ASTNode:111
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);    
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
   * Replaces the Indexes list.
   * @param list The new list node to be used as the Indexes list.
   * @apilevel high-level
   */
  public void setIndexesList(List<Expression> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the Indexes list.
   * @return Number of children in the Indexes list.
   * @apilevel high-level
   */
  public int getNumIndexes() {
    return getIndexesList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Indexes list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Indexes list.
   * @apilevel low-level
   */
  public int getNumIndexesNoTransform() {
    return getIndexesListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Indexes list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Indexes list.
   * @apilevel high-level
   */
  public Expression getIndexes(int i) {
    return (Expression) getIndexesList().getChild(i);
  }
  /**
   * Check whether the Indexes list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasIndexes() {
    return getIndexesList().getNumChild() != 0;
  }
  /**
   * Append an element to the Indexes list.
   * @param node The element to append to the Indexes list.
   * @apilevel high-level
   */
  public void addIndexes(Expression node) {
    List<Expression> list = (parent == null) ? getIndexesListNoTransform() : getIndexesList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addIndexesNoTransform(Expression node) {
    List<Expression> list = getIndexesListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Indexes list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setIndexes(Expression node, int i) {
    List<Expression> list = getIndexesList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Indexes list.
   * @return The node representing the Indexes list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Indexes")
  public List<Expression> getIndexesList() {
    List<Expression> list = (List<Expression>) getChild(1);
    return list;
  }
  /**
   * Retrieves the Indexes list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Indexes list.
   * @apilevel low-level
   */
  public List<Expression> getIndexesListNoTransform() {
    return (List<Expression>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the Indexes list without
   * triggering rewrites.
   */
  public Expression getIndexesNoTransform(int i) {
    return (Expression) getIndexesListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Indexes list.
   * @return The node representing the Indexes list.
   * @apilevel high-level
   */
  public List<Expression> getIndexess() {
    return getIndexesList();
  }
  /**
   * Retrieves the Indexes list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Indexes list.
   * @apilevel low-level
   */
  public List<Expression> getIndexessNoTransform() {
    return getIndexesListNoTransform();
  }
  /**
   * Replaces the Update child.
   * @param node The new node to replace the Update child.
   * @apilevel high-level
   */
  public void setUpdate(Expression node) {
    setChild(node, 2);
  }
  /**
   * Retrieves the Update child.
   * @return The current node used as the Update child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Update")
  public Expression getUpdate() {
    return (Expression) getChild(2);
  }
  /**
   * Retrieves the Update child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Update child.
   * @apilevel low-level
   */
  public Expression getUpdateNoTransform() {
    return (Expression) getChildNoTransform(2);
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
