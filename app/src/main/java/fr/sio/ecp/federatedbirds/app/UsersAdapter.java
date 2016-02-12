package fr.sio.ecp.federatedbirds.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 24/11/2015.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MessageViewHolder> {

    private List<User> mUsers;

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        final User user = mUsers.get(position);




        Picasso.with(holder.mAvatarView.getContext())
                .load(user.avatar)
                .into(holder.mAvatarView);

        holder.mUsernameView.setText(user.login);



        holder.mFollowB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new FollowTask(v.getContext(), user.id, true)
                );
                Toast.makeText(v.getContext(), R.string.follow, Toast.LENGTH_SHORT).show();
            }
        });

        holder.mUnfollowB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new FollowTask(v.getContext(), user.id, false)
                );
                Toast.makeText(v.getContext(), R.string.unfollow, Toast.LENGTH_SHORT).show();
            }
        });

        holder.mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),UserDetailsActivity.class);
                intent.putExtra("user", new Gson().toJson(user));
                v.getContext().startActivity(intent);
            }
        });
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvatarView;
        private TextView mUsernameView;
        private ImageButton mFollowB;
        private ImageButton mUnfollowB;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mFollowB = (ImageButton) itemView.findViewById(R.id.follow);
            mUnfollowB = (ImageButton) itemView.findViewById(R.id.unfollow);
        }

    }



    private class FollowTask extends AsyncTask<Void, Void, User> {

        private Context mContext;
        private long mFollowingId;
        private boolean mFollow;

        public FollowTask(Context context, long following_id, boolean follow){
            mContext = context;
            mFollowingId = following_id;
            mFollow = follow;
        }

        @Override
        protected User doInBackground(Void... params) {
            try {
                return  ApiClient.getInstance(mContext).setFollowRelationship(mFollowingId, mFollow);
            } catch (IOException e) {
                Log.e(UsersAdapter.class.getSimpleName(), "Failed", e);
                return null;
            }
        }
    }
}