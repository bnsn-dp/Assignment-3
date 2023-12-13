package frontend;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import backend.Group;
import backend.User;
import backend.Client;
import backend.Observer;
import frontend.PTree;

public class PUserManagement extends JPanel{
	private JButton userViewButton;
	private JButton addUserButton;
	private JButton addGroupButton;
	private JPanel treePanel;
	private JPanel addUserPanel;
	private JPanel addGroupPanel;
	private JTextField inputUserID;
	private JTextField inputGroupID;
	private HashMap<String, Observer> everyone;
	private HashMap<String, JFrame> openViews;

	public PUserManagement(JPanel treePanel, HashMap<String, Observer> everyone){
		super();
        this.treePanel = treePanel;
        this.everyone = everyone;
		this.setLayout(new GridLayout(3, 1));
        defineComponents();
        addComponents();
	}

	private void addComponents(){
		this.add(userViewButton);
        addUserPanel = new JPanel();
        addUserPanel.setLayout(new FlowLayout());
        addUserPanel.add(inputUserID);
        addUserPanel.add(addUserButton);
        addGroupPanel = new JPanel();
        addGroupPanel.setLayout(new FlowLayout());
        addGroupPanel.add(inputGroupID);
        addGroupPanel.add(addGroupButton);
		this.add(addUserPanel);
		this.add(addGroupPanel);
	}

	private void defineComponents(){
		openViews = new HashMap<String, JFrame>();
		userViewButton = new JButton("Open Selected User View");
		inputUserID = new JTextField("User ID");
        inputUserID.setPreferredSize(new Dimension(200, 25));
		inputGroupID = new JTextField("Group ID");
        inputGroupID.setPreferredSize(new Dimension(200, 25));
		addUserButton = new JButton("Add User");
		addGroupButton = new JButton("Add Group");
		initializeOpenUserViewActionListener();
		initializeAddUserButtonActionListener();
		initializeAddGroupButtonActionListener();

	}

	private DefaultMutableTreeNode getSelectedNode() {
        JTree tree = ((PTree) treePanel).getTree();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if (!((PTree) treePanel).getRoot().equals(selectedNode)) {
            selectedNode = (DefaultMutableTreeNode) selectedNode.getUserObject();
        }

        return selectedNode;
    }

	private void initializeOpenUserViewActionListener() {
        userViewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = getSelectedNode();
                if (!everyone.containsKey(((Client) selectedNode).getID())) {
                    InfoDialogueBox dialogBox = new InfoDialogueBox("Error!",
                            "No such user exists!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (selectedNode.getClass() == Group.class) {
                    InfoDialogueBox dialogBox = new InfoDialogueBox("Error!",
                            "Cannot open user view for a group!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (openViews.containsKey(((Client) selectedNode).getID())) {
                    InfoDialogueBox dialogBox = new InfoDialogueBox("Error!",
                            "User view already open for " + ((Client) selectedNode).getID() + "!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (selectedNode.getClass() == User.class) {
                    ViewUser userView = new ViewUser(everyone, openViews, selectedNode);
                    openViews.put(((Client) selectedNode).getID(), userView);
                    System.out.print(((Client) selectedNode).getID() + ": ");
                    System.out.println(((User) selectedNode).getCreatedTime());
                }
            }
        });
    }

	private void initializeAddUserButtonActionListener() {
        addUserButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // check if user ID already exists
                if (everyone.containsKey(inputUserID.getText())) {
                    InfoDialogueBox dialogBox = new InfoDialogueBox("Error!",
                            "User already exists!\nPlease choose a different user name.",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Observer child = new User(inputUserID.getText());

                    everyone.put(((Client) child).getID(), child);
                    ((PTree) treePanel).addUser((DefaultMutableTreeNode) child);
                }
            }
        });
    }

    private void initializeAddGroupButtonActionListener() {
        addGroupButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // check if user ID already exists
                if (everyone.containsKey(inputGroupID.getText())) {
                    InfoDialogueBox dialogBox = new InfoDialogueBox("Error!",
                            "User already exists!\nPlease choose a different user name.",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Observer child = new Group(inputGroupID.getText());

                    everyone.put(((User) child).getID(), child);
                    ((PTree) treePanel).addGroup((DefaultMutableTreeNode) child);
                }
            }
        });
    }
}
