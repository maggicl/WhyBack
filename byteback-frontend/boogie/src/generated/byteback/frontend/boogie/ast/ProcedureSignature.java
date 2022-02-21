/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:42
 * @astdecl ProcedureSignature : ASTNode ::= TypeArguments:TypeArgument* InputParameters:Binding* OutputParameters:Binding*;
 * @production ProcedureSignature : {@link ASTNode} ::= <span class="component">TypeArguments:{@link TypeArgument}*</span> <span class="component">InputParameters:{@link Binding}*</span> <span class="component">OutputParameters:{@link Binding}*</span>;

 */
public class ProcedureSignature extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public ProcedureSignature() {
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
    setChild(new List(), 2);
  }
  /**
   * @declaredat ASTNode:16
   */
  @ASTNodeAnnotation.Constructor(
    name = {"TypeArguments", "InputParameters", "OutputParameters"},
    type = {"List<TypeArgument>", "List<Binding>", "List<Binding>"},
    kind = {"List", "List", "List"}
  )
  public ProcedureSignature(List<TypeArgument> p0, List<Binding> p1, List<Binding> p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:27
   */
  protected int numChildren() {
    return 3;
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
  public ProcedureSignature clone() throws CloneNotSupportedException {
    ProcedureSignature node = (ProcedureSignature) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:50
   */
  public ProcedureSignature copy() {
    try {
      ProcedureSignature node = (ProcedureSignature) clone();
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
  public ProcedureSignature fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:79
   */
  public ProcedureSignature treeCopyNoTransform() {
    ProcedureSignature tree = (ProcedureSignature) copy();
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
  public ProcedureSignature treeCopy() {
    ProcedureSignature tree = (ProcedureSignature) copy();
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
   * Replaces the InputParameters list.
   * @param list The new list node to be used as the InputParameters list.
   * @apilevel high-level
   */
  public void setInputParametersList(List<Binding> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the InputParameters list.
   * @return Number of children in the InputParameters list.
   * @apilevel high-level
   */
  public int getNumInputParameters() {
    return getInputParametersList().getNumChild();
  }
  /**
   * Retrieves the number of children in the InputParameters list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the InputParameters list.
   * @apilevel low-level
   */
  public int getNumInputParametersNoTransform() {
    return getInputParametersListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the InputParameters list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the InputParameters list.
   * @apilevel high-level
   */
  public Binding getInputParameters(int i) {
    return (Binding) getInputParametersList().getChild(i);
  }
  /**
   * Check whether the InputParameters list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasInputParameters() {
    return getInputParametersList().getNumChild() != 0;
  }
  /**
   * Append an element to the InputParameters list.
   * @param node The element to append to the InputParameters list.
   * @apilevel high-level
   */
  public void addInputParameters(Binding node) {
    List<Binding> list = (parent == null) ? getInputParametersListNoTransform() : getInputParametersList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addInputParametersNoTransform(Binding node) {
    List<Binding> list = getInputParametersListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the InputParameters list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setInputParameters(Binding node, int i) {
    List<Binding> list = getInputParametersList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the InputParameters list.
   * @return The node representing the InputParameters list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="InputParameters")
  public List<Binding> getInputParametersList() {
    List<Binding> list = (List<Binding>) getChild(1);
    return list;
  }
  /**
   * Retrieves the InputParameters list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the InputParameters list.
   * @apilevel low-level
   */
  public List<Binding> getInputParametersListNoTransform() {
    return (List<Binding>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the InputParameters list without
   * triggering rewrites.
   */
  public Binding getInputParametersNoTransform(int i) {
    return (Binding) getInputParametersListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the InputParameters list.
   * @return The node representing the InputParameters list.
   * @apilevel high-level
   */
  public List<Binding> getInputParameterss() {
    return getInputParametersList();
  }
  /**
   * Retrieves the InputParameters list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the InputParameters list.
   * @apilevel low-level
   */
  public List<Binding> getInputParameterssNoTransform() {
    return getInputParametersListNoTransform();
  }
  /**
   * Replaces the OutputParameters list.
   * @param list The new list node to be used as the OutputParameters list.
   * @apilevel high-level
   */
  public void setOutputParametersList(List<Binding> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the OutputParameters list.
   * @return Number of children in the OutputParameters list.
   * @apilevel high-level
   */
  public int getNumOutputParameters() {
    return getOutputParametersList().getNumChild();
  }
  /**
   * Retrieves the number of children in the OutputParameters list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the OutputParameters list.
   * @apilevel low-level
   */
  public int getNumOutputParametersNoTransform() {
    return getOutputParametersListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the OutputParameters list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the OutputParameters list.
   * @apilevel high-level
   */
  public Binding getOutputParameters(int i) {
    return (Binding) getOutputParametersList().getChild(i);
  }
  /**
   * Check whether the OutputParameters list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasOutputParameters() {
    return getOutputParametersList().getNumChild() != 0;
  }
  /**
   * Append an element to the OutputParameters list.
   * @param node The element to append to the OutputParameters list.
   * @apilevel high-level
   */
  public void addOutputParameters(Binding node) {
    List<Binding> list = (parent == null) ? getOutputParametersListNoTransform() : getOutputParametersList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addOutputParametersNoTransform(Binding node) {
    List<Binding> list = getOutputParametersListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the OutputParameters list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setOutputParameters(Binding node, int i) {
    List<Binding> list = getOutputParametersList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the OutputParameters list.
   * @return The node representing the OutputParameters list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="OutputParameters")
  public List<Binding> getOutputParametersList() {
    List<Binding> list = (List<Binding>) getChild(2);
    return list;
  }
  /**
   * Retrieves the OutputParameters list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the OutputParameters list.
   * @apilevel low-level
   */
  public List<Binding> getOutputParametersListNoTransform() {
    return (List<Binding>) getChildNoTransform(2);
  }
  /**
   * @return the element at index {@code i} in the OutputParameters list without
   * triggering rewrites.
   */
  public Binding getOutputParametersNoTransform(int i) {
    return (Binding) getOutputParametersListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the OutputParameters list.
   * @return The node representing the OutputParameters list.
   * @apilevel high-level
   */
  public List<Binding> getOutputParameterss() {
    return getOutputParametersList();
  }
  /**
   * Retrieves the OutputParameters list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the OutputParameters list.
   * @apilevel low-level
   */
  public List<Binding> getOutputParameterssNoTransform() {
    return getOutputParametersListNoTransform();
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
