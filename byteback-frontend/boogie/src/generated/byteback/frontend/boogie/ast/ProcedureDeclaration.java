/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:40
 * @astdecl ProcedureDeclaration : Declaration ::= Identifier:Identifier Attributes:Attribute* Signature:ProcedureSignature Conditions:Specification* [Body];
 * @production ProcedureDeclaration : {@link Declaration} ::= <span class="component">Signature:{@link ProcedureSignature}</span> <span class="component">Conditions:{@link Specification}*</span> <span class="component">[{@link Body}]</span>;

 */
public class ProcedureDeclaration extends Declaration implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public ProcedureDeclaration() {
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
    children = new ASTNode[5];
    setChild(new List(), 1);
    setChild(new List(), 3);
    setChild(new Opt(), 4);
  }
  /**
   * @declaredat ASTNode:16
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Identifier", "Attributes", "Signature", "Conditions", "Body"},
    type = {"Identifier", "List<Attribute>", "ProcedureSignature", "List<Specification>", "Opt<Body>"},
    kind = {"Child", "List", "Child", "List", "Opt"}
  )
  public ProcedureDeclaration(Identifier p0, List<Attribute> p1, ProcedureSignature p2, List<Specification> p3, Opt<Body> p4) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
    setChild(p3, 3);
    setChild(p4, 4);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:29
   */
  protected int numChildren() {
    return 5;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:35
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:39
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:43
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:47
   */
  public ProcedureDeclaration clone() throws CloneNotSupportedException {
    ProcedureDeclaration node = (ProcedureDeclaration) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:52
   */
  public ProcedureDeclaration copy() {
    try {
      ProcedureDeclaration node = (ProcedureDeclaration) clone();
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
   * @declaredat ASTNode:71
   */
  @Deprecated
  public ProcedureDeclaration fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:81
   */
  public ProcedureDeclaration treeCopyNoTransform() {
    ProcedureDeclaration tree = (ProcedureDeclaration) copy();
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
   * @declaredat ASTNode:101
   */
  public ProcedureDeclaration treeCopy() {
    ProcedureDeclaration tree = (ProcedureDeclaration) copy();
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
   * @declaredat ASTNode:115
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
   * Replaces the Signature child.
   * @param node The new node to replace the Signature child.
   * @apilevel high-level
   */
  public void setSignature(ProcedureSignature node) {
    setChild(node, 2);
  }
  /**
   * Retrieves the Signature child.
   * @return The current node used as the Signature child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Signature")
  public ProcedureSignature getSignature() {
    return (ProcedureSignature) getChild(2);
  }
  /**
   * Retrieves the Signature child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Signature child.
   * @apilevel low-level
   */
  public ProcedureSignature getSignatureNoTransform() {
    return (ProcedureSignature) getChildNoTransform(2);
  }
  /**
   * Replaces the Conditions list.
   * @param list The new list node to be used as the Conditions list.
   * @apilevel high-level
   */
  public void setConditionsList(List<Specification> list) {
    setChild(list, 3);
  }
  /**
   * Retrieves the number of children in the Conditions list.
   * @return Number of children in the Conditions list.
   * @apilevel high-level
   */
  public int getNumConditions() {
    return getConditionsList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Conditions list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Conditions list.
   * @apilevel low-level
   */
  public int getNumConditionsNoTransform() {
    return getConditionsListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Conditions list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Conditions list.
   * @apilevel high-level
   */
  public Specification getConditions(int i) {
    return (Specification) getConditionsList().getChild(i);
  }
  /**
   * Check whether the Conditions list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasConditions() {
    return getConditionsList().getNumChild() != 0;
  }
  /**
   * Append an element to the Conditions list.
   * @param node The element to append to the Conditions list.
   * @apilevel high-level
   */
  public void addConditions(Specification node) {
    List<Specification> list = (parent == null) ? getConditionsListNoTransform() : getConditionsList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addConditionsNoTransform(Specification node) {
    List<Specification> list = getConditionsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Conditions list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setConditions(Specification node, int i) {
    List<Specification> list = getConditionsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Conditions list.
   * @return The node representing the Conditions list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Conditions")
  public List<Specification> getConditionsList() {
    List<Specification> list = (List<Specification>) getChild(3);
    return list;
  }
  /**
   * Retrieves the Conditions list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Conditions list.
   * @apilevel low-level
   */
  public List<Specification> getConditionsListNoTransform() {
    return (List<Specification>) getChildNoTransform(3);
  }
  /**
   * @return the element at index {@code i} in the Conditions list without
   * triggering rewrites.
   */
  public Specification getConditionsNoTransform(int i) {
    return (Specification) getConditionsListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Conditions list.
   * @return The node representing the Conditions list.
   * @apilevel high-level
   */
  public List<Specification> getConditionss() {
    return getConditionsList();
  }
  /**
   * Retrieves the Conditions list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Conditions list.
   * @apilevel low-level
   */
  public List<Specification> getConditionssNoTransform() {
    return getConditionsListNoTransform();
  }
  /**
   * Replaces the optional node for the Body child. This is the <code>Opt</code>
   * node containing the child Body, not the actual child!
   * @param opt The new node to be used as the optional node for the Body child.
   * @apilevel low-level
   */
  public void setBodyOpt(Opt<Body> opt) {
    setChild(opt, 4);
  }
  /**
   * Replaces the (optional) Body child.
   * @param node The new node to be used as the Body child.
   * @apilevel high-level
   */
  public void setBody(Body node) {
    getBodyOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional Body child exists.
   * @return {@code true} if the optional Body child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasBody() {
    return getBodyOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) Body child.
   * @return The Body child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Body getBody() {
    return (Body) getBodyOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the Body child. This is the <code>Opt</code> node containing the child Body, not the actual child!
   * @return The optional node for child the Body child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="Body")
  public Opt<Body> getBodyOpt() {
    return (Opt<Body>) getChild(4);
  }
  /**
   * Retrieves the optional node for child Body. This is the <code>Opt</code> node containing the child Body, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child Body.
   * @apilevel low-level
   */
  public Opt<Body> getBodyOptNoTransform() {
    return (Opt<Body>) getChildNoTransform(4);
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
