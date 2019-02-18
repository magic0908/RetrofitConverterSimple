package com.xing.retrofitconverter;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xing.retrofitconverter.api.ApiServiceImpl;
import com.xing.retrofitconverter.api.BaseResourceObserver;
import com.xing.retrofitconverter.api.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private CompositeDisposable mCompositeDisposable;

    private int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitClient.getInstance().init();
        setContentView(R.layout.activity_main);
        initView();
        mAdapter = new ArticleAdapter(null);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleDetailBean bean = mAdapter.getData().get(position);
                Uri uri = Uri.parse(bean.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int margin = getResources().getDimensionPixelSize(R.dimen.dp_10);
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = margin;
                    outRect.bottom = margin;
                } else {
                    outRect.top = 0;
                    outRect.bottom = margin;
                }
                outRect.left = margin;
                outRect.right = margin;
            }
        });
        onRefresh();
    }

    private void initView() {
        mRefreshLayout = findViewById(R.id.srl);
        mRecyclerView = findViewById(R.id.rv_list);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        requestData();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreRequested() {
        requestData();
    }

    private void requestData() {
        Disposable disposable = ApiServiceImpl.getInstance()
                .getArticleList(pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseResourceObserver<ArticleBean>() {
                    @Override
                    public void onNext(ArticleBean articleBean) {
                        List<ArticleDetailBean> list = articleBean.getDatas();
                        if (pageIndex == 0) {
                            mAdapter.setNewData(list);
                        } else if (articleBean.getCurPage() >= articleBean.getPageCount()) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.addData(list);
                            mAdapter.loadMoreComplete();
                        }
                        pageIndex = articleBean.getCurPage();
                    }

                    //可以重写该方法，处理额外的逻辑
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }

}
