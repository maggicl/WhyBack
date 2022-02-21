/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:145
 * @astdecl AssignmentStatement : Statement ::= Label:Identifier Targets:SymbolicReference* Source:Expression*;
 * @production AssignmentStatement : {@link Statement} ::= <span class="component">Targets:{@link SymbolicReference}*</span> <span class="component">Source:{@link Expression}*</span>;

 */
public class AssignmentStatement extends Statement implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public AssignmentStatement() {
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
    setChild(new List(), 2);
  }
  /**
   * @declaredat ASTNode:15
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Label", "Targets", "Source"},
    type = {"Identifier", "List<SymbolicReference>", "List<Expression>"},
    kind = {"Child", "List", "List"}
  )
  public AssignmentStatement(Identifier p0, List<SymbolicReference> p1, List<Expression> p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:26
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:32
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:36
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:40
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:44
   */
  public AssignmentStatement clone() throws CloneNotSupportedException {
    AssignmentStatement node = (AssignmentStatement) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:49
   */
  public AssignmentStatement copy() {
    try {
      AssignmentStatement node = (AssignmentStatement) clone();
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
   * @declaredat ASTNode:68
   */
  @Deprecated
  public AssignmentStatement fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:78
   */
  public AssignmentStatement treeCopyNoTransform() {
    AssignmentStatement tree = (AssignmentStatement) copy();
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
   * @declaredat ASTNode:98
   */
  public AssignmentStatement treeCopy() {
    AssignmentStatement tree = (AssignmentStatement) copy();
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
   * @declaredat ASTNode:112
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
   * Replaces the Targets list.
   * @param list The new list node to be used as the Targets list.
   * @apilevel high-level
   */
  public void setTargetsList(List<SymbolicReference> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the Targets list.
   * @return Number of children in the Targets list.
   * @apilevel high-level
   */
  public int getNumTargets() {
    return getTargetsList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Targets list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Targets list.
   * @apilevel low-level
   */
  public int getNumTargetsNoTransform() {
    return getTargetsListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Targets list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Targets list.
   * @apilevel high-level
   */
  public SymbolicReference getTargets(int i) {
    return (SymbolicReference) getTargetsList().getChild(i);
  }
  /**
   * Check whether the Targets list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTargets() {
    return getTargetsList().getNumChild() != 0;
  }
  /**
   * Append an element to the Targets list.
   * @param node The element to append to the Targets list.
   * @apilevel high-level
   */
  public void addTargets(SymbolicReference node) {
    List<SymbolicReference> list = (parent == null) ? getTargetsListNoTransform() : getTargetsList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addTargetsNoTransform(SymbolicReference node) {
    List<SymbolicReference> list = getTargetsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Targets list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTargets(SymbolicReference node, int i) {
    List<SymbolicReference> list = getTargetsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Targets list.
   * @return The node representing the Targets list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Targets")
  public List<SymbolicReference> getTargetsList() {
    List<SymbolicReference> list = (List<SymbolicReference>) getChild(1);
    return list;
  }
  /**
   * Retrieves the Targets list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Targets list.
   * @apilevel low-level
   */
  public List<SymbolicReference> getTargetsListNoTransform() {
    return (List<SymbolicReference>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the Targets list without
   * triggering rewrites.
   */
  public SymbolicReference getTargetsNoTransform(int i) {
    return (SymbolicReference) getTargetsListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Targets list.
   * @return The node representing the Targets list.
   * @apilevel high-level
   */
  public List<SymbolicReference> getTargetss() {
    return getTargetsList();
  }
  /**
   * Retrieves the Targets list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Targets list.
   * @apilevel low-level
   */
  public List<SymbolicReference> getTargetssNoTransform() {
    return getTargetsListNoTransform();
  }
  /**
   * Replaces the Source list.
   * @param list The new list node to be used as the Source list.
   * @apilevel high-level
   */
  public void setSourceList(List<Expression> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the Source list.
   * @return Number of children in the Source list.
   * @apilevel high-level
   */
  public int getNumSource() {
    return getSourceList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Source list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Source list.
   * @apilevel low-level
   */
  public int getNumSourceNoTransform() {
    return getSourceListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Source list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Source list.
   * @apilevel high-level
   */
  public Expression getSource(int i) {
    return (Expression) getSourceList().getChild(i);
  }
  /**
   * Check whether the Source list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasSource() {
    return getSourceList().getNumChild() != 0;
  }
  /**
   * Append an element to the Source list.
   * @param node The element to append to the Source list.
   * @apilevel high-level
   */
  public void addSource(Expression node) {
    List<Expression> list = (parent == null) ? getSourceListNoTransform() : getSourceList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addSourceNoTransform(Expression node) {
    List<Expression> list = getSourceListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Source list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setSource(Expression node, int i) {
    List<Expression> list = getSourceList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Source list.
   * @return The node representing the Source list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Source")
  public List<Expression> getSourceList() {
    List<Expression> list = (List<Expression>) getChild(2);
    return list;
  }
  /**
   * Retrieves the Source list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Source list.
   * @apilevel low-level
   */
  public List<Expression> getSourceListNoTransform() {
    return (List<Expression>) getChildNoTransform(2);
  }
  /**
   * @return the element at index {@code i} in the Source list without
   * triggering rewrites.
   */
  public Expression getSourceNoTransform(int i) {
    return (Expression) getSourceListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Source list.
   * @return The node representing the Source list.
   * @apilevel high-level
   */
  public List<Expression> getSources() {
    return getSourceList();
  }
  /**
   * Retrieves the Source list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Source list.
   * @apilevel low-level
   */
  public List<Expression> getSourcesNoTransform() {
    return getSourceListNoTransform();
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
