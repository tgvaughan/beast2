package beast.app.beauti;

import javax.swing.*;
import java.awt.*;

/**
 * Static methods for creating JOptionPanes that word-wrap the displayed text by default.
 * Resizing causes text to reflow and, in the case that the reflowed text exceeds the number
 * number of visible rows, causes a vertical scroll bar to appear.
 *
 * Created by Tim Vaughan <tgvaughan@gmail.com> on 7/04/17.
 */
public class WrappedOptionPane {

    /**
     * Width (approximate characters) at which text will wrap.
     */
    private static int DEFAULT_WIDTH = 50;

    private static Object getMsgObject(Component parent, Object message) {

        String messageStr;
        if (message instanceof String) {
            messageStr = (String) message;
        } else {
            if (message instanceof String[])
                messageStr = String.join("\n", (String[])message);
            else
                throw new IllegalArgumentException("Message must be of type String or String[].");
        }


        JTextArea textArea = new JTextArea(messageStr);

        textArea.setEditable(false);
        textArea.setColumns(DEFAULT_WIDTH);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        if (parent != null)
            textArea.setBackground(parent.getBackground());
        else
            textArea.setBackground(UIManager.getColor("Panel.background"));

        textArea.setSize(textArea.getPreferredSize());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        return scrollPane;
    }

    public static void showMessageDialog(Component parent, Object message, String title, int messageType, Icon icon) {
        JOptionPane.showMessageDialog(parent, getMsgObject(parent, message), title, messageType, icon);
    }

    public static void showMessageDialog(Component parent, Object message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, getMsgObject(parent, message), title, messageType);
    }

    public static void showMessageDialog(Component parent, Object message) {
        JOptionPane.showMessageDialog(parent, getMsgObject(parent, message));
    }
}
