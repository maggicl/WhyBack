/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:105
 * @astdecl ConstructedType : Type ::= Name:Identifier Arguments:Type*;
 * @production ConstructedType : {@link Type} ::= <span class="component">Name:{@link Identifier}</span> <span class="component">Arguments:{@link Type}*</span>;

 */
public class ConstructedType extends Type implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public ConstructedType() {
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
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:14
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Name", "Arguments"},
    type = {"Identifier", "List<Type>"},
    kind = {"Child", "List"}
  )
  public ConstructedType(Identifier p0, List<Type> p1) {
    setChild(p0, 0);
    setChild(p1, 1);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:24
   */
  protected int numChildren() {
    return 2;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:30
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:34
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:38
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:42
   */
  public ConstructedType clone() throws CloneNotSupportedException {
    ConstructedType node = (ConstructedType) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:47
   */
  public ConstructedType copy() {
    try {
      ConstructedType node = (ConstructedType) clone();
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
   * @declaredat ASTNode:66
   */
  @Deprecated
  public ConstructedType fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:76
   */
  public ConstructedType treeCopyNoTransform() {
    ConstructedType tree = (ConstructedType) copy();
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
   * @declaredat ASTNode:96
   */
  public ConstructedType treeCopy() {
    ConstructedType tree = (ConstructedType) copy();
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
   * @declaredat ASTNode:110
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);    
  }
  /**
   * Replaces the Name child.
   * @param node The new node to replace the Name child.
   * @apilevel high-level
   */
  public void setName(Identifier node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Name child.
   * @return The current node used as the Name child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Name")
  public Identifier getName() {
    return (Identifier) getChild(0);
  }
  /**
   * Retrieves the Name child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Name child.
   * @apilevel low-level
   */
  public Identifier getNameNoTransform() {
    return (Identifier) getChildNoTransform(0);
  }
  /**
   * Replaces the Arguments list.
   * @param list The new list node to be used as the Arguments list.
   * @apilevel high-level
   */
  public void setArgumentsList(List<Type> list) {
    setChild(list, 1);
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
  public Type getArguments(int i) {
    return (Type) getArgumentsList().getChild(i);
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
  public void addArguments(Type node) {
    List<Type> list = (parent == null) ? getArgumentsListNoTransform() : getArgumentsList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addArgumentsNoTransform(Type node) {
    List<Type> list = getArgumentsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Arguments list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setArguments(Type node, int i) {
    List<Type> list = getArgumentsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Arguments list.
   * @return The node representing the Arguments list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Arguments")
  public List<Type> getArgumentsList() {
    List<Type> list = (List<Type>) getChild(1);
    return list;
  }
  /**
   * Retrieves the Arguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Arguments list.
   * @apilevel low-level
   */
  public List<Type> getArgumentsListNoTransform() {
    return (List<Type>) getChildNoTransform(1);
  }
  /**
   * @return the element at index {@code i} in the Arguments list without
   * triggering rewrites.
   */
  public Type getArgumentsNoTransform(int i) {
    return (Type) getArgumentsListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Arguments list.
   * @return The node representing the Arguments list.
   * @apilevel high-level
   */
  public List<Type> getArgumentss() {
    return getArgumentsList();
  }
  /**
   * Retrieves the Arguments list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Arguments list.
   * @apilevel low-level
   */
  public List<Type> getArgumentssNoTransform() {
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
