package com.dmko.criminalintent.controller.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dmko.criminalintent.R;
import com.dmko.criminalintent.util.PictureUtils;

public class PhotoFragment extends DialogFragment {
    private static final String ARG_PHOTO_FILE_PATH= "photo_file_path";
    private ImageView photoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_photo, container, false);
        String photoFilePath = getArguments().getString(ARG_PHOTO_FILE_PATH);

        photoView = (ImageView) view.findViewById(R.id.crime_photo);
        Bitmap bitmap = PictureUtils.getScaledBitmap(photoFilePath, getActivity());
        photoView.setImageBitmap(bitmap);

        return view;
    }

    public static PhotoFragment newInstance(Context context, String photoFilePath) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PHOTO_FILE_PATH, photoFilePath);
        fragment.setArguments(bundle);
        return fragment;
    }
}
