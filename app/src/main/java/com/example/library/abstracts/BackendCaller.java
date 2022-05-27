package com.example.library.abstracts;

import com.example.library.callbacks.Callback;
import com.example.library.callbacks.RequestCallback;
import com.example.library.models.Book;
import com.example.library.models.Staff;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BackendCaller {

    private static BackendCaller instance = new BackendCaller();

    private HttpURLConnection connection;

    private Thread caller;

    private ConcurrentLinkedQueue<RequestCallback> tasks;

    private BackendCaller(){
        this.tasks = new ConcurrentLinkedQueue<>();
        caller = new Thread(() -> {
            while(true){
                if(tasks.isEmpty()){
                    continue;
                }
                tasks.poll().call();
            }
        });
        caller.start();
    }

    public static BackendCaller inst(){
        return instance;
    }

    private String request(String path){
        String output = "";

        try {
            URL url = new URL("http://192.168.3.182:8080/" + path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if(status < 300){
                Scanner scanner = new Scanner(connection.getInputStream());
                while(scanner.hasNextLine()){
                    output += scanner.nextLine();
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    private String post(String path, String body){
        try {
            URL url = new URL("http://192.168.3.182:8080/" + path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try(OutputStream os = connection.getOutputStream()){
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void loginCustomer(String username, String password, Callback<Staff> callback) {
        tasks.add(() -> {
            String data = request("api/employees/login?username=" + username + "&password=" + password);
            Staff s = null;
            try{
                JSONObject object = new JSONObject(data);
                s = new Staff(
                        object.getInt("employee_id"),
                        object.getString("first_name"),
                        object.getString("last_name"),
                        object.getString("username"),
                        object.getString("password"),
                        object.getString("role")
                );
            }catch(Exception e){

            }
            StaffDetails.inst().setStaff(s);
            callback.call(s);
        });
    }

    public void getBookInformation(String isbn, Callback<Book> callback){
        tasks.add(() -> {
            String data = request("api/google/isbn/" + isbn);
            Book book = null;
            try {
                JSONObject object = new JSONObject(data);
                book = new Book(
                        object.getString("title"),
                        object.getString("isbn"),
                        object.getString("published"),
                        object.getString("image")
                );
            }catch(Exception e){

            }
            callback.call(book);
        });
    }

    public void addBook(String isbn, Callback<Boolean> callback){
        tasks.add(() -> {
            JSONObject object = null;
            try {
                object = new JSONObject();
                object.put("isbn", isbn);
            }catch(Exception e){

            }
            String data = post("api/book_details/add", object.toString());
            callback.call(Boolean.parseBoolean(data));
        });
    }
}
