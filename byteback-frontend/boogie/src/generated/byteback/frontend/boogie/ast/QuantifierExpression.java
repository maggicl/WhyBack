/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:112
 * @astdecl QuantifierExpression : UnaryExpression ::= Operand:Expression TypeArguments:TypeArgument* Parameters:Binding* Triggers:Expression*;
 * @production QuantifierExpression : {@link UnaryExpression} ::= <span class="component">TypeArguments:{@link TypeArgument}*</span> <span class="component">Parameters:{@link Binding}*</span> <span class="component">Triggers:{@link Expression}*</span>;

 */
public abstract class QuantifierExpression extends UnaryExpression implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public QuantifierExpression() {
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
    setChild(new List(), 1);
    setChild(new List(), 2);
    setChild(new List(), 3);
  }
  /**
   * @declaredat ASTNode:16
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Operand", "TypeArguments", "Parameters", "Triggers"},
    type = {"Expression", "List<TypeArgument>", "List<Binding>", "List<Expression>"},
    kind = {"Child", "List", "List", "List"}
  )
  public QuantifierExpression(Expression p0, List<TypeArgument> p1, List<Binding> p2, List<Expression> p3) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
    setChild(p3, 3);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:28
   */
  protected int numChildren() {
    return 4;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:34
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:38
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:42
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:46
   */
  public QuantifierExpression clone() throws CloneNotSupportedException {
    QuantifierExpression node = (QuantifierExpression) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   * @declaredat ASTNode:57
   */
  @Deprecated
  public abstract QuantifierExpression fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:65
   */
  public abstract QuantifierExpression treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:73
   */
  public abstract QuantifierExpression treeCopy();
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
   * Replaces the TypeArguments list.
   * @param list The new list node to be used as the TypeArguments list.
   * @apilevel high-level
   */
  public void setTypeArgumentsList(List<TypeArgument> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the TypeArguments list.
   * @return Number of children in the TypeArguments list.
   * @apilevel high-level
   */
  public int getNumTypeArguments() {
    return getTypeArgumentsList().getNumChild();
  }
  /**
   * Retrieves the number of children in the TypeArguments list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the TypeArguments list.
   * @apilevel low-level
   */
  public int getNumTypeArgumentsNoTransform() {
    return getTypeArgumentsListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the TypeArguments list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the TypeArguments list.
   * @apilevel high-level
   */
  public TypeArgument getTypeArguments(int i) {
    return (TypeArgument) getTypeArgumentsList().getChild(i);
  }
  /**
   * Check whether the TypeArguments list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTypeArguments() {
    return getTypeArgumentsList().getNumChild() != 0;
  }
  /**
   * Append an element to the TypeArguments list.
   * @param node The element to append to the TypeArguments list.
   * @apilevel high-level
   */
  public void addTypeArguments(TypeArgument node) {
    List<TypeArgument> list = (parent == null) ? getTypeArgumentsListNoTransform() : getTypeArgumentsList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addTypeArgumentsNoTransform(TypeArgument node) {
    List<TypeArgument> list = getTypeArgumentsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the TypeArguments list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTypeArguments(TypeArgument node, int i) {
    List<TypeArgument> list = getTypeArgumentsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the TypeArguments list.
   * @return The node representing the TypeArguments list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="TypeArguments")
  public List<TypeArgument> getTypeArgumentsList() {
    List<TypeArgument> list = (List<TypeArgument>) getChild(1);
    return list;
  }
  /**
   * Retrieves the TypeArguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeArguments list.
   * @apilevel low-level
   */
  public List<TypeArgument> getTypeArgumentsListNoTransform() {
    return (List<TypeArgument>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the TypeArguments list without
   * triggering rewrites.
   */
  public TypeArgument getTypeArgumentsNoTransform(int i) {
    return (TypeArgument) getTypeArgumentsListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the TypeArguments list.
   * @return The node representing the TypeArguments list.
   * @apilevel high-level
   */
  public List<TypeArgument> getTypeArgumentss() {
    return getTypeArgumentsList();
  }
  /**
   * Retrieves the TypeArguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeArguments list.
   * @apilevel low-level
   */
  public List<TypeArgument> getTypeArgumentssNoTransform() {
    return getTypeArgumentsListNoTransform();
  }
  /**
   * Replaces the Parameters list.
   * @param list The new list node to be used as the Parameters list.
   * @apilevel high-level
   */
  public void setParametersList(List<Binding> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the Parameters list.
   * @return Number of children in the Parameters list.
   * @apilevel high-level
   */
  public int getNumParameters() {
    return getParametersList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Parameters list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Parameters list.
   * @apilevel low-level
   */
  public int getNumParametersNoTransform() {
    return getParametersListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Parameters list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Parameters list.
   * @apilevel high-level
   */
  public Binding getParameters(int i) {
    return (Binding) getParametersList().getChild(i);
  }
  /**
   * Check whether the Parameters list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasParameters() {
    return getParametersList().getNumChild() != 0;
  }
  /**
   * Append an element to the Parameters list.
   * @param node The element to append to the Parameters list.
   * @apilevel high-level
   */
  public void addParameters(Binding node) {
    List<Binding> list = (parent == null) ? getParametersListNoTransform() : getParametersList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addParametersNoTransform(Binding node) {
    List<Binding> list = getParametersListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Parameters list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setParameters(Binding node, int i) {
    List<Binding> list = getParametersList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Parameters list.
   * @return The node representing the Parameters list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Parameters")
  public List<Binding> getParametersList() {
    List<Binding> list = (List<Binding>) getChild(2);
    return list;
  }
  /**
   * Retrieves the Parameters list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Parameters list.
   * @apilevel low-level
   */
  public List<Binding> getParametersListNoTransform() {
    return (List<Binding>) getChildNoTransform(2);
  }
  /**
   * @return the element at index {@code i} in the Parameters list without
   * triggering rewrites.
   */
  public Binding getParametersNoTransform(int i) {
    return (Binding) getParametersListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Parameters list.
   * @return The node representing the Parameters list.
   * @apilevel high-level
   */
  public List<Binding> getParameterss() {
    return getParametersList();
  }
  /**
   * Retrieves the Parameters list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Parameters list.
   * @apilevel low-level
   */
  public List<Binding> getParameterssNoTransform() {
    return getParametersListNoTransform();
  }
  /**
   * Replaces the Triggers list.
   * @param list The new list node to be used as the Triggers list.
   * @apilevel high-level
   */
  public void setTriggersList(List<Expression> list) {
    setChild(list, 3);
  }
  /**
   * Retrieves the number of children in the Triggers list.
   * @return Number of children in the Triggers list.
   * @apilevel high-level
   */
  public int getNumTriggers() {
    return getTriggersList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Triggers list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Triggers list.
   * @apilevel low-level
   */
  public int getNumTriggersNoTransform() {
    return getTriggersListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Triggers list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Triggers list.
   * @apilevel high-level
   */
  public Expression getTriggers(int i) {
    return (Expression) getTriggersList().getChild(i);
  }
  /**
   * Check whether the Triggers list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTriggers() {
    return getTriggersList().getNumChild() != 0;
  }
  /**
   * Append an element to the Triggers list.
   * @param node The element to append to the Triggers list.
   * @apilevel high-level
   */
  public void addTriggers(Expression node) {
    List<Expression> list = (parent == null) ? getTriggersListNoTransform() : getTriggersList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addTriggersNoTransform(Expression node) {
    List<Expression> list = getTriggersListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Triggers list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTriggers(Expression node, int i) {
    List<Expression> list = getTriggersList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Triggers list.
   * @return The node representing the Triggers list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Triggers")
  public List<Expression> getTriggersList() {
    List<Expression> list = (List<Expression>) getChild(3);
    return list;
  }
  /**
   * Retrieves the Triggers list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Triggers list.
   * @apilevel low-level
   */
  public List<Expression> getTriggersListNoTransform() {
    return (List<Expression>) getChildNoTransform(3);
  }
  /**
   * @return the element at index {@code i} in the Triggers list without
   * triggering rewrites.
   */
  public Expression getTriggersNoTransform(int i) {
    return (Expression) getTriggersListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Triggers list.
   * @return The node representing the Triggers list.
   * @apilevel high-level
   */
  public List<Expression> getTriggerss() {
    return getTriggersList();
  }
  /**
   * Retrieves the Triggers list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Triggers list.
   * @apilevel low-level
   */
  public List<Expression> getTriggerssNoTransform() {
    return getTriggersListNoTransform();
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
