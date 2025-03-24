package com.shoply.shoply_backend.utilities;

public interface DatabaseManager {
    void start();
    void stop();
    String getConnectionString();
}


