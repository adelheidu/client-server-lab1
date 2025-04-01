package org.example.connection;

import org.example.objects.ObjectList;

public interface ConnectionListener {

    void addButtonAction();
    void sendButtonAction();
    void clearButtonAction();
    void closeButtonAction();
    ObjectList getObjectList();
    void sendResetConnectionMessage();
}
