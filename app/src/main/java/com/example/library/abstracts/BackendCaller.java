package com.example.library.abstracts;

import com.example.library.callbacks.Callback;
import com.example.library.callbacks.RequestCallback;
import com.example.library.models.Staff;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
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

    public void loginCustomer(Callback<Staff> callback) {
        tasks.add(() -> {
            String data = request("api/employees/login?username=test&password=123");
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
            callback.call(s);
        });
    }
}
