package fr.sio.ecp.federatedbirds;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.Message;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 24/11/2015.
 */
public class ApiClient {

    /*
    private static final String API_BASE = "http://10.0.2.2:9000/";
    */
    private static final String API_BASE = "http://192.168.1.23:8080/";
    private static ApiClient mInstance;
    private Context mContext;

    private ApiClient(Context context) {
        mContext = context.getApplicationContext();
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiClient(context);
        }
        return mInstance;
    }

    private <T> T get(String path, Type type) throws IOException {
        return method("GET", path, null, type);
    }

    private <T> T post(String path, Object body, Type type) throws IOException {
        return method("POST", path, body, type);
    }

    private <T> T method(String method, String path, Object body, Type type) throws IOException {
        String url = API_BASE + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        // Correct the output error
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        String token = TokenManager.getUserToken(mContext);
        if (token != null) {
            connection.addRequestProperty("Authorization", "Bearer " + token);
        }
        if (body != null) {
            Writer writer = new OutputStreamWriter(connection.getOutputStream());
            try {
                new Gson().toJson(body, writer);
            } finally {
                writer.close();
            }
        }
        Reader reader = new InputStreamReader(connection.getInputStream());
        try {
            return new Gson().fromJson(reader, type);
        } finally {
            reader.close();
        }
    }

    public List<Message> getMessages(Long userId) throws IOException {
        TypeToken<List<Message>> type = new TypeToken<List<Message>>() {};
        String path = userId == null ? "messages" : "messages?author=" + userId;
        return get(path, type.getType());
    }

    public User getUser(long id) throws IOException {
        return get("users/" + id, User.class);
    }

    public List<User> getUserFollowed(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        //return get("users", type.getType());
        return get("users/" + id + "/followed", type.getType());
    }

    // Added by SNenon
    public List<User> getUserFollowers(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        //return get("users", type.getType());
        return get("users/" + id + "/followers", type.getType());
    }

    public String login(String login, String password) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        return post("auth/token", body, String.class);
    }

    // Added by SNenon
    public String register(String login, String password, String email) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        body.addProperty("email", email);
        return post("users", body, String.class);
    }

    public Message postMessage(String text) throws IOException {
        Message message = new Message();
        message.text = text;
        return post("messages", message, Message.class);
    }

    public User setFollowRelationship(Long followId, boolean bFollow) throws IOException {
        if (bFollow) {
            return post("users/" + Long.toString(followId) + "?followed=true", null, User.class);
        }
        else {
            return post("users/" + Long.toString(followId) + "?followed=false", null, User.class);
        }
    }

    public User uploadAvatar(Bitmap bitmap) throws IOException {
        // we start from the hypothesis that the picture is sent in Json using the following format:
        // {"picture":Base64 encoded picture}
        // So, we first need to encode the bitmap in base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageData = Base64.encodeToString(byteArray, Base64.DEFAULT);
        // Now we create the json body for the request
        JsonObject body = new JsonObject();
        body.addProperty("picture", imageData);
        // then, second hypothesis, the server uses a dedicated servlet named "avatar"
        // who returns an updated user in json
        return post("avatar", body, User.class);
    }

}
