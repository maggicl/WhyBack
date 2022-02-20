/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:107
 * @astdecl MapType : Type ::= Arguments:TypeArgument* KeyTypes:Type* ValueType:Type;
 * @production MapType : {@link Type} ::= <span class="component">Arguments:{@link TypeArgument}*</span> <span class="component">KeyTypes:{@link Type}*</span> <span class="component">ValueType:{@link Type}</span>;

 */
public class MapType extends Type implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public MapType() {
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
    name = {"Arguments", "KeyTypes", "ValueType"},
    type = {"List<TypeArgument>", "List<Type>", "Type"},
    kind = {"List", "List", "Child"}
  )
  public MapType(List<TypeArgument> p0, List<Type> p1, Type p2) {
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
  public MapType clone() throws CloneNotSupportedException {
    MapType node = (MapType) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:49
   */
  public MapType copy() {
    try {
      MapType node = (MapType) clone();
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
  public MapType fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:78
   */
  public MapType treeCopyNoTransform() {
    MapType tree = (MapType) copy();
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
  public MapType treeCopy() {
    MapType tree = (MapType) copy();
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
   * Replaces the Arguments list.
   * @param list The new list node to be used as the Arguments list.
   * @apilevel high-level
   */
  public void setArgumentsList(List<TypeArgument> list) {
    setChild(list, 0);
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
  public TypeArgument getArguments(int i) {
    return (TypeArgument) getArgumentsList().getChild(i);
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
  public void addArguments(TypeArgument node) {
    List<TypeArgument> list = (parent == null) ? getArgumentsListNoTransform() : getArgumentsList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addArgumentsNoTransform(TypeArgument node) {
    List<TypeArgument> list = getArgumentsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Arguments list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setArguments(TypeArgument node, int i) {
    List<TypeArgument> list = getArgumentsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Arguments list.
   * @return The node representing the Arguments list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Arguments")
  public List<TypeArgument> getArgumentsList() {
    List<TypeArgument> list = (List<TypeArgument>) getChild(0);
    return list;
  }
  /**
   * Retrieves the Arguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Arguments list.
   * @apilevel low-level
   */
  public List<TypeArgument> getArgumentsListNoTransform() {
    return (List<TypeArgument>) getChildNoTransform(0);
  }
  /**
   * @return the element at index {@code i} in the Arguments list without
   * triggering rewrites.
   */
  public TypeArgument getArgumentsNoTransform(int i) {
    return (TypeArgument) getArgumentsListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Arguments list.
   * @return The node representing the Arguments list.
   * @apilevel high-level
   */
  public List<TypeArgument> getArgumentss() {
    return getArgumentsList();
  }
  /**
   * Retrieves the Arguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Arguments list.
   * @apilevel low-level
   */
  public List<TypeArgument> getArgumentssNoTransform() {
    return getArgumentsListNoTransform();
  }
  /**
   * Replaces the KeyTypes list.
   * @param list The new list node to be used as the KeyTypes list.
   * @apilevel high-level
   */
  public void setKeyTypesList(List<Type> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the KeyTypes list.
   * @return Number of children in the KeyTypes list.
   * @apilevel high-level
   */
  public int getNumKeyTypes() {
    return getKeyTypesList().getNumChild();
  }
  /**
   * Retrieves the number of children in the KeyTypes list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the KeyTypes list.
   * @apilevel low-level
   */
  public int getNumKeyTypesNoTransform() {
    return getKeyTypesListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the KeyTypes list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the KeyTypes list.
   * @apilevel high-level
   */
  public Type getKeyTypes(int i) {
    return (Type) getKeyTypesList().getChild(i);
  }
  /**
   * Check whether the KeyTypes list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasKeyTypes() {
    return getKeyTypesList().getNumChild() != 0;
  }
  /**
   * Append an element to the KeyTypes list.
   * @param node The element to append to the KeyTypes list.
   * @apilevel high-level
   */
  public void addKeyTypes(Type node) {
    List<Type> list = (parent == null) ? getKeyTypesListNoTransform() : getKeyTypesList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addKeyTypesNoTransform(Type node) {
    List<Type> list = getKeyTypesListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the KeyTypes list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setKeyTypes(Type node, int i) {
    List<Type> list = getKeyTypesList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the KeyTypes list.
   * @return The node representing the KeyTypes list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="KeyTypes")
  public List<Type> getKeyTypesList() {
    List<Type> list = (List<Type>) getChild(1);
    return list;
  }
  /**
   * Retrieves the KeyTypes list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the KeyTypes list.
   * @apilevel low-level
   */
  public List<Type> getKeyTypesListNoTransform() {
    return (List<Type>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the KeyTypes list without
   * triggering rewrites.
   */
  public Type getKeyTypesNoTransform(int i) {
    return (Type) getKeyTypesListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the KeyTypes list.
   * @return The node representing the KeyTypes list.
   * @apilevel high-level
   */
  public List<Type> getKeyTypess() {
    return getKeyTypesList();
  }
  /**
   * Retrieves the KeyTypes list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the KeyTypes list.
   * @apilevel low-level
   */
  public List<Type> getKeyTypessNoTransform() {
    return getKeyTypesListNoTransform();
  }
  /**
   * Replaces the ValueType child.
   * @param node The new node to replace the ValueType child.
   * @apilevel high-level
   */
  public void setValueType(Type node) {
    setChild(node, 2);
  }
  /**
   * Retrieves the ValueType child.
   * @return The current node used as the ValueType child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="ValueType")
  public Type getValueType() {
    return (Type) getChild(2);
  }
  /**
   * Retrieves the ValueType child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the ValueType child.
   * @apilevel low-level
   */
  public Type getValueTypeNoTransform() {
    return (Type) getChildNoTransform(2);
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
