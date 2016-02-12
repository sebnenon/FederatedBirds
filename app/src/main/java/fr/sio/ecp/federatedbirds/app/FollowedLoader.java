package fr.sio.ecp.federatedbirds.app;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 30/11/2015.
 * No changes here
 */
public class FollowedLoader extends UsersLoader {

    public FollowedLoader(Context context, Long userId) {
        super(context, userId);
    }

    @Override
    protected List<User> getUsers(Long userId) throws IOException {
        return ApiClient.getInstance(getContext()).getUserFollowed(userId);
    }

}