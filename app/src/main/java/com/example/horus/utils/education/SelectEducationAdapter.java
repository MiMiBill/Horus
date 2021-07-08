package com.example.horus.utils.education;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.horus.R;

import java.util.List;

import butterknife.BindView;
import io.reactivex.subjects.PublishSubject;

/**
 * des:
 * author: lognyun
 * date: 2018/10/26 15:41
 */
public class SelectEducationAdapter extends RecyclerView.Adapter<SelectEducationAdapter.ViewHolder> {

    private final PublishSubject<Integer> sSubject = PublishSubject.create();
    private List<String> mDatas;

    public SelectEducationAdapter(List<String> datas) {
        mDatas = datas;
    }

    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_select_education, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTxt.setText(mDatas.get(i));
        viewHolder.mTxt.setOnClickListener(v-> sSubject.onNext(viewHolder.getAdapterPosition()));
    }

    @Override public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt) TextView mTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public PublishSubject<Integer> getsSubject() {
        return sSubject;
    }
}
