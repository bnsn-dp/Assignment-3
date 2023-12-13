package frontend;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import backend.*;

public class ViewAdmin extends ViewWindow{
	private static ViewAdmin INSTANCE;
	private JPanel tree;
	private JPanel addition;
	private JMenuBar diagnostics;
	private HashMap<String, Observer> everyone;
	private JMenuItem newestTweet;
	private JMenuItem newestUser;
	private JMenuItem userCount;
	private JMenuItem groupCount;
	private JMenuItem messageCount;
	private JMenuItem positivity;

	public static ViewAdmin getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ViewAdmin("Admin");
		}
		return INSTANCE;
	}

	private ViewAdmin(String title) {
		super(title);
		everyone = new HashMap<String, Observer>();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		defineComponents();
		addComponents();
	}

	@Override
	public void addComponents(){
		this.add(tree, BorderLayout.NORTH);
		this.add(addition, BorderLayout.SOUTH);
		this.setJMenuBar(diagnostics);
		this.pack();
	}

	@Override
	public void defineComponents(){
		everyone.put(Root.getInstance().getID(), Root.getInstance());
		tree = new PTree(Root.getInstance());
		addition = new PUserManagement(tree, everyone);
		addition.setPreferredSize(new Dimension(360, 160));
		defineMenuBar();
	}

	public void defineMenuBar(){
		diagnostics = new JMenuBar();
		JMenu latestChanges = new JMenu("Latest Changes");
		newestUser = new JMenuItem("Newest user");
		newestTweet = new JMenuItem("Newest tweet");
		latestChanges.add(newestUser);
		latestChanges.add(newestTweet);
		JMenu statistics = new JMenu("Statistics");
		userCount = new JMenuItem("Total Users");
		initializeUserTotalButtonActionListener();
		groupCount = new JMenuItem("Total Groups");
		initializeGroupTotalButtonActionListener();
		messageCount = new JMenuItem("Total Messages");
		initializeMessagesTotalButtonActionListener();
		positivity = new JMenuItem("Positivity");
		initializePositivePercentageButtonActionListener();
		statistics.add(userCount);
		statistics.add(groupCount);
		statistics.add(messageCount);
		statistics.add(positivity);
		diagnostics.add(latestChanges);
		diagnostics.add(statistics);
	}

	private DefaultMutableTreeNode getSelectedNode() {
        JTree tree = ((PTree) this.tree).getTree();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if (!((PTree) this.tree).getRoot().equals(selectedNode)) {
            selectedNode = (DefaultMutableTreeNode) selectedNode.getUserObject();
        }

        return selectedNode;
    }

	private void initializeUserTotalButtonActionListener() {
        userCount.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // get User selected in TreePanel
                DefaultMutableTreeNode selectedNode = getSelectedNode();

                VClientTotal visitor = new VClientTotal();
                ((User) selectedNode).accept(visitor);
                String message = "Total number of inidividual users within "
                        + ((User) selectedNode).getID() + ": "
                        + Integer.toString(visitor.visitUser(((User) selectedNode)));

                InfoDialogueBox popUp = new InfoDialogueBox(((User) selectedNode).getID() + " information",
                        message, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

	private void initializeGroupTotalButtonActionListener() {
        groupCount.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // get User selected in TreePanel
                DefaultMutableTreeNode selectedNode = getSelectedNode();

                VGroupTotal visitor = new VGroupTotal();
                ((User) selectedNode).accept(visitor);
                String message = "Total number of groups within "
                        + ((User) selectedNode).getID() + ": "
                        + Integer.toString(visitor.visitUser(((User) selectedNode)));

                InfoDialogueBox popUp = new InfoDialogueBox(((User) selectedNode).getID() + " information",
                        message, JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }

	private void initializeMessagesTotalButtonActionListener() {
        messageCount.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // get User selected in TreePanel
                DefaultMutableTreeNode selectedNode = getSelectedNode();

                VMessageTotal visitor = new VMessageTotal();
                ((User) selectedNode).accept(visitor);
                String message = "Total number of messages sent by "
                        + ((User) selectedNode).getID() + ": "
                        + Integer.toString(visitor.visitUser(((User) selectedNode)));

                InfoDialogueBox popUp = new InfoDialogueBox(((User) selectedNode).getID() + " information",
                        message, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

	private void initializePositivePercentageButtonActionListener() {
        positivity.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // get User selected in TreePanel
                DefaultMutableTreeNode selectedNode = getSelectedNode();

                VPositiveTotal positiveCountVisitor = new VPositiveTotal();
                ((User) selectedNode).accept(positiveCountVisitor);
                int positiveCount = positiveCountVisitor.visitUser(((User) selectedNode));

                VMessageTotal messageCountVisitor = new VMessageTotal();
                ((User) selectedNode).accept(messageCountVisitor);
                int messageCount = messageCountVisitor.visitUser(((User) selectedNode));

                // calculate percentage, set percentage to 0.00 if no messages have yet been sent
                double percentage = 0;
                if (messageCount > 0) {
                    percentage = ((double) positiveCount / messageCount) * 100;
                }
                String percentageString = String.format("%.2f", percentage);

                String message = "Percentage of positive messages sent by "
                        + ((User) selectedNode).getID() + ": "
                        + percentageString + "%";

                InfoDialogueBox popUp = new InfoDialogueBox(((User) selectedNode).getID() + " information",
                        message, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

}
