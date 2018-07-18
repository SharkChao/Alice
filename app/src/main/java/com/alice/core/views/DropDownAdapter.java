package com.alice.core.views;

import com.alice.core.alice.R;
import com.alice.core.model.KeyValueObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Admin on 2018/2/5.
 */

public class DropDownAdapter extends BaseQuickAdapter<KeyValueObject,BaseViewHolder> {
    public DropDownAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, KeyValueObject item) {
        helper.setText(R.id.tvProject,item.getValue());
    }
}
