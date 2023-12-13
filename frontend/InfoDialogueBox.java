package frontend;

import javax.swing.JOptionPane;

public class InfoDialogueBox {

	public InfoDialogueBox(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

}
