package frontend;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import backend.User;
import backend.Group;
import backend.Client;
import backend.Root;

public class PTree extends JPanel{
	private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private JTree tree;
    private JScrollPane scrollPane;

	public PTree(DefaultMutableTreeNode root){
        super();
		setPreferredSize(new Dimension(360, 480));
        rootNode = root;
        defineComponents();
        addComponents();
    }

	public JTree getTree(){
		return this.tree;
	}

	public DefaultMutableTreeNode getRoot(){
		return this.rootNode;
	}

	public void addGroup(DefaultMutableTreeNode instance){
		DefaultMutableTreeNode parent = null;
		TreePath parentPath = tree.getSelectionPath();
		if(parentPath == null){
			parent = rootNode;
		}
		else{
			parent = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
		}
		addClient(parent, instance, true);
	}

	public void addUser(DefaultMutableTreeNode instance){
		DefaultMutableTreeNode parent = null;
		TreePath parentPath = tree.getSelectionPath();
		if(parentPath == null){
			parent = rootNode;
		}
		else{
			parent = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
		}
		if(parent.getUserObject().getClass() == User.class){
			parent = (DefaultMutableTreeNode) parent.getParent();
		}
		addClient(parent, instance, true);
	}

	private void addClient(DefaultMutableTreeNode parent, DefaultMutableTreeNode instance, boolean visible){
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(instance);
		if (parent == null) {
            parent = rootNode;
        }
		treeModel.insertNodeInto(child, parent, parent.getChildCount());
		if(visible){
			tree.scrollPathToVisible(new TreePath(child.getPath()));
		}
		if(parent.getClass() != Group.class){
			parent = (DefaultMutableTreeNode) parent.getUserObject();
		}
		((Group) parent).addMember((Client) instance);
	}

	private void addComponents(){
		add(scrollPane);
	}

	private void defineComponents(){
		treeModel = new DefaultTreeModel(rootNode);
		treeModel.addTreeModelListener(new MyTreeModelListener());
		tree = new JTree(treeModel);
		tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.setSelectionRow(0);
		scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(300, 500));
	}

	private class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
}
