/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:5
 * @astdecl TypeDeclaration : Declaration ::= Attributes:Attribute* Identifiers:Identifier*;
 * @production TypeDeclaration : {@link Declaration} ::= <span class="component">Identifiers:{@link Identifier}*</span>;

 */
public abstract class TypeDeclaration extends Declaration implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public TypeDeclaration() {
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
    name = {"Attributes", "Identifiers"},
    type = {"List<Attribute>", "List<Identifier>"},
    kind = {"List", "List"}
  )
  public TypeDeclaration(List<Attribute> p0, List<Identifier> p1) {
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
  public TypeDeclaration clone() throws CloneNotSupportedException {
    TypeDeclaration node = (TypeDeclaration) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   * @declaredat ASTNode:54
   */
  @Deprecated
  public abstract TypeDeclaration fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:62
   */
  public abstract TypeDeclaration treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:70
   */
  public abstract TypeDeclaration treeCopy();
  /**
   * Replaces the Attributes list.
   * @param list The new list node to be used as the Attributes list.
   * @apilevel high-level
   */
  public void setAttributesList(List<Attribute> list) {
    setChild(list, 0);
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
    List<Attribute> list = (List<Attribute>) getChild(0);
    return list;
  }
  /**
   * Retrieves the Attributes list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Attributes list.
   * @apilevel low-level
   */
  public List<Attribute> getAttributesListNoTransform() {
    return (List<Attribute>) getChildNoTransform(0);
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
   * Replaces the Identifiers list.
   * @param list The new list node to be used as the Identifiers list.
   * @apilevel high-level
   */
  public void setIdentifiersList(List<Identifier> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the Identifiers list.
   * @return Number of children in the Identifiers list.
   * @apilevel high-level
   */
  public int getNumIdentifiers() {
    return getIdentifiersList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Identifiers list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Identifiers list.
   * @apilevel low-level
   */
  public int getNumIdentifiersNoTransform() {
    return getIdentifiersListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Identifiers list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Identifiers list.
   * @apilevel high-level
   */
  public Identifier getIdentifiers(int i) {
    return (Identifier) getIdentifiersList().getChild(i);
  }
  /**
   * Check whether the Identifiers list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasIdentifiers() {
    return getIdentifiersList().getNumChild() != 0;
  }
  /**
   * Append an element to the Identifiers list.
   * @param node The element to append to the Identifiers list.
   * @apilevel high-level
   */
  public void addIdentifiers(Identifier node) {
    List<Identifier> list = (parent == null) ? getIdentifiersListNoTransform() : getIdentifiersList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addIdentifiersNoTransform(Identifier node) {
    List<Identifier> list = getIdentifiersListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Identifiers list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setIdentifiers(Identifier node, int i) {
    List<Identifier> list = getIdentifiersList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Identifiers list.
   * @return The node representing the Identifiers list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Identifiers")
  public List<Identifier> getIdentifiersList() {
    List<Identifier> list = (List<Identifier>) getChild(1);
    return list;
  }
  /**
   * Retrieves the Identifiers list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Identifiers list.
   * @apilevel low-level
   */
  public List<Identifier> getIdentifiersListNoTransform() {
    return (List<Identifier>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the Identifiers list without
   * triggering rewrites.
   */
  public Identifier getIdentifiersNoTransform(int i) {
    return (Identifier) getIdentifiersListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Identifiers list.
   * @return The node representing the Identifiers list.
   * @apilevel high-level
   */
  public List<Identifier> getIdentifierss() {
    return getIdentifiersList();
  }
  /**
   * Retrieves the Identifiers list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Identifiers list.
   * @apilevel low-level
   */
  public List<Identifier> getIdentifierssNoTransform() {
    return getIdentifiersListNoTransform();
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
