package com.alice.core.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alice.core.R;
import com.alice.core.annotation.ContentView;
import com.alice.core.util.DensityUtil;
import com.alice.core.viewmodel.BaseViewModel;
import com.alice.core.views.LoadingView;

import java.lang.reflect.ParameterizedType;


/**
 *
 * Created by yuzhijun on 2017/6/27.
 */
public abstract class BaseFragment<T extends ViewModel> extends Fragment {
    public static int ivBack= R.mipmap.wn_iv_top_back;

    private TextView mTvTitle;
    private ImageView mBtnLeft;
    private ImageView mBtnRight;
    public Toolbar mToolbar;
    private LinearLayout mLLContent;
    private TextView mTvLeft;
    private TextView mTvRight;
    protected LinearLayout mLlLeft;
    private LinearLayout mLlRight;
    private ViewDataBinding mBind;
    protected ViewModel viewModel;
    private volatile boolean isLoading;
    private LoadingView progressDialog;

    protected boolean isInit = false;
    protected boolean isLoad = false;
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_base_activity, container, false);
        ViewGroup content = view.findViewById(R.id.content);
        mBind = DataBindingUtil.inflate(getLayoutInflater(),getLayoutResId(),content,true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view,savedInstanceState);
        handleArguments(getArguments());

        viewModel = ViewModelProviders.of(this).get(getTClass());

        if (viewModel instanceof BaseViewModel){
            ((BaseViewModel)viewModel).getErrorObservableData().observe(this, errorData -> onRepsonseError((Throwable) errorData));
        }

        initTitle();
        initViews(mBind,savedInstanceState);
        initData(viewModel);
        initEvent();

        isInit = true;

        isCanLoadData();
    }
    public void showProgressDialog() {
        if(!this.isLoading) {
            this.hideProgressDialog();
            this.isLoading = true;
            this.progressDialog = new LoadingView();
            this.progressDialog.show();
        }
    }
    public void hideProgressDialog() {
        if(this.progressDialog != null) {
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.progressDialog = null;
        }
        this.isLoading = false;
    }
    public boolean isLoading() {
        return isLoading;
    }
    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isCanLoadData();
        }
    }

    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }
    protected abstract void handleArguments(Bundle arguments);
    protected abstract void initTitle();
    protected abstract void initViews(ViewDataBinding viewDataBinding, Bundle savedInstanceState);
    protected abstract void initData(ViewModel baseViewModel);
    protected abstract void initEvent();

    protected int getLayoutResId() {
        for (Class c = getClass(); c != Fragment.class; c = c.getSuperclass()) {
            ContentView annotation = (ContentView) c.getAnnotation(ContentView.class);
            if (annotation != null) {
                return annotation.value();
            }
        }
        return 0;
    }
    public Class<T> getTClass(){
        Class<T> tClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }


    private  void setToolbar(View view, Bundle bundle){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTvTitle = (TextView) view.findViewById(R.id.toolbar_title);
        mBtnRight = (ImageView) view.findViewById(R.id.btnRight);
        mBtnLeft = (ImageView) view.findViewById(R.id.btnLeft);
        mLLContent= (LinearLayout) view.findViewById(R.id.content);
        mLlLeft= (LinearLayout) view.findViewById(R.id.llLeft);
        mLlRight= (LinearLayout) view.findViewById(R.id.llRight);
        mTvLeft=(TextView) view.findViewById(R.id.tvLeft);
        mTvRight=(TextView) view.findViewById(R.id.tvRight);
        setLeftDefault();
    }
    protected void isShowToolBar(boolean isShow){
        mToolbar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    protected void isShowLeft(boolean isShow){
        mBtnLeft.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mTvLeft.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    protected void isShowRight(boolean isShow){
        mBtnRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mTvRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    protected void setCenterTitle(String title){
        mTvTitle.setText(title == null || "".equalsIgnoreCase(title) ? "" : title);
    }
    protected  void setRightTitle(String title, View.OnClickListener listener){
        if (title != null && !"".equalsIgnoreCase(title)){
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(title);
            mBtnRight.setVisibility(View.GONE);
            mLlRight.setOnClickListener(listener);
        }
    }
    protected void setRightTitleAndIcon(String title, @DrawableRes int icon, View.OnClickListener listener){
        if (title != null && !"".equalsIgnoreCase(title)){
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(title);
        }
        mBtnRight.setVisibility(View.VISIBLE);
        mBtnRight.setImageResource(icon);
        ViewGroup.LayoutParams linearParams = mBtnRight.getLayoutParams();
        linearParams.height = DensityUtil.dip2px(getActivity(),15);
        linearParams.width = DensityUtil.dip2px(getActivity(),15);
        mBtnRight.setLayoutParams(linearParams);
        mLlRight.setOnClickListener(listener);
    }
    protected  void setLeftTitle(String title, View.OnClickListener listener){
        if (title != null && !"".equalsIgnoreCase(title)){
            mTvLeft.setVisibility(View.VISIBLE);
            mTvLeft.setText(title);
            mBtnLeft.setVisibility(View.GONE);
            mLlLeft.setOnClickListener(listener);
        }
    }
    protected  void setLeftTitleColor(int resId){
        int color=getResources().getColor(resId);
        mTvLeft.setVisibility(View.VISIBLE);
        mTvLeft.setTextColor(color);
    }
    protected  void setRightTitleColor(int resId){
        int color=getResources().getColor(resId);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setTextColor(color);
    }
    protected void setLeftTitleAndIcon(String title, @DrawableRes int icon, View.OnClickListener listener){
        if (title != null && !"".equalsIgnoreCase(title)){
            mTvLeft.setVisibility(View.VISIBLE);
            mTvLeft.setText(title);
        }
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setBackgroundResource(icon);
        ViewGroup.LayoutParams linearParams = mBtnLeft.getLayoutParams();
        linearParams.height = DensityUtil.dip2px(getActivity(),15);
        linearParams.width = DensityUtil.dip2px(getActivity(),15);
        mBtnLeft.setLayoutParams(linearParams);
        mLlLeft.setOnClickListener(listener);
    }
    protected ImageView getLeftButton(){
        return mBtnLeft;
    }
    protected ImageView getRightButton(){
        return mBtnRight;
    }
    protected void  setLeftDefault(){
        mToolbar.setTitle("");
        mTvLeft.setVisibility(View.VISIBLE);
        mTvLeft.setText("返回");
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setBackgroundResource(ivBack);
        ViewGroup.LayoutParams linearParams = mBtnLeft.getLayoutParams();
        linearParams.height = DensityUtil.dip2px(getActivity(),15);
        linearParams.width = DensityUtil.dip2px(getActivity(),15);
        mBtnLeft.setLayoutParams(linearParams);
        mLlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.currentActivity.finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
    }


    protected abstract void lazyLoad();


    protected void stopLoad() {
    }

    public void onRepsonseError(Throwable throwable){
        Toast.makeText(getActivity(), throwable.getMessage()+"", Toast.LENGTH_SHORT).show();

    }
}
