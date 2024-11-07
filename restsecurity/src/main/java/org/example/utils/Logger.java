package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.HttpStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {

    private static ObjectMapper mapper = new ObjectMapper();

    public synchronized static ObjectNode log(HttpStatus status, String message){
        ObjectNode node = mapper.createObjectNode();
        try(FileWriter fileWriter = new FileWriter("exceptionLog.txt", true)){
            fileWriter.write("status: "+ status.getCode());
            fileWriter.append("\n");
            fileWriter.write("message: "+message);
            fileWriter.append("\n");
            fileWriter.write("timestamp: "+ LocalDateTime.now().toString());
            fileWriter.append("\n");
            fileWriter.append("----------------------------------------------------------------------------------");
            fileWriter.append("\n");

            node.put("status", status.getCode());
            node.put("message", message);
            node.put("timestamp", LocalDateTime.now().toString());
            return node;
        }catch (IOException e){
            e.printStackTrace();
        }
        return node;
    }

    public synchronized static ObjectNode log(HttpStatus status, String message, String ip){
        ObjectNode node = mapper.createObjectNode();
        try(FileWriter fileWriter = new FileWriter("exceptionLog.txt", true)){
            fileWriter.write("status: "+ status.getCode());
            fileWriter.append("\n");
            fileWriter.write("message: "+message);
            fileWriter.append("\n");
            fileWriter.write("timestamp: "+ LocalDateTime.now().toString());
            fileWriter.append("\n");
            fileWriter.write("IP:: "+ ip);
            fileWriter.append("\n");
            fileWriter.append("----------------------------------------------------------------------------------");
            fileWriter.append("\n");

            node.put("status", status.getCode());
            node.put("message", message);
            node.put("timestamp", LocalDateTime.now().toString());
            node.put("IP:", ip);
            return node;
        }catch (IOException e){
            e.printStackTrace();
        }
        return node;
    }

    public static void consoleLog(String in){
        System.out.println(in);
    }
}