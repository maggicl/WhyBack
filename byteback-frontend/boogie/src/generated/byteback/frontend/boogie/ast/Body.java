/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:50
 * @astdecl Body : ASTNode ::= LocalVariables:LocalVariableDeclaration* Statements:Statement*;
 * @production Body : {@link ASTNode} ::= <span class="component">LocalVariables:{@link LocalVariableDeclaration}*</span> <span class="component">Statements:{@link Statement}*</span>;

 */
public class Body extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public Body() {
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
    setChild(new List(), 0);
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:15
   */
  @ASTNodeAnnotation.Constructor(
    name = {"LocalVariables", "Statements"},
    type = {"List<LocalVariableDeclaration>", "List<Statement>"},
    kind = {"List", "List"}
  )
  public Body(List<LocalVariableDeclaration> p0, List<Statement> p1) {
    setChild(p0, 0);
    setChild(p1, 1);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:25
   */
  protected int numChildren() {
    return 2;
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
  public Body clone() throws CloneNotSupportedException {
    Body node = (Body) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:48
   */
  public Body copy() {
    try {
      Body node = (Body) clone();
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
  public Body fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:77
   */
  public Body treeCopyNoTransform() {
    Body tree = (Body) copy();
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
  public Body treeCopy() {
    Body tree = (Body) copy();
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
   * Replaces the LocalVariables list.
   * @param list The new list node to be used as the LocalVariables list.
   * @apilevel high-level
   */
  public void setLocalVariablesList(List<LocalVariableDeclaration> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the LocalVariables list.
   * @return Number of children in the LocalVariables list.
   * @apilevel high-level
   */
  public int getNumLocalVariables() {
    return getLocalVariablesList().getNumChild();
  }
  /**
   * Retrieves the number of children in the LocalVariables list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the LocalVariables list.
   * @apilevel low-level
   */
  public int getNumLocalVariablesNoTransform() {
    return getLocalVariablesListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the LocalVariables list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the LocalVariables list.
   * @apilevel high-level
   */
  public LocalVariableDeclaration getLocalVariables(int i) {
    return (LocalVariableDeclaration) getLocalVariablesList().getChild(i);
  }
  /**
   * Check whether the LocalVariables list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasLocalVariables() {
    return getLocalVariablesList().getNumChild() != 0;
  }
  /**
   * Append an element to the LocalVariables list.
   * @param node The element to append to the LocalVariables list.
   * @apilevel high-level
   */
  public void addLocalVariables(LocalVariableDeclaration node) {
    List<LocalVariableDeclaration> list = (parent == null) ? getLocalVariablesListNoTransform() : getLocalVariablesList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addLocalVariablesNoTransform(LocalVariableDeclaration node) {
    List<LocalVariableDeclaration> list = getLocalVariablesListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the LocalVariables list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setLocalVariables(LocalVariableDeclaration node, int i) {
    List<LocalVariableDeclaration> list = getLocalVariablesList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the LocalVariables list.
   * @return The node representing the LocalVariables list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="LocalVariables")
  public List<LocalVariableDeclaration> getLocalVariablesList() {
    List<LocalVariableDeclaration> list = (List<LocalVariableDeclaration>) getChild(0);
    return list;
  }
  /**
   * Retrieves the LocalVariables list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the LocalVariables list.
   * @apilevel low-level
   */
  public List<LocalVariableDeclaration> getLocalVariablesListNoTransform() {
    return (List<LocalVariableDeclaration>) getChildNoTransform(0);
  }
  /**
   * @return the element at index {@code i} in the LocalVariables list without
   * triggering rewrites.
   */
  public LocalVariableDeclaration getLocalVariablesNoTransform(int i) {
    return (LocalVariableDeclaration) getLocalVariablesListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the LocalVariables list.
   * @return The node representing the LocalVariables list.
   * @apilevel high-level
   */
  public List<LocalVariableDeclaration> getLocalVariabless() {
    return getLocalVariablesList();
  }
  /**
   * Retrieves the LocalVariables list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the LocalVariables list.
   * @apilevel low-level
   */
  public List<LocalVariableDeclaration> getLocalVariablessNoTransform() {
    return getLocalVariablesListNoTransform();
  }
  /**
   * Replaces the Statements list.
   * @param list The new list node to be used as the Statements list.
   * @apilevel high-level
   */
  public void setStatementsList(List<Statement> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the Statements list.
   * @return Number of children in the Statements list.
   * @apilevel high-level
   */
  public int getNumStatements() {
    return getStatementsList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Statements list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Statements list.
   * @apilevel low-level
   */
  public int getNumStatementsNoTransform() {
    return getStatementsListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Statements list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Statements list.
   * @apilevel high-level
   */
  public Statement getStatements(int i) {
    return (Statement) getStatementsList().getChild(i);
  }
  /**
   * Check whether the Statements list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasStatements() {
    return getStatementsList().getNumChild() != 0;
  }
  /**
   * Append an element to the Statements list.
   * @param node The element to append to the Statements list.
   * @apilevel high-level
   */
  public void addStatements(Statement node) {
    List<Statement> list = (parent == null) ? getStatementsListNoTransform() : getStatementsList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addStatementsNoTransform(Statement node) {
    List<Statement> list = getStatementsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Statements list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setStatements(Statement node, int i) {
    List<Statement> list = getStatementsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Statements list.
   * @return The node representing the Statements list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Statements")
  public List<Statement> getStatementsList() {
    List<Statement> list = (List<Statement>) getChild(1);
    return list;
  }
  /**
   * Retrieves the Statements list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Statements list.
   * @apilevel low-level
   */
  public List<Statement> getStatementsListNoTransform() {
    return (List<Statement>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the Statements list without
   * triggering rewrites.
   */
  public Statement getStatementsNoTransform(int i) {
    return (Statement) getStatementsListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Statements list.
   * @return The node representing the Statements list.
   * @apilevel high-level
   */
  public List<Statement> getStatementss() {
    return getStatementsList();
  }
  /**
   * Retrieves the Statements list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Statements list.
   * @apilevel low-level
   */
  public List<Statement> getStatementssNoTransform() {
    return getStatementsListNoTransform();
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
