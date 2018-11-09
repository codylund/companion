package com.codylund.companion;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CheckpointItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class CheckpointsRecyclerViewAdapter extends RecyclerView.Adapter<CheckpointsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = CheckpointsRecyclerViewAdapter.class.toString();

    private List<CheckpointItem> mPendingCheckpoints;
    private OnListFragmentInteractionListener mListener;

    public CheckpointsRecyclerViewAdapter(Object object) {
        if (object instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) object;
        } else {
            throw new RuntimeException(object.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public void setPendingCheckpoints(List<CheckpointItem> checkpointsItems) {
        if (checkpointsItems != null)
            mPendingCheckpoints = checkpointsItems;
        else
            mPendingCheckpoints = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setNewPendingCheckpoints(List<CheckpointItem> checkpointItems) {
        if (checkpointItems == null)
            checkpointItems = new ArrayList<>();

        final CheckpointDiffCallback diffCallback = new CheckpointDiffCallback(mPendingCheckpoints, checkpointItems);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mPendingCheckpoints.clear();
        this.mPendingCheckpoints.addAll(checkpointItems);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_checkpoints, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d(TAG, "Binding to ViewHolder " + holder.toString());
        if (mPendingCheckpoints != null) {
            holder.mItem = mPendingCheckpoints.get(position);
            holder.mTimeView.setText(mPendingCheckpoints.get(position).getTime());
            double lat = holder.mItem.getLat();
            double lng = holder.mItem.getLng();
            String loc = "(" + String.valueOf(lat) + ", " + String.valueOf(lng) + ")";
            holder.mLocationView.setText(loc);

            // Only set click listeners if this checkpoint hasn't been shared yet...
            if (!holder.mItem.getShared()) {
                holder.mProgressTextView.setText("Tap to share.");
                holder.mView.setOnClickListener(holder);
                holder.mView.setOnLongClickListener(holder);
            } else {
                holder.mProgressTextView.setText("Successfully shared.");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mPendingCheckpoints != null) {
            return mPendingCheckpoints.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        public final TextView mTimeView;
        public final TextView mLocationView;
        public final TextView mProgressTextView;
        public final ProgressBar mProgressBar;

        public CheckpointItem mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTimeView = view.findViewById(R.id.time);
            mLocationView = view.findViewById(R.id.location);
            mProgressTextView = view.findViewById(R.id.status);
            mProgressBar = view.findViewById(R.id.progress);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationView.getText() + "'";
        }

        @Override
        public void onClick(final View v) {
            // For now, this view is no longer clickable
            v.setClickable(false);

            // Set the initial progress text before making the request
            mProgressTextView.setText("Sharing checkpoint...");
            mProgressBar.setVisibility(View.VISIBLE);

            mListener.onCheckpointShare(mItem, new PostCheckpointShareListener() {
                @Override
                public void onError() {
                    v.setClickable(true);
                    mProgressTextView.setText("Coudln't share. Try again later!");
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess() {
                    v.setClickable(false);
                    mProgressTextView.setText("Successfully shared.");
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onCheckpointDelete(mItem);
            return false;
        }

    }

    public interface OnListFragmentInteractionListener {
        void onCheckpointShare(final CheckpointItem checkpointItem, final PostCheckpointShareListener listener);
        void onCheckpointDelete(final CheckpointItem checkpointItem);
    }

    public interface PostCheckpointShareListener {
        void onError();
        void onSuccess();
    }
}
