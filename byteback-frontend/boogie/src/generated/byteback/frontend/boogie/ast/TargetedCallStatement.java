/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:150
 * @astdecl TargetedCallStatement : CallStatement ::= Label:Identifier Callee:Identifier Targets:SymbolicReference* Arguments:Expression*;
 * @production TargetedCallStatement : {@link CallStatement} ::= <span class="component">Targets:{@link SymbolicReference}*</span> <span class="component">Arguments:{@link Expression}*</span>;

 */
public class TargetedCallStatement extends CallStatement implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public TargetedCallStatement() {
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
    children = new ASTNode[4];
    setChild(new List(), 2);
    setChild(new List(), 3);
  }
  /**
   * @declaredat ASTNode:15
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Label", "Callee", "Targets", "Arguments"},
    type = {"Identifier", "Identifier", "List<SymbolicReference>", "List<Expression>"},
    kind = {"Child", "Child", "List", "List"}
  )
  public TargetedCallStatement(Identifier p0, Identifier p1, List<SymbolicReference> p2, List<Expression> p3) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
    setChild(p3, 3);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:27
   */
  protected int numChildren() {
    return 4;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:37
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:41
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:45
   */
  public TargetedCallStatement clone() throws CloneNotSupportedException {
    TargetedCallStatement node = (TargetedCallStatement) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:50
   */
  public TargetedCallStatement copy() {
    try {
      TargetedCallStatement node = (TargetedCallStatement) clone();
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
   * @declaredat ASTNode:69
   */
  @Deprecated
  public TargetedCallStatement fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:79
   */
  public TargetedCallStatement treeCopyNoTransform() {
    TargetedCallStatement tree = (TargetedCallStatement) copy();
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
   * @declaredat ASTNode:99
   */
  public TargetedCallStatement treeCopy() {
    TargetedCallStatement tree = (TargetedCallStatement) copy();
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
   * @declaredat ASTNode:113
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
   * Replaces the Callee child.
   * @param node The new node to replace the Callee child.
   * @apilevel high-level
   */
  public void setCallee(Identifier node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the Callee child.
   * @return The current node used as the Callee child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Callee")
  public Identifier getCallee() {
    return (Identifier) getChild(1);
  }
  /**
   * Retrieves the Callee child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Callee child.
   * @apilevel low-level
   */
  public Identifier getCalleeNoTransform() {
    return (Identifier) getChildNoTransform(1);
  }
  /**
   * Replaces the Targets list.
   * @param list The new list node to be used as the Targets list.
   * @apilevel high-level
   */
  public void setTargetsList(List<SymbolicReference> list) {
    setChild(list, 2);
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
    List<SymbolicReference> list = (List<SymbolicReference>) getChild(2);
    return list;
  }
  /**
   * Retrieves the Targets list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Targets list.
   * @apilevel low-level
   */
  public List<SymbolicReference> getTargetsListNoTransform() {
    return (List<SymbolicReference>) getChildNoTransform(2);
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
   * Replaces the Arguments list.
   * @param list The new list node to be used as the Arguments list.
   * @apilevel high-level
   */
  public void setArgumentsList(List<Expression> list) {
    setChild(list, 3);
  }
  /**
   * Retrieves the number of children in the Arguments list.
   * @return Number of children in the Arguments list.
   * @apilevel high-level
   */
  public int getNumArguments() {
    return getArgumentsList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Arguments list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Arguments list.
   * @apilevel low-level
   */
  public int getNumArgumentsNoTransform() {
    return getArgumentsListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Arguments list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Arguments list.
   * @apilevel high-level
   */
  public Expression getArguments(int i) {
    return (Expression) getArgumentsList().getChild(i);
  }
  /**
   * Check whether the Arguments list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasArguments() {
    return getArgumentsList().getNumChild() != 0;
  }
  /**
   * Append an element to the Arguments list.
   * @param node The element to append to the Arguments list.
   * @apilevel high-level
   */
  public void addArguments(Expression node) {
    List<Expression> list = (parent == null) ? getArgumentsListNoTransform() : getArgumentsList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addArgumentsNoTransform(Expression node) {
    List<Expression> list = getArgumentsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Arguments list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setArguments(Expression node, int i) {
    List<Expression> list = getArgumentsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Arguments list.
   * @return The node representing the Arguments list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Arguments")
  public List<Expression> getArgumentsList() {
    List<Expression> list = (List<Expression>) getChild(3);
    return list;
  }
  /**
   * Retrieves the Arguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Arguments list.
   * @apilevel low-level
   */
  public List<Expression> getArgumentsListNoTransform() {
    return (List<Expression>) getChildNoTransform(3);
  }
  /**
   * @return the element at index {@code i} in the Arguments list without
   * triggering rewrites.
   */
  public Expression getArgumentsNoTransform(int i) {
    return (Expression) getArgumentsListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Arguments list.
   * @return The node representing the Arguments list.
   * @apilevel high-level
   */
  public List<Expression> getArgumentss() {
    return getArgumentsList();
  }
  /**
   * Retrieves the Arguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Arguments list.
   * @apilevel low-level
   */
  public List<Expression> getArgumentssNoTransform() {
    return getArgumentsListNoTransform();
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
