/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:21
 * @astdecl OrderSpecification : ASTNode ::= Edges:ParentEdge* <Complete:Boolean>;
 * @production OrderSpecification : {@link ASTNode} ::= <span class="component">Edges:{@link ParentEdge}*</span> <span class="component">&lt;Complete:Boolean&gt;</span>;

 */
public class OrderSpecification extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public OrderSpecification() {
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
    setChild(new List(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Edges", "Complete"},
    type = {"List<ParentEdge>", "Boolean"},
    kind = {"List", "Token"}
  )
  public OrderSpecification(List<ParentEdge> p0, Boolean p1) {
    setChild(p0, 0);
    setComplete(p1);
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
  public OrderSpecification clone() throws CloneNotSupportedException {
    OrderSpecification node = (OrderSpecification) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:47
   */
  public OrderSpecification copy() {
    try {
      OrderSpecification node = (OrderSpecification) clone();
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
  public OrderSpecification fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:76
   */
  public OrderSpecification treeCopyNoTransform() {
    OrderSpecification tree = (OrderSpecification) copy();
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
  public OrderSpecification treeCopy() {
    OrderSpecification tree = (OrderSpecification) copy();
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
    return super.is$Equal(node) && (tokenBoolean_Complete == ((OrderSpecification) node).tokenBoolean_Complete);    
  }
  /**
   * Replaces the Edges list.
   * @param list The new list node to be used as the Edges list.
   * @apilevel high-level
   */
  public void setEdgesList(List<ParentEdge> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the Edges list.
   * @return Number of children in the Edges list.
   * @apilevel high-level
   */
  public int getNumEdges() {
    return getEdgesList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Edges list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Edges list.
   * @apilevel low-level
   */
  public int getNumEdgesNoTransform() {
    return getEdgesListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Edges list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Edges list.
   * @apilevel high-level
   */
  public ParentEdge getEdges(int i) {
    return (ParentEdge) getEdgesList().getChild(i);
  }
  /**
   * Check whether the Edges list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasEdges() {
    return getEdgesList().getNumChild() != 0;
  }
  /**
   * Append an element to the Edges list.
   * @param node The element to append to the Edges list.
   * @apilevel high-level
   */
  public void addEdges(ParentEdge node) {
    List<ParentEdge> list = (parent == null) ? getEdgesListNoTransform() : getEdgesList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addEdgesNoTransform(ParentEdge node) {
    List<ParentEdge> list = getEdgesListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Edges list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setEdges(ParentEdge node, int i) {
    List<ParentEdge> list = getEdgesList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Edges list.
   * @return The node representing the Edges list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Edges")
  public List<ParentEdge> getEdgesList() {
    List<ParentEdge> list = (List<ParentEdge>) getChild(0);
    return list;
  }
  /**
   * Retrieves the Edges list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Edges list.
   * @apilevel low-level
   */
  public List<ParentEdge> getEdgesListNoTransform() {
    return (List<ParentEdge>) getChildNoTransform(0);
  }
  /**
   * @return the element at index {@code i} in the Edges list without
   * triggering rewrites.
   */
  public ParentEdge getEdgesNoTransform(int i) {
    return (ParentEdge) getEdgesListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Edges list.
   * @return The node representing the Edges list.
   * @apilevel high-level
   */
  public List<ParentEdge> getEdgess() {
    return getEdgesList();
  }
  /**
   * Retrieves the Edges list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Edges list.
   * @apilevel low-level
   */
  public List<ParentEdge> getEdgessNoTransform() {
    return getEdgesListNoTransform();
  }
  /**
   * Replaces the lexeme Complete.
   * @param value The new value for the lexeme Complete.
   * @apilevel high-level
   */
  public void setComplete(Boolean value) {
    tokenBoolean_Complete = value;
  }
  /** @apilevel internal 
   */
  protected Boolean tokenBoolean_Complete;
  /**
   * Retrieves the value for the lexeme Complete.
   * @return The value for the lexeme Complete.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Complete")
  public Boolean getComplete() {
    return tokenBoolean_Complete;
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
