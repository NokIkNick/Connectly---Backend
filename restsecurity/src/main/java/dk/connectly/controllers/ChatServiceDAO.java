package dk.connectly.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dk.connectly.exceptions.ApiException;
import org.bson.Document;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ChatServiceDAO {

    private static ChatServiceDAO instance;
    private static String URI = "mongodb://159.223.24.167:27017/";
    private static String DB_NAME = "ConnectlyDB";
    private static ObjectMapper objectMapper = new ObjectMapper();


    public static ChatServiceDAO getInstance(boolean isTesting) {
        if(instance == null){
            instance = new ChatServiceDAO();

            if(isTesting){
                DB_NAME = "Test";
            }

            String isDeployed = System.getenv("DEPLOYED");
            if(isDeployed != null){
                String MONGODB_URI = System.getenv("MONGODB_URI");
                if(MONGODB_URI != null){
                    URI = System.getenv("MONGODB_URI");
                }
            }
        }
        return instance;
    }

    private ChatServiceDAO (){
    }




    public void establishTestConnection() {
        try (MongoClient mongoClient = MongoClients.create(URI)) {

            MongoDatabase database = mongoClient.getDatabase("Test");
            System.out.println("Connected to " + database.getName());

            database.getCollection("testCollection").insertOne(new Document("name", "testDocument").append("type", "example"));
            System.out.println("Document has been created");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTestFile() {
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


    public Document fetchChat(String userEmail1, String userEmail2) throws ApiException {
        try (MongoClient mongoClient = MongoClients.create(URI)){
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> chats = database.getCollection("Chats");

            Document foundChat = chats.find(new Document("participants", new Document("$all", List.of(userEmail1, userEmail2)))).first();

            if(foundChat == null){
                System.out.println("No existing chat was found, creating chat now...");
                return createChat(userEmail1, userEmail2);
            }

            return foundChat;
        }
    }

    public Document fetchChatById(int id) throws ApiException {
        try (MongoClient mongoClient = MongoClients.create(URI)){
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> chats = database.getCollection("Chats");

            Document foundChat = chats.find(new Document("_id", id)).first();

            if(foundChat == null){
                throw new ApiException(500, "No chat was found with the id: "+id);
            }

            return foundChat;
        }
    }

    public Document createChat(String userEmail1, String userEmail2) throws ApiException {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> chats = database.getCollection("Chats");

            Document existingChat = chats.find(new Document("participants", new Document("$all", List.of(userEmail1, userEmail2)))).first();

            if(existingChat != null){
                System.out.println("Chat already exists, fetching chat now...");
                return fetchChat(userEmail1, userEmail2);
            }

            Document newChat = new Document()
                    .append("_id", chats.countDocuments() + 1)
                    .append("participants", List.of(userEmail1, userEmail2))
                    .append("created_at", new Date().toString())
                    .append("last_message_at", null);
            chats.insertOne(newChat);


            return newChat;
        } catch (Exception e) {
            throw new ApiException(500, "Error while creating chat: "+e.getMessage());
        }
    }

    public Document sendMessage(long chatId, String messenger, String messageContent) throws ApiException {
        try(MongoClient mongoClient = MongoClients.create(URI)){
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> messages = database.getCollection("Messages");
            MongoCollection<Document> chats = database.getCollection("Chats");

            Document newMessage = new Document()
                    .append("chat_id", chatId)
                    .append("messenger", messenger)
                    .append("message", messageContent)
                    .append("sent_at", new Date().toString());
            messages.insertOne(newMessage);

            chats.updateOne(new Document("_id", chatId),
                    new Document("$set", new Document("last_message_at", new Date().toString())));

            return newMessage;
        }catch(Exception e){
            throw new ApiException(500, "Error while sending message: "+e.getMessage());
        }
    }

    public List<Document> fetchMessagesByChatId(long chatId) throws ApiException {
        try(MongoClient mongoClient = MongoClients.create(URI)){
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> messages = database.getCollection("Messages");

            List<Document> messagesToReturn = new LinkedList<>();
            for (Document message : messages.find(new Document("chat_id", chatId))){
                messagesToReturn.add(message);
            }
            return messagesToReturn;

        }catch(Exception e){
            throw new ApiException(500, "Error while fetching messages for chatID: "+chatId+", "+e.getMessage());
        }
    }

    public List<Document> listChatsForUser(String userEmail) throws ApiException {
        try (MongoClient mongoClient = MongoClients.create(URI)){
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> chats = database.getCollection("Chats");

            List<Document> chatsToReturn = new LinkedList<>();
            for(Document chat : chats.find(new Document("participants", userEmail))){
                chatsToReturn.add(chat);
            }

            return chatsToReturn;
        }catch (Exception e){
            throw new ApiException(500, "Error while fetching chat list for user: "+userEmail+", "+e.getMessage());
        }
    }



    public void dropDatabase() {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            database.drop();
        }
    }
}
