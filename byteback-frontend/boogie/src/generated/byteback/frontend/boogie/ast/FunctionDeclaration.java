/* This file was generated with JastAdd2 (http://jastadd.org) version 2.3.2 */
package byteback.frontend.boogie.ast;
/**
 * @ast node
 * @declaredat /home/mpaganoni/Projects/byteback/byteback-frontend/boogie/spec/Boogie.ast:27
 * @astdecl FunctionDeclaration : ASTNode ::= FunctionSignature;
 * @production FunctionDeclaration : {@link ASTNode} ::= <span class="component">{@link FunctionSignature}</span>;

 */
public class FunctionDeclaration extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public FunctionDeclaration() {
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
  }
  /**
   * @declaredat ASTNode:13
   */
  @ASTNodeAnnotation.Constructor(
    name = {"FunctionSignature"},
    type = {"FunctionSignature"},
    kind = {"Child"}
  )
  public FunctionDeclaration(FunctionSignature p0) {
    setChild(p0, 0);
  }
  /** @apilevel low-level 
   * @declaredat ASTNode:22
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:28
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:32
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:36
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /** @apilevel internal 
   * @declaredat ASTNode:40
   */
  public FunctionDeclaration clone() throws CloneNotSupportedException {
    FunctionDeclaration node = (FunctionDeclaration) super.clone();
    return node;
  }
  /** @apilevel internal 
   * @declaredat ASTNode:45
   */
  public FunctionDeclaration copy() {
    try {
      FunctionDeclaration node = (FunctionDeclaration) clone();
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
   * @declaredat ASTNode:64
   */
  @Deprecated
  public FunctionDeclaration fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:74
   */
  public FunctionDeclaration treeCopyNoTransform() {
    FunctionDeclaration tree = (FunctionDeclaration) copy();
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
   * @declaredat ASTNode:94
   */
  public FunctionDeclaration treeCopy() {
    FunctionDeclaration tree = (FunctionDeclaration) copy();
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
   * @declaredat ASTNode:108
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);    
  }
  /**
   * Replaces the FunctionSignature child.
   * @param node The new node to replace the FunctionSignature child.
   * @apilevel high-level
   */
  public void setFunctionSignature(FunctionSignature node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the FunctionSignature child.
   * @return The current node used as the FunctionSignature child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="FunctionSignature")
  public FunctionSignature getFunctionSignature() {
    return (FunctionSignature) getChild(0);
  }
  /**
   * Retrieves the FunctionSignature child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the FunctionSignature child.
   * @apilevel low-level
   */
  public FunctionSignature getFunctionSignatureNoTransform() {
    return (FunctionSignature) getChildNoTransform(0);
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
