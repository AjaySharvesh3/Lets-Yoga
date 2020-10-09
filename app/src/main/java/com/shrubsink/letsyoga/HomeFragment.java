package com.shrubsink.letsyoga;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    CircleImageView mProfilePicture;
    TextView mUsernameTv;
    FirebaseAuth mFirebaseAuth;
    GoogleSignInClient googleSignInClient;

    public HomeFragment() {}


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mProfilePicture = view.findViewById(R.id.profile_image_iv);
        mUsernameTv = view.findViewById(R.id.username_tv);

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            Uri personPhoto = acct.getPhotoUrl();

            mUsernameTv.setText("Hello, " + personName);
            Glide.with(this).load(personPhoto).placeholder(R.drawable.profile_placeholder).into(mProfilePicture);
        }

        return view;
    }
}