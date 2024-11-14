package org.example.controllers;

import org.example.daos.ConnectionDAO;

public class ConnectionController {
  private static ConnectionDAO connectionDAO;
  private static ConnectionController instance;

  public static ConnectionController getInstance(boolean isTesting){
      if(instance == null){
          instance = new ConnectionController();
          connectionDAO = ConnectionDAO.getInstance(isTesting);
      }
      return instance;
  }
}
