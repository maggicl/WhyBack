/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:18
 * @astdecl ConstantDeclaration : Declaration ::= Identifier:Identifier Attributes:Attribute* <Unique:Boolean> OrderSpecification;
 * @production ConstantDeclaration : {@link Declaration} ::= <span class="component">&lt;Unique:Boolean&gt;</span> <span class="component">{@link OrderSpecification}</span>;

 */
public class ConstantDeclaration extends Declaration implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public ConstantDeclaration() {
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
  }
  /**
   * @declaredat ASTNode:14
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Identifier", "Attributes", "Unique", "OrderSpecification"},
    type = {"Identifier", "List<Attribute>", "Boolean", "OrderSpecification"},
    kind = {"Child", "List", "Token", "Child"}
  )
  public ConstantDeclaration(Identifier p0, List<Attribute> p1, Boolean p2, OrderSpecification p3) {
    setChild(p0, 0);
    setChild(p1, 1);
    setUnique(p2);
    setChild(p3, 2);
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
  public ConstantDeclaration clone() throws CloneNotSupportedException {
    ConstantDeclaration node = (ConstantDeclaration) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:49
   */
  public ConstantDeclaration copy() {
    try {
      ConstantDeclaration node = (ConstantDeclaration) clone();
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
  public ConstantDeclaration fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:78
   */
  public ConstantDeclaration treeCopyNoTransform() {
    ConstantDeclaration tree = (ConstantDeclaration) copy();
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
  public ConstantDeclaration treeCopy() {
    ConstantDeclaration tree = (ConstantDeclaration) copy();
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
    return super.is$Equal(node) && (tokenBoolean_Unique == ((ConstantDeclaration) node).tokenBoolean_Unique);    
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
   * Replaces the lexeme Unique.
   * @param value The new value for the lexeme Unique.
   * @apilevel high-level
   */
  public void setUnique(Boolean value) {
    tokenBoolean_Unique = value;
  }
  /** @apilevel internal 
   */
  protected Boolean tokenBoolean_Unique;
  /**
   * Retrieves the value for the lexeme Unique.
   * @return The value for the lexeme Unique.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Unique")
  public Boolean getUnique() {
    return tokenBoolean_Unique;
  }
  /**
   * Replaces the OrderSpecification child.
   * @param node The new node to replace the OrderSpecification child.
   * @apilevel high-level
   */
  public void setOrderSpecification(OrderSpecification node) {
    setChild(node, 2);
  }
  /**
   * Retrieves the OrderSpecification child.
   * @return The current node used as the OrderSpecification child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="OrderSpecification")
  public OrderSpecification getOrderSpecification() {
    return (OrderSpecification) getChild(2);
  }
  /**
   * Retrieves the OrderSpecification child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the OrderSpecification child.
   * @apilevel low-level
   */
  public OrderSpecification getOrderSpecificationNoTransform() {
    return (OrderSpecification) getChildNoTransform(2);
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
