package com.alice.core.views;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alice.core.R;
import com.alice.core.model.KeyValueObject;
import com.alice.core.util.CommonUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * Created by Admin on 2018/2/5.
 */

public class DropDownView extends LinearLayout {

    private List<KeyValueObject> mList = new ArrayList<>();
    private View mView;
    private TextView mTvProject;
    private DropDownAdapter mAdapter;
    private AlertDialog mDialog;
    private boolean mClickable;
    private OnItemSelectListener mOnClickListener;
    private onTextChangeListener mOnTextChangeListener;
    private TextView mTvNewKH;
    private boolean canShowLocalValue;


    public DropDownView(Context context) {
        super(context);
        init(context);
    }

    public DropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.wn_drop_down_view, this);
        mTvProject = (TextView) mView.findViewById(R.id.tvProject);
        initDialog(context);

        mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickable) {
//                    if (mList != null && mList.size() > 0) {
                        if (!mDialog.isShowing()) {
                            mDialog.show();
                        } else {
                            mDialog.dismiss();
                        }
//                    }
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mTvProject.setText(mAdapter.getData().get(position).getValue());
                if (!mDialog.isShowing()) {
                    mDialog.show();
                } else {
                    mDialog.dismiss();
                }
                if (mOnClickListener != null) {
                    mOnClickListener.onSelect(mAdapter.getData().get(position).getValue());
                }
            }
        });

    }

    private void initDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context).inflate(R.layout.wn_drop_down_list, null, false);
        builder.setView(itemView);
        mDialog = builder.create();
        RecyclerView rvList = (RecyclerView) itemView.findViewById(R.id.rvList);
        mTvNewKH = (TextView) itemView.findViewById(R.id.tvNewKH);
        mTvNewKH.setVisibility(GONE);
        rvList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL));
        mAdapter = new DropDownAdapter(R.layout.wn_drop_down_list_item);
        rvList.setAdapter(mAdapter);

        final EditText etSearch = (EditText) itemView.findViewById(R.id.etSearch);
        Button btnSearch = (Button) itemView.findViewById(R.id.btnSearch);
        mTvNewKH.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvProject.setText(etSearch.getText().toString());
                if (!mDialog.isShowing()) {
                    mDialog.show();
                } else {
                    mDialog.dismiss();
                }
                if (mOnClickListener != null) {
                    mOnClickListener.onSelect(etSearch.getText().toString());
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!canShowLocalValue){
                    filterMainProject(etSearch.getText().toString());
                    mTvNewKH.setText(etSearch.getText().toString());
                    mTvNewKH.setVisibility(GONE);
                    if (mOnTextChangeListener != null){
                        mOnTextChangeListener.onTextChange(etSearch.getText().toString());
                    }
                }else {
                    filterMainProject(etSearch.getText().toString());
                    mTvNewKH.setText(etSearch.getText().toString());
                    mTvNewKH.setVisibility(VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(context, etSearch.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMainProject(etSearch.getText().toString());
            }
        });
    }

    public void setList(List<KeyValueObject> list) {
        mList.clear();
        this.mList.addAll(list);
        if (mList != null && mList.size() > 0) {
            mTvProject.setText(mList.get(0).getValue());
        }
        mAdapter.setNewData(mList);
    }

    public void setText(String content) {
        if (!CommonUtil.isStrEmpty(content)) {
            mTvProject.setText(content);
        } else {
            if (mList != null && mList.size() > 0) {
                mTvProject.setText(mList.get(0).getValue());
            } else {
//                setText("暂无数据");
            }
        }
    }

    public String getText() {
        return mTvProject.getText().toString();
    }

    public KeyValueObject getCurrentObject(){
        for (KeyValueObject keyValueObject:mList){
            if (keyValueObject.getValue().equalsIgnoreCase(getText())){
                return keyValueObject;
            }
        }
        return null;
    }
    public void setClickables(boolean clickable) {
        mClickable = clickable;
    }

    private void filterMainProject(final String content) {

        if (mList != null && mList.size() > 0) {
            Observable.from(mList)
                    .filter(new Func1<KeyValueObject, Boolean>() {
                        @Override
                        public Boolean call(KeyValueObject mainProject) {
                            return mainProject.getValue().contains(content);
                        }
                    }).toList()
                    .subscribe(new Action1<List<KeyValueObject>>() {
                        @Override
                        public void call(List<KeyValueObject> mainProjects) {
                            mAdapter.setNewData(mainProjects);
                        }
                    });
        }
    }

    public void setOnItemClickListener(OnItemSelectListener onItemClickListener) {
        mOnClickListener = onItemClickListener;
    }

    public interface OnItemSelectListener {
        void onSelect(String value);
    }


    public interface  onTextChangeListener{
        void onTextChange(String text);
    }
    public void setOntextChangeListener(onTextChangeListener ontextChangeListener){
        mOnTextChangeListener = ontextChangeListener;
    }

}
