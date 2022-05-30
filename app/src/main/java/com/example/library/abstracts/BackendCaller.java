package com.example.library.abstracts;

import android.telecom.Call;

import com.example.library.callbacks.Callback;
import com.example.library.callbacks.RequestCallback;
import com.example.library.models.Book;
import com.example.library.models.CopyItem;
import com.example.library.models.Library;
import com.example.library.models.Staff;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void getLibraries(Callback<List<Library>> callback){
        tasks.add(() -> {
            String data = request("api/libraries");
            List<Library> output = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(data);
                for (int i = 0; array.length() > i; i++) {
                    JSONObject o = array.getJSONObject(i);
                    output.add(new Library(
                            o.getInt("library_id"),
                            o.getString("name"),
                            o.getString("address"),
                            o.getString("county")
                    ));
                }
            }catch(Exception e){

            }
            callback.call(output);
        });
    }

    public void addBook(String isbn, Callback<Boolean> callback){
        tasks.add(() -> {
            JSONObject body = new JSONObject();
            try{
                body.put("isbn", isbn);
            }catch(Exception e){

            }
           String data = post("api/book_details/add", body.toString());
            callback.call(Boolean.parseBoolean(data));
        });
    }

    public void addCopies(String isbn, int libraryId, int amount, Callback<Boolean> callback){
        tasks.add(() -> {
            JSONObject body = new JSONObject();
            try {
                body.put("isbn", isbn);
                body.put("library_id", libraryId);
                body.put("amount", amount);
            }catch(Exception e){

            }
            String data = post("api/books/add_multiple", body.toString());
            callback.call(Boolean.parseBoolean(data));
        });
    }

    public void getCopiesInLibrary(int libraryId, String isbn, Callback<List<CopyItem>> callback){
        tasks.add(() -> {
            String data = request("api/books/copies_in_library?library_id=" + libraryId + "&isbn=" + isbn);
            List<CopyItem> output = new ArrayList<>();
            try{
                JSONArray array = new JSONArray(data);
                for (int i = 0; array.length() > i; i++) {
                    JSONObject o = array.getJSONObject(i);
                    output.add(new CopyItem(
                       o.getInt("book_id"),
                       o.getInt("available") == 1
                    ));
                }
            }catch(Exception e){

            }
            callback.call(output);
        });
    }
}
