package org.example.frame;

import org.example.connection.ConnectionListener;
import org.example.objects.Oval;
import org.example.objects.Rectangle;
import org.example.objects.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {

    JPanel buttonPanel;
    JPanel drawingPanel;

    JButton addButton;
    JButton sendButton;
    JButton clearButton;
    JButton closeButton;

    JCheckBox checkBox;
    JLabel label;

    private ConnectionListener connectionListener;


    public Frame (ConnectionListener connectionListener, boolean isServer) {

        super("Разработка клиент-серверных приложений");

        this.connectionListener = connectionListener;
        initialize(isServer);

    }

    private void initialize(boolean isServer) {
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        addButton = new JButton("Добавить");
        sendButton = new JButton("Отправить");
        clearButton = new JButton("Удалить");
        closeButton = new JButton("Закрыть");

        label = new JLabel();
        editTextLabel(isServer);
        buttonPanel.add(label);

        checkBox = new JCheckBox("Сервер", isServer);
        checkBox.setBackground(Color.WHITE);
        buttonPanel.add(checkBox);

        buttonPanel.add(addButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);

        onAddButtonClick();
        onSendButtonClick();
        onClearButtonClick();
        onCloseButtonClick();
        onCheckBoxClick();

        addDrawingPanel();

        add(buttonPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void addDrawingPanel() {
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                connectionListener.getObjectList().getObjects().forEach(object -> {
                    if (object instanceof Rectangle) drawRectangle(g, (Rectangle) object);
                    else if (object instanceof Oval) drawOval(g, (Oval) object);
                    else if (object instanceof Triangle) drawTriangle(g, (Triangle) object);
                });
            }
        };
        drawingPanel.setBackground(Color.WHITE);
    }

    private void drawRectangle(Graphics g, Rectangle rect) {
        g.setColor(Color.PINK);
        g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    private void drawOval(Graphics g, Oval oval) {
        g.setColor(Color.ORANGE);
        g.fillOval(oval.getX(), oval.getY(), oval.getRadiusX(), oval.getRadiusY());
    }

    private void drawTriangle(Graphics g, Triangle triangle) {
        g.setColor(Color.CYAN);
        g.fillPolygon(new int[]{triangle.getX(), triangle.getX1(), triangle.getX2()}, new int[]{triangle.getY(), triangle.getY1(), triangle.getY2()}, 3);
    }


    private void onAddButtonClick() {

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectionListener.addButtonAction();
            }
        });

    }

    private void onSendButtonClick() {

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectionListener.sendButtonAction();
            }
        });

    }

    private void onClearButtonClick() {

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectionListener.clearButtonAction();
            }
        });

    }

    private void onCloseButtonClick() {

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectionListener.closeButtonAction();
            }
        });
    }

    private void onCheckBoxClick() {
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTextLabel(checkBox.isSelected());
                connectionListener.sendResetConnectionMessage();
            }
        });
    }

    public void repaintDrawingPanel() {
        drawingPanel.repaint();
    }

    public void changeCheckBox(boolean isServer) {
        checkBox.setSelected(isServer);
    }

    public void editTextLabel(boolean isServer) {
        if (isServer) label.setText("Сервер");
        else label.setText("Клиент");
    }

}
