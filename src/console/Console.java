package console;

import javax.swing.*;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Console implements KeyListener {

    private JFrame frame;
    private JPanel container;
    private JTextArea text_area;
    private JTextArea text_field;
    private JScrollPane scrollPane;
    private boolean input = false, happened = false, fullscreen = false, first = true;

    private int count_lines()
    {

        return text_area.getLineCount()+RXTextUtilities.getWrappedLines(text_area);
    }

    public Console (int x, int y, int width, int height, Color background, Color foreground) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Console");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0,0));
        frame.setBackground(Color.BLACK);
        frame.pack();
        frame.setBounds(x,y,width,height);

        container = new JPanel();
        container.setSize(frame.getWidth(), 2000);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        text_area = new JTextArea();
        text_area.setEditable(false);
        text_area.setBorder(null);
        text_area.setBackground(background);
        text_area.setForeground(foreground);
        text_area.append("Console Ver. 1.0\r\n");
        text_area.setLineWrap(true);
        text_area.addKeyListener(this);
        text_area.setPreferredSize(new Dimension(container.getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*text_area.getLineCount()));
        container.add(text_area);

        text_field = new JTextArea();
        text_field.setBackground(background);
        text_field.setForeground(foreground);
        text_field.setCaretColor(foreground);
        text_field.setBorder(null);
        text_field.setLineWrap(true);
        text_field.addKeyListener(this);
        text_field.setPreferredSize(new Dimension(container.getWidth(), 2000));
        container.add(text_field);

        scrollPane = new JScrollPane(container);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
        container.setPreferredSize(new Dimension(frame.getWidth()-scrollPane.getVerticalScrollBar().getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*count_lines()+text_field.getHeight()));
        text_area.setPreferredSize(new Dimension(container.getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*text_area.getLineCount()));
        text_field.setPreferredSize(new Dimension(container.getWidth(), 2000));
    }

    public void outln (String message) {
        String regex = "\\s+\n";
        message = message.replaceAll(regex, "\r\n");
        if (first) {
            text_area.append(message);
            first = false;
        } else
            text_area.append("\r\n"+message);
        text_area.setPreferredSize(new Dimension(container.getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*count_lines()));
        text_field.setPreferredSize(new Dimension(container.getWidth(), 2000));
        container.setPreferredSize(new Dimension(container.getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*count_lines()+text_field.getHeight()));
        scrollPane.setPreferredSize(new Dimension(container.getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*count_lines()+text_field.getHeight()));
        scrollPane.getVerticalScrollBar().setValue(text_area.getHeight()-
                text_area.getFontMetrics(text_area.getFont()).getHeight()*8);
    }

    public void out (String message) {
        String regex = "\\s+\n";
        message = message.replaceAll(regex, "\n");
        text_area.append(message);
        text_area.setPreferredSize(new Dimension(container.getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*count_lines()));
        text_field.setPreferredSize(new Dimension(container.getWidth(), 2000));
        container.setPreferredSize(new Dimension(container.getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*count_lines()+text_field.getHeight()));
        scrollPane.setPreferredSize(new Dimension(container.getWidth(),
                text_area.getFontMetrics(text_area.getFont()).getHeight()*count_lines()+text_field.getHeight()));
        scrollPane.getVerticalScrollBar().setValue(text_area.getHeight()-
                text_area.getFontMetrics(text_area.getFont()).getHeight()*8);
    }

    public String get_input () throws InterruptedException {
        input = true;
        while (!happened) Thread.sleep(100);
        input = false;
        happened = false;
        String text = text_field.getText();
        text_field.setText("");
        return text;
    }

    public void set_fullscreen () {
        fullscreen = true;
        frame.dispose();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    public void maximize () {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if (input) {
                e.consume();
                happened = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (fullscreen) {
                fullscreen = false;
                frame.dispose();
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setUndecorated(false);
                frame.setVisible(true);
            }
            else {
                set_fullscreen();
            }
        }
    }

    // Unused
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}
