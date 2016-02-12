package fr.sio.ecp.federatedbirds.app;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.model.Message;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 24/11/2015.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Message> mMessages;
    private boolean showAvatar = true;

    // here are two constructors allowing the use of a special boolean parameter to hide the avatar in a message.
    // this is a temporary hack that could strongly be improved
    public MessagesAdapter() {}

    public MessagesAdapter(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        final User author = message.user;

        holder.mTextView.setText(message.text);
        // Improved version of the avatar removal
        if(showAvatar){
            Picasso.with(holder.mUserAvatarView.getContext())
                    .load(message.user.avatar)
                    .into(holder.mUserAvatarView);


            holder.mUserAvatarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), UserDetailsActivity.class);
                    intent.putExtra("user", new Gson().toJson(author));
                    v.getContext().startActivity(intent);
                }
            });
        }else{
            holder.mUserAvatarView.setVisibility(View.GONE);
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mUserAvatarView;
        private TextView mTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mUserAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }

    }

}