package net.along.dragonflyfm.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import net.along.dragonflyfm.R;

import java.util.List;


/**
 *
 * @author 陈泰龙
 * @date 2020/7/30
 */

public abstract class SimpleAdapter<T> extends RecyclerView.Adapter {

    List<T> items;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simpleadapter_view, parent, false);
        SimpleAdapter.SimpleHolder holder = new SimpleAdapter.SimpleHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SimpleHolder) holder).bind(items.get(position));
    }

    public abstract void bindData(SimpleHolder holder, T object);

    @Override
    public int getItemCount() {
        if (items != null ) {
            return items.size();
        }
        return 0;
    }

    class SimpleHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        LinearLayout mLinearLayout;
        TextView mTextView;

        public SimpleHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.cover);
            mTextView = itemView.findViewById(R.id.title);
            mLinearLayout = itemView.findViewById(R.id.container);
        }

        public void bind(T object) {
            bindData(this, object);
        }
    }
}