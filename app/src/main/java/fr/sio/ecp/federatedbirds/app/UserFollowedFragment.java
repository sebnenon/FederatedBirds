package fr.sio.ecp.federatedbirds.app;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

import fr.sio.ecp.federatedbirds.model.User;

// This class is just an extension of UsersFragment, adapted for the followed


public class UserFollowedFragment extends UsersFragment implements LoaderManager.LoaderCallbacks<List<User>> {


    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new FollowedLoader(getContext(), null);
    }


}
