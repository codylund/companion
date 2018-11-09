package com.codylund.companion;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewCheckpointFragment extends Fragment {

    private static final String TAG = NewCheckpointFragment.class.getName();

    // View related stuff
    private View mView;
    private EditText mText;
    private ArrayList<ImageView> mImageSet;
    private LinearLayout mImageViews;
    private TextView mNoImagesText;
    private TextView mProgressBarText;

    private String mSelectedImage;

    private CheckpointViewModel mCheckpointViewModel;
    private IToolbarController mToolbarController;

    public NewCheckpointFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewCheckpointFragment.
     */
    public static NewCheckpointFragment newInstance() {
        NewCheckpointFragment fragment = new NewCheckpointFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get reference to the persistent view model
        mCheckpointViewModel = ViewModelProviders.of(this).get(CheckpointViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_new_checkpoint, container, false);
        this.mView = view;

        // Share checkpoint button
        view.findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO move this into a viewmodel class

                // Make sure the provided checkpoint data is legit
                if (!validateCheckpoint()) {
                    Toast.makeText(NewCheckpointFragment.this.getActivity(), "Please attach text or an image.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Get the latest GPS position
                LatLngHelper.getLatestLatLng(getContext(), new LatLngHelper.IGetLatestLatLngCallback() {
                    @Override
                    public void returnLatestLatLng(Location location) {
                        if (location != null) {
                            Log.d(TAG, "Got latest GPS position: " + location.toString());

                            // Create the checkpoint object
                            CheckpointItem checkpointItem = new CheckpointItem(
                                    new Date().toString(),
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    NewCheckpointFragment.this.mText.getText().toString(),
                                    NewCheckpointFragment.this.mSelectedImage
                            );

                            // Insert the new checkpoint item
                            NewCheckpointFragment.this.mCheckpointViewModel.insert(checkpointItem);

                            // We are done with this fragment
                            NewCheckpointFragment.this.finish();
                        } else {
                            Log.e(TAG, "Failed to retrieve the GPS location.");
                            Toast.makeText(NewCheckpointFragment.this.getActivity(), "Failed to get GPS position!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // Add image button
        view.findViewById(R.id.buttonImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the image-picker activity
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });

        // Get reference to the important views
        mText = view.findViewById(R.id.text);
        mImageViews = view.findViewById(R.id.picCollection);
        mImageSet = new ArrayList<>();
        mNoImagesText = view.findViewById(R.id.noImagesText);
        mProgressBarText = view.findViewById(R.id.progress);

        // TODO Set the toolbar icons
        mToolbarController.setDisplayHome(true);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IToolbarController) {
            mToolbarController = (IToolbarController) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IToolbarController");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK && imageReturnedIntent != null){
            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                mSelectedImage = RealPathUtil.getRealPathFromURI_BelowAPI11(getContext(), imageReturnedIntent.getData());

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                mSelectedImage = RealPathUtil.getRealPathFromURI_API11to18(getContext(), imageReturnedIntent.getData());

                // SDK > 19 (Android 4.4)
            else
                mSelectedImage = RealPathUtil.getRealPathFromURI_API19(getContext(), imageReturnedIntent.getData());

            try {
                Uri pickedImage = imageReturnedIntent.getData();
                InputStream imageStream = getActivity().getContentResolver().openInputStream(pickedImage);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                final ImageView image = new ImageView(getActivity());
                image.setImageBitmap(selectedImage);
                image.setAdjustViewBounds(true);
                image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                mView.findViewById(R.id.noImagesText).setVisibility(View.GONE);
                ((LinearLayout) mView.findViewById(R.id.picCollection)).addView(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedImage = null;
                        ((LinearLayout) mView.findViewById(R.id.picCollection)).removeView(image);
                        if (((LinearLayout) mView.findViewById(R.id.picCollection)).getChildCount() == 1) {
                            mView.findViewById(R.id.noImagesText).setVisibility(View.VISIBLE);
                        }
                    }
                });
                mImageSet.add(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateCheckpoint() {
        return ((mSelectedImage != null && !mSelectedImage.isEmpty())
                || (mText.getText().toString() != null && !mText.getText().toString().isEmpty()));
    }

    private void finish() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
