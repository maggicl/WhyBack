/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:48
 * @astdecl LocalVariableDeclaration : Declaration ::= Identifier:Identifier Attributes:Attribute* Variables:Binding*;
 * @production LocalVariableDeclaration : {@link Declaration} ::= <span class="component">Variables:{@link Binding}*</span>;

 */
public class LocalVariableDeclaration extends Declaration implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public LocalVariableDeclaration() {
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
    name = {"Identifier", "Attributes", "Variables"},
    type = {"Identifier", "List<Attribute>", "List<Binding>"},
    kind = {"Child", "List", "List"}
  )
  public LocalVariableDeclaration(Identifier p0, List<Attribute> p1, List<Binding> p2) {
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
  public LocalVariableDeclaration clone() throws CloneNotSupportedException {
    LocalVariableDeclaration node = (LocalVariableDeclaration) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:49
   */
  public LocalVariableDeclaration copy() {
    try {
      LocalVariableDeclaration node = (LocalVariableDeclaration) clone();
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
  public LocalVariableDeclaration fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:78
   */
  public LocalVariableDeclaration treeCopyNoTransform() {
    LocalVariableDeclaration tree = (LocalVariableDeclaration) copy();
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
  public LocalVariableDeclaration treeCopy() {
    LocalVariableDeclaration tree = (LocalVariableDeclaration) copy();
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
   * Replaces the Identifier child.
   * @param node The new node to replace the Identifier child.
   * @apilevel high-level
   */
  public void setIdentifier(Identifier node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Identifier child.
   * @return The current node used as the Identifier child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Identifier")
  public Identifier getIdentifier() {
    return (Identifier) getChild(0);
  }
  /**
   * Retrieves the Identifier child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Identifier child.
   * @apilevel low-level
   */
  public Identifier getIdentifierNoTransform() {
    return (Identifier) getChildNoTransform(0);
  }
  /**
   * Replaces the Attributes list.
   * @param list The new list node to be used as the Attributes list.
   * @apilevel high-level
   */
  public void setAttributesList(List<Attribute> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the Attributes list.
   * @return Number of children in the Attributes list.
   * @apilevel high-level
   */
  public int getNumAttributes() {
    return getAttributesList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Attributes list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Attributes list.
   * @apilevel low-level
   */
  public int getNumAttributesNoTransform() {
    return getAttributesListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Attributes list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Attributes list.
   * @apilevel high-level
   */
  public Attribute getAttributes(int i) {
    return (Attribute) getAttributesList().getChild(i);
  }
  /**
   * Check whether the Attributes list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasAttributes() {
    return getAttributesList().getNumChild() != 0;
  }
  /**
   * Append an element to the Attributes list.
   * @param node The element to append to the Attributes list.
   * @apilevel high-level
   */
  public void addAttributes(Attribute node) {
    List<Attribute> list = (parent == null) ? getAttributesListNoTransform() : getAttributesList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addAttributesNoTransform(Attribute node) {
    List<Attribute> list = getAttributesListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Attributes list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setAttributes(Attribute node, int i) {
    List<Attribute> list = getAttributesList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Attributes list.
   * @return The node representing the Attributes list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Attributes")
  public List<Attribute> getAttributesList() {
    List<Attribute> list = (List<Attribute>) getChild(1);
    return list;
  }
  /**
   * Retrieves the Attributes list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Attributes list.
   * @apilevel low-level
   */
  public List<Attribute> getAttributesListNoTransform() {
    return (List<Attribute>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the Attributes list without
   * triggering rewrites.
   */
  public Attribute getAttributesNoTransform(int i) {
    return (Attribute) getAttributesListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Attributes list.
   * @return The node representing the Attributes list.
   * @apilevel high-level
   */
  public List<Attribute> getAttributess() {
    return getAttributesList();
  }
  /**
   * Retrieves the Attributes list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Attributes list.
   * @apilevel low-level
   */
  public List<Attribute> getAttributessNoTransform() {
    return getAttributesListNoTransform();
  }
  /**
   * Replaces the Variables list.
   * @param list The new list node to be used as the Variables list.
   * @apilevel high-level
   */
  public void setVariablesList(List<Binding> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the Variables list.
   * @return Number of children in the Variables list.
   * @apilevel high-level
   */
  public int getNumVariables() {
    return getVariablesList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Variables list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Variables list.
   * @apilevel low-level
   */
  public int getNumVariablesNoTransform() {
    return getVariablesListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Variables list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Variables list.
   * @apilevel high-level
   */
  public Binding getVariables(int i) {
    return (Binding) getVariablesList().getChild(i);
  }
  /**
   * Check whether the Variables list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasVariables() {
    return getVariablesList().getNumChild() != 0;
  }
  /**
   * Append an element to the Variables list.
   * @param node The element to append to the Variables list.
   * @apilevel high-level
   */
  public void addVariables(Binding node) {
    List<Binding> list = (parent == null) ? getVariablesListNoTransform() : getVariablesList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addVariablesNoTransform(Binding node) {
    List<Binding> list = getVariablesListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Variables list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setVariables(Binding node, int i) {
    List<Binding> list = getVariablesList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Variables list.
   * @return The node representing the Variables list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Variables")
  public List<Binding> getVariablesList() {
    List<Binding> list = (List<Binding>) getChild(2);
    return list;
  }
  /**
   * Retrieves the Variables list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Variables list.
   * @apilevel low-level
   */
  public List<Binding> getVariablesListNoTransform() {
    return (List<Binding>) getChildNoTransform(2);
  }
  /**
   * @return the element at index {@code i} in the Variables list without
   * triggering rewrites.
   */
  public Binding getVariablesNoTransform(int i) {
    return (Binding) getVariablesListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Variables list.
   * @return The node representing the Variables list.
   * @apilevel high-level
   */
  public List<Binding> getVariabless() {
    return getVariablesList();
  }
  /**
   * Retrieves the Variables list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Variables list.
   * @apilevel low-level
   */
  public List<Binding> getVariablessNoTransform() {
    return getVariablesListNoTransform();
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
