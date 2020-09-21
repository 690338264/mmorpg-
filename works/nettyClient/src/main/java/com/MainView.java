package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    public static final JButton PLAYER_INFO = new JButton("人物");
    public static final JButton EQUIP_INFO = new JButton("装备");
    public static final JButton QUEST = new JButton("任务");
    public static final JButton AOI = new JButton("AOI");
    public static final JButton BAG = new JButton("背包");
    public static final JButton FRIEND = new JButton("好友");
    public static final JButton SHOP = new JButton("商店");
    public static final JButton DUNGEON = new JButton("副本");
    public static final JButton SECT = new JButton("公会");
    public static final JButton EMAIL = new JButton("收件箱");
    public static final JButton FIXED_PRICE = new JButton("一口价");
    public static final JButton COMPETITION_PRICE = new JButton("竞拍");
    public static final JButton MAP = new JButton("地图");
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
        final JScrollPane jsp2 = new JScrollPane(INPUT);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jsp2.setBounds(0, 675, 600, 90);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollPane jsp3 = new JScrollPane(CMD);

        jsp3.setBounds(610, 0, 300, 765);
        PLAYER_INFO.setBounds(0, 605, 70, 30);
        EQUIP_INFO.setBounds(75, 605, 70, 30);
        QUEST.setBounds(150, 605, 70, 30);
        AOI.setBounds(225, 605, 70, 30);
        BAG.setBounds(300, 605, 70, 30);
        FRIEND.setBounds(375, 605, 70, 30);
        SHOP.setBounds(450, 605, 70, 30);
        DUNGEON.setBounds(525, 605, 70, 30);
        SECT.setBounds(0, 640, 80, 30);
        EMAIL.setBounds(85, 640, 80, 30);
        FIXED_PRICE.setBounds(170, 640, 80, 30);
        COMPETITION_PRICE.setBounds(255, 640, 80, 30);
        MAP.setBounds(340, 640, 80, 30);
        for (final Cmd cmd : Cmd.values()) {
            CMD.append(cmd.getCmdId() + " " + cmd.getCmd() + "\n");
        }
        PLAYER_INFO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(8888 + NEW_LINE_STRING);
            }
        });

        EQUIP_INFO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(7777 + NEW_LINE_STRING);
            }
        });

        QUEST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(10230 + NEW_LINE_STRING);
            }
        });

        AOI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(1005 + NEW_LINE_STRING);
            }
        });

        BAG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(1010 + NEW_LINE_STRING);
            }
        });

        FRIEND.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(10220 + NEW_LINE_STRING);
            }
        });

        SHOP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(1012 + NEW_LINE_STRING);
            }
        });

        DUNGEON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(1017 + NEW_LINE_STRING);
            }
        });

        SECT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(1020 + NEW_LINE_STRING);
            }
        });

        EMAIL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(1015 + NEW_LINE_STRING);
            }
        });

        FIXED_PRICE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(10210 + NEW_LINE_STRING);
            }
        });

        COMPETITION_PRICE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(10211 + NEW_LINE_STRING);
            }
        });

        MAP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EchoClient.channel.writeAndFlush(1007 + NEW_LINE_STRING);
            }
        });
        //把滚动条添加到容器里面
        this.add(JSP_1);
        this.add(jsp2);
        this.add(jsp3);
        this.add(PLAYER_INFO);
        this.add(EQUIP_INFO);
        this.add(QUEST);
        this.add(AOI);
        this.add(BAG);
        this.add(FRIEND);
        this.add(SHOP);
        this.add(DUNGEON);
        this.add(SECT);
        this.add(EMAIL);
        this.add(FIXED_PRICE);
        this.add(COMPETITION_PRICE);
        this.add(MAP);
        this.setSize(950, 820);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
