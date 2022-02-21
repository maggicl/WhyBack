/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:29
 * @astdecl FunctionSignature : ASTNode ::= TypeArguments:TypeArgument* FunctionArguments:Binding* ReturnArgument:Binding;
 * @production FunctionSignature : {@link ASTNode} ::= <span class="component">TypeArguments:{@link TypeArgument}*</span> <span class="component">FunctionArguments:{@link Binding}*</span> <span class="component">ReturnArgument:{@link Binding}</span>;

 */
public class FunctionSignature extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public FunctionSignature() {
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
    setChild(new List(), 0);
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:15
   */
  @ASTNodeAnnotation.Constructor(
    name = {"TypeArguments", "FunctionArguments", "ReturnArgument"},
    type = {"List<TypeArgument>", "List<Binding>", "Binding"},
    kind = {"List", "List", "Child"}
  )
  public FunctionSignature(List<TypeArgument> p0, List<Binding> p1, Binding p2) {
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
  public FunctionSignature clone() throws CloneNotSupportedException {
    FunctionSignature node = (FunctionSignature) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:49
   */
  public FunctionSignature copy() {
    try {
      FunctionSignature node = (FunctionSignature) clone();
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
  public FunctionSignature fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:78
   */
  public FunctionSignature treeCopyNoTransform() {
    FunctionSignature tree = (FunctionSignature) copy();
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
  public FunctionSignature treeCopy() {
    FunctionSignature tree = (FunctionSignature) copy();
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
   * Replaces the TypeArguments list.
   * @param list The new list node to be used as the TypeArguments list.
   * @apilevel high-level
   */
  public void setTypeArgumentsList(List<TypeArgument> list) {
    setChild(list, 0);
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
    List<TypeArgument> list = (List<TypeArgument>) getChild(0);
    return list;
  }
  /**
   * Retrieves the TypeArguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeArguments list.
   * @apilevel low-level
   */
  public List<TypeArgument> getTypeArgumentsListNoTransform() {
    return (List<TypeArgument>) getChildNoTransform(0);
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
   * Replaces the FunctionArguments list.
   * @param list The new list node to be used as the FunctionArguments list.
   * @apilevel high-level
   */
  public void setFunctionArgumentsList(List<Binding> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the FunctionArguments list.
   * @return Number of children in the FunctionArguments list.
   * @apilevel high-level
   */
  public int getNumFunctionArguments() {
    return getFunctionArgumentsList().getNumChild();
  }
  /**
   * Retrieves the number of children in the FunctionArguments list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the FunctionArguments list.
   * @apilevel low-level
   */
  public int getNumFunctionArgumentsNoTransform() {
    return getFunctionArgumentsListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the FunctionArguments list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the FunctionArguments list.
   * @apilevel high-level
   */
  public Binding getFunctionArguments(int i) {
    return (Binding) getFunctionArgumentsList().getChild(i);
  }
  /**
   * Check whether the FunctionArguments list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasFunctionArguments() {
    return getFunctionArgumentsList().getNumChild() != 0;
  }
  /**
   * Append an element to the FunctionArguments list.
   * @param node The element to append to the FunctionArguments list.
   * @apilevel high-level
   */
  public void addFunctionArguments(Binding node) {
    List<Binding> list = (parent == null) ? getFunctionArgumentsListNoTransform() : getFunctionArgumentsList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addFunctionArgumentsNoTransform(Binding node) {
    List<Binding> list = getFunctionArgumentsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the FunctionArguments list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setFunctionArguments(Binding node, int i) {
    List<Binding> list = getFunctionArgumentsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the FunctionArguments list.
   * @return The node representing the FunctionArguments list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="FunctionArguments")
  public List<Binding> getFunctionArgumentsList() {
    List<Binding> list = (List<Binding>) getChild(1);
    return list;
  }
  /**
   * Retrieves the FunctionArguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the FunctionArguments list.
   * @apilevel low-level
   */
  public List<Binding> getFunctionArgumentsListNoTransform() {
    return (List<Binding>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the FunctionArguments list without
   * triggering rewrites.
   */
  public Binding getFunctionArgumentsNoTransform(int i) {
    return (Binding) getFunctionArgumentsListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the FunctionArguments list.
   * @return The node representing the FunctionArguments list.
   * @apilevel high-level
   */
  public List<Binding> getFunctionArgumentss() {
    return getFunctionArgumentsList();
  }
  /**
   * Retrieves the FunctionArguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the FunctionArguments list.
   * @apilevel low-level
   */
  public List<Binding> getFunctionArgumentssNoTransform() {
    return getFunctionArgumentsListNoTransform();
  }
  /**
   * Replaces the ReturnArgument child.
   * @param node The new node to replace the ReturnArgument child.
   * @apilevel high-level
   */
  public void setReturnArgument(Binding node) {
    setChild(node, 2);
  }
  /**
   * Retrieves the ReturnArgument child.
   * @return The current node used as the ReturnArgument child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="ReturnArgument")
  public Binding getReturnArgument() {
    return (Binding) getChild(2);
  }
  /**
   * Retrieves the ReturnArgument child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the ReturnArgument child.
   * @apilevel low-level
   */
  public Binding getReturnArgumentNoTransform() {
    return (Binding) getChildNoTransform(2);
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
