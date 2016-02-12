package fr.sio.ecp.federatedbirds.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.Message;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by snenon on 22/01/16.
 */
// TODO comment

public class UserDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Message>> {

    private static final int LOADER_MESSAGES = 0;

    private MessagesAdapter mMessagesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User usr = new Gson().fromJson(getIntent().getExtras().getString("user"), User.class);
        final String USRNAME = usr.login;

        setContentView(R.layout.activity_userdetails);

        RecyclerView listView = (RecyclerView) findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        // false means no avatar in message
        mMessagesAdapter = new MessagesAdapter(false);
        listView.setAdapter(mMessagesAdapter);

        TextView userNameView = (TextView) findViewById(R.id.username);
        userNameView.setText(usr.login);

        ImageView userAvatarView = (ImageView) findViewById(R.id.avatar);
        Picasso.with(userAvatarView.getContext())
                .load(usr.avatar)
                .into(userAvatarView);

        String currentUser = TokenManager.getAuthenticatedUser(getApplicationContext());
        if (currentUser.equals(USRNAME)) {
            userAvatarView.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent intent = new Intent(v.getContext(), UploadAvatarActivity.class);
                                                      v.getContext().startActivity(intent);
                                                  }
                                              }
            );
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().initLoader(
                LOADER_MESSAGES,
                getIntent().getExtras(),
                this
        );
    }

    @Override
    public Loader<List<Message>> onCreateLoader(int id, Bundle args) {
        User usr = new Gson().fromJson(getIntent().getExtras().getString("user"), User.class);
        return new MessagesLoader(getApplicationContext(), usr.id);
    }

    @Override
    public void onLoadFinished(Loader<List<Message>> loader, List<Message> messages) {
        mMessagesAdapter.setMessages(messages);
    }

    @Override
    public void onLoaderReset(Loader<List<Message>> loader) {

    }

}




