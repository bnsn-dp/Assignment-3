package frontend;

import java.util.Map;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.security.auth.Subject;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import backend.Observer;
import backend.User;
import backend.Group;
import backend.Client;
import backend.Observed;

public class ViewUser extends ViewWindow{
	private JTextField toFollowTextField;
	private GridBagConstraints constraints;

    private JTextArea tweetMessageTextArea;
    private JTextArea currentFollowingTextArea;
    private JTextArea newsFeedTextArea;

    private JScrollPane tweetMessageScrollPane;
    private JScrollPane currentFollowingScrollPane;
    private JScrollPane newsFeedScrollPane;

    private JButton followUserButton;
    private JButton postTweetButton;

    private Observed user;
    private HashMap<String, Observer> everyone;
    private HashMap<String, JFrame> openViews;

	public ViewUser(HashMap<String, Observer> everyone, HashMap<String, JFrame> allViews, DefaultMutableTreeNode user) {
		super(((User) user).getID());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.user = (Observed) user;
		this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                openViews.remove(((User) user).getID());
            }
        });
		this.everyone = everyone;
		this.openViews = allViews;
		defineComponents();
        addComponents();
		this.pack();
	}

	@Override
	public void addComponents() {
		addComponent(this, toFollowTextField, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addComponent(this, followUserButton, 1, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addComponent(this, currentFollowingTextArea, 0, 1, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addComponent(this, tweetMessageScrollPane, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addComponent(this, postTweetButton, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addComponent(this, newsFeedScrollPane, 0, 3, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
	}

	public void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill) {
		Insets insets = new Insets(0, 0, 0, 0);
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0,
            anchor, fill, insets, 0, 0);
        container.add(component, gbc);
    }

	@Override
	public void defineComponents() {
		constraints = new GridBagConstraints();
        constraints.ipady = 100;

		toFollowTextField = new JTextField("User ID");
        followUserButton = new JButton("Follow User");
        initializeFollowUserButtonActionListener();

        currentFollowingTextArea = new JTextArea("Current Following: ");
        formatTextArea(currentFollowingTextArea);
        currentFollowingScrollPane = new JScrollPane(currentFollowingTextArea);
        currentFollowingScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        tweetMessageTextArea = new JTextArea("Tweet Message");
        tweetMessageScrollPane = new JScrollPane(tweetMessageTextArea);
        tweetMessageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        postTweetButton = new JButton("Post Tweet");
        initializePostTweetButtonActionListener();

        newsFeedTextArea = new JTextArea("News Feed: ");
        formatTextArea(newsFeedTextArea);
        newsFeedScrollPane = new JScrollPane(newsFeedTextArea);
        newsFeedScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // current following and news feed lists reflect most recent state of UserViewPanel
        updateCurrentFollowingTextArea();

        // news feed is updated even while UserViewPanel is closed
        updateNewsFeedTextArea();
	}

	private void formatTextArea(JTextArea textArea) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setRows(8);
        textArea.setEditable(false);
    }

	private void updateNewsFeedTextArea() {
        String list = "News Feed: \n";

        for (String news : ((User) user).getFeed()) {
            list += " - " + news + "\n";
        }

        // show most recent message at top of news feed
        newsFeedTextArea.setText(list);
        newsFeedTextArea.setCaretPosition(0);
    }

	private void updateCurrentFollowingTextArea() {
        String list = "Current Following: \n";
        for (String following : ((User) user).getFollowing().keySet()) {
            list += " - " + following + "\n";
        }
        currentFollowingTextArea.setText(list);
        currentFollowingTextArea.setCaretPosition(0);
    }

	private void initializePostTweetButtonActionListener() {
        postTweetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((User) user).postTweet(tweetMessageTextArea.getText());

                for (JFrame panel : openViews.values()) {
                    ((ViewUser) panel).updateNewsFeedTextArea();
                }
            }
        });
    }

	private void initializeFollowUserButtonActionListener() {
        followUserButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Client toFollow = (Client) everyone.get(toFollowTextField.getText());

                if (!everyone.containsKey(toFollowTextField.getText())) {
                    InfoDialogueBox dialogBox = new InfoDialogueBox("Error!",
                            "User does not exist!",
                            JOptionPane.ERROR_MESSAGE);

                } else if (toFollow.getClass() == Group.class) {
                    InfoDialogueBox dialogBox = new InfoDialogueBox("Error!",
                            "Cannot follow a group!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (everyone.containsKey(toFollowTextField.getText())) {
                    ((Observed) toFollow).attach((Observer) user);
                }

                // show current following as list
                updateCurrentFollowingTextArea();
            }
        });
    }
}
