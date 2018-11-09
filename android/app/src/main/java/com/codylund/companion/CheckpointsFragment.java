package com.codylund.companion;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CheckpointsFragment extends Fragment implements CheckpointsRecyclerViewAdapter.OnListFragmentInteractionListener {

    // For logging purposes
    private static final String TAG = CheckpointsFragment.class.getName();

    // Member variables for the list of checkpoints
    private CheckpointViewModel mCheckpointViewModel;
    private RecyclerView mRecyclerView;
    private CheckpointsRecyclerViewAdapter mAdapter;
    private CheckpointsFragmentListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CheckpointsFragment() {
        Log.d(TAG, "Creating new fragment.");
    }

    public static CheckpointsFragment newInstance() {
        CheckpointsFragment fragment = new CheckpointsFragment();
        // We can set other fragment arguments in here if needed...
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a reference to the pending checkpoints view model
        mCheckpointViewModel = ViewModelProviders.of(getActivity()).get(CheckpointViewModel.class);

        // Observe updates to the pending checkpoints
        mCheckpointViewModel.getPendingCheckpoints().observe(getActivity(), new Observer<List<CheckpointItem>>() {
            @Override
            public void onChanged(@Nullable List<CheckpointItem> checkpointItems) {
                Log.d(TAG, "Pending checkpoints list changed. Displaying new list.");
                if (mAdapter != null)
                    mAdapter.setNewPendingCheckpoints(checkpointItems);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkpoints_list, container, false);

        // Get the latest list of pending checkpoints
        List<CheckpointItem> checkpointItems = mCheckpointViewModel.getAllCheckpoints().getValue();

        // Prepare the recycler view to display the pending checkpoints
        if (view.findViewById(R.id.list) instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = view.findViewById(R.id.list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new CheckpointsRecyclerViewAdapter(this);
            mAdapter.setPendingCheckpoints(checkpointItems);
            mRecyclerView.setAdapter(mAdapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                    layoutManager.getOrientation());
            mRecyclerView.addItemDecoration(dividerItemDecoration);
        }

        view.findViewById(R.id.addCheckpoint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showNewCheckpointWizard();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CheckpointsFragmentListener) {
            mListener = (CheckpointsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCheckpointShare(final CheckpointItem checkpointItem, final CheckpointsRecyclerViewAdapter.PostCheckpointShareListener listener) {
        mCheckpointViewModel.share(checkpointItem, new CheckpointRepository.OnShareCheckpointResult() {
            @Override
            public void onError() {
                Log.e(TAG, "Failed to share the checkpoint at " + checkpointItem.getTime() + ".");
                listener.onError();
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "Successfully shared the checkpoint at " + checkpointItem.getTime() + ".");
                listener.onSuccess();
            }
        });
    }

    @Override
    public void onCheckpointDelete(CheckpointItem checkpointItem) {

    }

    public interface CheckpointsFragmentListener {
        void showNewCheckpointWizard();
    }
}
