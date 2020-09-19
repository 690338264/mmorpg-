package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Catherine
 * @create 2020-09-19 00:47
 */
public class MainView extends JFrame {
    public static final JTextArea OUTPUT = new JTextArea();
    public static final JScrollPane JSP_1 = new JScrollPane(OUTPUT);
    public static final JTextArea INPUT = new JTextArea();
    public static final JTextArea CMD = new JTextArea();

    private static final char NEW_LINE_CHARACTER = '\n';
    private static final String NEW_LINE_STRING = "\n";

    public MainView() {
        this.setLayout(null);

        OUTPUT.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 20));
        OUTPUT.setLineWrap(true);

        INPUT.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 20));
        CMD.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));

        INPUT.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // NOOP
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == NEW_LINE_CHARACTER) {
                    String text = INPUT.getText().replaceAll(NEW_LINE_STRING, "");
                    OUTPUT.append(text + NEW_LINE_STRING);
                    EchoClient.channel.writeAndFlush(text + NEW_LINE_STRING);
                    INPUT.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == NEW_LINE_CHARACTER) {
                    INPUT.setCaretPosition(0);
                }
            }
        });
        CMD.setBackground(Color.getHSBColor(120, 25, 120));

        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        JSP_1.setBounds(0, 0, 600, 600);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        JSP_1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //在文本框上添加滚动条
        JScrollPane jsp2 = new JScrollPane(INPUT);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jsp2.setBounds(0, 610, 600, 90);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollPane jsp3 = new JScrollPane(CMD);
        jsp3.setBounds(610, 0, 300, 700);
        for (Cmd cmd : Cmd.values()) {
            CMD.append(cmd.getCmdId() + " " + cmd.getCmd() + "\n");
        }


        //把滚动条添加到容器里面
        this.add(JSP_1);
        this.add(jsp2);
        this.add(jsp3);
        this.setSize(950, 760);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
