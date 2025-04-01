package org.example.connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.frame.Frame;
import org.example.objects.GraphicObject;
import org.example.objects.ObjectList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

public class Connection implements ConnectionListener {

    private final XmlMapper xmlMapper;

    private Thread thread;

    private final Frame frame;

    private ObjectList objectList;

    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    private DataOutputStream outStream;
    private DataInputStream inStream;

    private ServerSocket serverSocket;

    private Socket socket;

    private boolean isServer;

    public Connection(boolean isServer) {
        this.xmlMapper = new XmlMapper();
        objectList = new ObjectList();
        frame = new Frame(this, isServer);
        this.isServer = isServer;
        createThread();
    }

    private void createThread() {
        thread = new Thread(() -> {
            try {
                if (isServer) {
                    serverSocket = new ServerSocket(PORT);
                    socket = serverSocket.accept();

                } else {
                    socket = new Socket(HOST, PORT);
                }

                outStream = new DataOutputStream(socket.getOutputStream()); // для записи
                inStream = new DataInputStream(socket.getInputStream()); // для чтения

                while (true) {
                    String inputString = inStream.readUTF();
                    if (inputString.equals("reset")) {
                        inStream.close();
                        outStream.close();
                        if (serverSocket != null) serverSocket.close();
                        if (socket != null) socket.close();

                        if (isServer) Thread.sleep(1000);
                        isServer = !isServer;
                        frame.changeCheckBox(isServer);                                             
                        frame.editTextLabel(isServer);

                        createThread();

                    } else if (inputString.equals("close")) {
                        close();
                    } else {
                        objectList.add(deserializeXML(inputString));
                        frame.repaintDrawingPanel();
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    private String serializeXML() {
        String xmlObjects;
        try {
            xmlObjects = xmlMapper.writeValueAsString(objectList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(xmlObjects);
        return xmlObjects;
    }


    private Set<GraphicObject> deserializeXML(String serializedObjects) {
        ObjectList list = new ObjectList();
        try {
            list = xmlMapper.readValue(serializedObjects, ObjectList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return list.getObjects();
    }

    @Override
    public void addButtonAction() {
        objectList.generateFigure();
        frame.repaintDrawingPanel();
    }

    @Override
    public void sendButtonAction() {
        if (objectList.isEmpty()) {
            return;
        }
        sendObjects();
    }

    private void sendObjects() {
        try {
            outStream.writeUTF(serializeXML());
            outStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearButtonAction() {
        objectList.clear();
        frame.repaintDrawingPanel();
    }

    @Override
    public void closeButtonAction() {
        try {
            outStream.writeUTF("close");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        close();
    }

    private void close() {
        try {
            frame.dispose();
            inStream.close();
            outStream.close();
            socket.close();
            serverSocket.close();
            thread.interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectList getObjectList() {
        return objectList;
    }

    @Override
    public void sendResetConnectionMessage() {
        try {
            outStream.writeUTF("reset");

            inStream.close();
            outStream.close();
            if (serverSocket != null) serverSocket.close();
            if (socket != null) socket.close();

            if (isServer) Thread.sleep(1000);
            isServer = !isServer;
            createThread();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

