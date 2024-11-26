package dk.connectly.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;
import java.util.Objects;

public class MongoDBConfig {
    static String URI = "mongodb://159.223.24.167:27017/";
    static String DB_NAME = "ConnectlyDB";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void establishTestConnection() {
        try (MongoClient mongoClient = MongoClients.create(URI)) {

            MongoDatabase database = mongoClient.getDatabase("Test");
            System.out.println("Connected to " + database.getName());

            database.getCollection("testCollection").insertOne(new Document("name", "testDocument").append("type", "example"));
            System.out.println("Document has been created");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTestFile() {
        try (MongoClient mongoClient = MongoClients.create(URI)) {

            MongoDatabase database = mongoClient.getDatabase("Test");
            System.out.println("Connected to " + database.getName());

            MongoCollection<Document> documentCollection = database.getCollection("testCollection");
            Document toBeFound = new Document("name", "testDocument");
            Document found = documentCollection.find(toBeFound).first();

            System.out.println(found.toJson());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Object getChatLogByUsers(String userEmail1, String userEmail2, int chatId) {
        return null;
    }

    public static Object updateChatLogByUsers(String userEmail1, String userEmail2, int chatId) {
        return null;
    }

    public static Object createChat(String userEmail1, String userEmail2) {
        try (MongoClient mongoClient = MongoClients.create(DB_NAME)) {
            
        }
    }

    public static Object createChatMessage(String authorEmail, String message, int id) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase("ConnectlyDB");

            MongoCollection<Document> documentCollection = database.getCollection("ChatService");
            Document toBeFound = documentCollection.find(new Document("_id", id)).first();

            if (toBeFound == null) {
                return objectMapper.createObjectNode().put("Error", "Couldn't find chat log");
            }

            toBeFound.append("author", authorEmail).append("Message", message).append("date", new Date());

            documentCollection.replaceOne(Objects.requireNonNull(documentCollection.find((new Document("_id", id))).first()), toBeFound);
            return toBeFound;


        }
    }

    public static void printDatabase() {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase("ConnectlyDB");

            MongoCollection<Document> documentCollection = database.getCollection("ChatService");
            for (Document d : documentCollection.find()) {
                System.out.println(d.toJson());
            }
        }
    }

    public static void dropDatabase() {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase("ConnectlyDB");
            database.drop();
        }
    }
}
