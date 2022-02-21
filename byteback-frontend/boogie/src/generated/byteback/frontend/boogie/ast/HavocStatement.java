/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:143
 * @astdecl HavocStatement : Statement ::= Label:Identifier SymbolicReference*;
 * @production HavocStatement : {@link Statement} ::= <span class="component">{@link SymbolicReference}*</span>;

 */
public class HavocStatement extends Statement implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public HavocStatement() {
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
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:14
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Label", "SymbolicReference"},
    type = {"Identifier", "List<SymbolicReference>"},
    kind = {"Child", "List"}
  )
  public HavocStatement(Identifier p0, List<SymbolicReference> p1) {
    setChild(p0, 0);
    setChild(p1, 1);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:24
   */
  protected int numChildren() {
    return 2;
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
  public HavocStatement clone() throws CloneNotSupportedException {
    HavocStatement node = (HavocStatement) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:47
   */
  public HavocStatement copy() {
    try {
      HavocStatement node = (HavocStatement) clone();
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
  public HavocStatement fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:76
   */
  public HavocStatement treeCopyNoTransform() {
    HavocStatement tree = (HavocStatement) copy();
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
  public HavocStatement treeCopy() {
    HavocStatement tree = (HavocStatement) copy();
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
    return super.is$Equal(node);    
  }
  /**
   * Replaces the Label child.
   * @param node The new node to replace the Label child.
   * @apilevel high-level
   */
  public void setLabel(Identifier node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Label child.
   * @return The current node used as the Label child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Label")
  public Identifier getLabel() {
    return (Identifier) getChild(0);
  }
  /**
   * Retrieves the Label child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Label child.
   * @apilevel low-level
   */
  public Identifier getLabelNoTransform() {
    return (Identifier) getChildNoTransform(0);
  }
  /**
   * Replaces the SymbolicReference list.
   * @param list The new list node to be used as the SymbolicReference list.
   * @apilevel high-level
   */
  public void setSymbolicReferenceList(List<SymbolicReference> list) {
    setChild(list, 1);
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
    List<SymbolicReference> list = (List<SymbolicReference>) getChild(1);
    return list;
  }
  /**
   * Retrieves the SymbolicReference list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the SymbolicReference list.
   * @apilevel low-level
   */
  public List<SymbolicReference> getSymbolicReferenceListNoTransform() {
    return (List<SymbolicReference>) getChildNoTransform(1);
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
