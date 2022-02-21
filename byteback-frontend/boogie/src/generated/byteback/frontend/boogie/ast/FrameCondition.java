/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:172
 * @astdecl FrameCondition : ASTNode ::= SymbolicReference*;
 * @production FrameCondition : {@link ASTNode} ::= <span class="component">{@link SymbolicReference}*</span>;

 */
public class FrameCondition extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public FrameCondition() {
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
    name = {"SymbolicReference"},
    type = {"List<SymbolicReference>"},
    kind = {"List"}
  )
  public FrameCondition(List<SymbolicReference> p0) {
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
  public FrameCondition clone() throws CloneNotSupportedException {
    FrameCondition node = (FrameCondition) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:46
   */
  public FrameCondition copy() {
    try {
      FrameCondition node = (FrameCondition) clone();
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
  public FrameCondition fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:75
   */
  public FrameCondition treeCopyNoTransform() {
    FrameCondition tree = (FrameCondition) copy();
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
  public FrameCondition treeCopy() {
    FrameCondition tree = (FrameCondition) copy();
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
   * Replaces the SymbolicReference list.
   * @param list The new list node to be used as the SymbolicReference list.
   * @apilevel high-level
   */
  public void setSymbolicReferenceList(List<SymbolicReference> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the SymbolicReference list.
   * @return Number of children in the SymbolicReference list.
   * @apilevel high-level
   */
  public int getNumSymbolicReference() {
    return getSymbolicReferenceList().getNumChild();
  }
  /**
   * Retrieves the number of children in the SymbolicReference list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the SymbolicReference list.
   * @apilevel low-level
   */
  public int getNumSymbolicReferenceNoTransform() {
    return getSymbolicReferenceListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the SymbolicReference list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the SymbolicReference list.
   * @apilevel high-level
   */
  public SymbolicReference getSymbolicReference(int i) {
    return (SymbolicReference) getSymbolicReferenceList().getChild(i);
  }
  /**
   * Check whether the SymbolicReference list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasSymbolicReference() {
    return getSymbolicReferenceList().getNumChild() != 0;
  }
  /**
   * Append an element to the SymbolicReference list.
   * @param node The element to append to the SymbolicReference list.
   * @apilevel high-level
   */
  public void addSymbolicReference(SymbolicReference node) {
    List<SymbolicReference> list = (parent == null) ? getSymbolicReferenceListNoTransform() : getSymbolicReferenceList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addSymbolicReferenceNoTransform(SymbolicReference node) {
    List<SymbolicReference> list = getSymbolicReferenceListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the SymbolicReference list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setSymbolicReference(SymbolicReference node, int i) {
    List<SymbolicReference> list = getSymbolicReferenceList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the SymbolicReference list.
   * @return The node representing the SymbolicReference list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="SymbolicReference")
  public List<SymbolicReference> getSymbolicReferenceList() {
    List<SymbolicReference> list = (List<SymbolicReference>) getChild(0);
    return list;
  }
  /**
   * Retrieves the SymbolicReference list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the SymbolicReference list.
   * @apilevel low-level
   */
  public List<SymbolicReference> getSymbolicReferenceListNoTransform() {
    return (List<SymbolicReference>) getChildNoTransform(0);
  }
  /**
   * @return the element at index {@code i} in the SymbolicReference list without
   * triggering rewrites.
   */
  public SymbolicReference getSymbolicReferenceNoTransform(int i) {
    return (SymbolicReference) getSymbolicReferenceListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the SymbolicReference list.
   * @return The node representing the SymbolicReference list.
   * @apilevel high-level
   */
  public List<SymbolicReference> getSymbolicReferences() {
    return getSymbolicReferenceList();
  }
  /**
   * Retrieves the SymbolicReference list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the SymbolicReference list.
   * @apilevel low-level
   */
  public List<SymbolicReference> getSymbolicReferencesNoTransform() {
    return getSymbolicReferenceListNoTransform();
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
