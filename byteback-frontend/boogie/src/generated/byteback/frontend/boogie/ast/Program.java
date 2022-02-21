/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:2
 * @astdecl Program : ASTNode ::= Declaration*;
 * @production Program : {@link ASTNode} ::= <span class="component">{@link Declaration}*</span>;

 */
public class Program extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public Program() {
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
    children = new ASTNode[1];
    setChild(new List(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  @ASTNodeAnnotation.Constructor(
    name = {"Declaration"},
    type = {"List<Declaration>"},
    kind = {"List"}
  )
  public Program(List<Declaration> p0) {
    setChild(p0, 0);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:23
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:29
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:33
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:37
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:41
   */
  public Program clone() throws CloneNotSupportedException {
    Program node = (Program) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:46
   */
  public Program copy() {
    try {
      Program node = (Program) clone();
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
   * @declaredat ASTNode:65
   */
  @Deprecated
  public Program fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:75
   */
  public Program treeCopyNoTransform() {
    Program tree = (Program) copy();
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
   * @declaredat ASTNode:95
   */
  public Program treeCopy() {
    Program tree = (Program) copy();
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
   * @declaredat ASTNode:109
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);    
  }
  /**
   * Replaces the Declaration list.
   * @param list The new list node to be used as the Declaration list.
   * @apilevel high-level
   */
  public void setDeclarationList(List<Declaration> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the Declaration list.
   * @return Number of children in the Declaration list.
   * @apilevel high-level
   */
  public int getNumDeclaration() {
    return getDeclarationList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Declaration list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Declaration list.
   * @apilevel low-level
   */
  public int getNumDeclarationNoTransform() {
    return getDeclarationListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Declaration list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Declaration list.
   * @apilevel high-level
   */
  public Declaration getDeclaration(int i) {
    return (Declaration) getDeclarationList().getChild(i);
  }
  /**
   * Check whether the Declaration list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasDeclaration() {
    return getDeclarationList().getNumChild() != 0;
  }
  /**
   * Append an element to the Declaration list.
   * @param node The element to append to the Declaration list.
   * @apilevel high-level
   */
  public void addDeclaration(Declaration node) {
    List<Declaration> list = (parent == null) ? getDeclarationListNoTransform() : getDeclarationList();
    list.addChild(node);
  }
  /** @apilevel low-level 
   */
  public void addDeclarationNoTransform(Declaration node) {
    List<Declaration> list = getDeclarationListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Declaration list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setDeclaration(Declaration node, int i) {
    List<Declaration> list = getDeclarationList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Declaration list.
   * @return The node representing the Declaration list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Declaration")
  public List<Declaration> getDeclarationList() {
    List<Declaration> list = (List<Declaration>) getChild(0);
    return list;
  }
  /**
   * Retrieves the Declaration list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Declaration list.
   * @apilevel low-level
   */
  public List<Declaration> getDeclarationListNoTransform() {
    return (List<Declaration>) getChildNoTransform(0);
  }
  /**
   * @return the element at index {@code i} in the Declaration list without
   * triggering rewrites.
   */
  public Declaration getDeclarationNoTransform(int i) {
    return (Declaration) getDeclarationListNoTransform().getChildNoTransform(i);
  }
  /**
   * Retrieves the Declaration list.
   * @return The node representing the Declaration list.
   * @apilevel high-level
   */
  public List<Declaration> getDeclarations() {
    return getDeclarationList();
  }
  /**
   * Retrieves the Declaration list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Declaration list.
   * @apilevel low-level
   */
  public List<Declaration> getDeclarationsNoTransform() {
    return getDeclarationListNoTransform();
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
