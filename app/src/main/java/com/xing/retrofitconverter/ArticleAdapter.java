package com.xing.retrofitconverter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Creation Time: 2019/2/13 14:26.
 * Author: King.
 * Description: ArticleAdapter
 */
public class ArticleAdapter extends BaseQuickAdapter<ArticleDetailBean, BaseViewHolder> {

    public ArticleAdapter(@Nullable List<ArticleDetailBean> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleDetailBean item) {
        ImageView ivPic = helper.getView(R.id.iv_pic);
        Glide.with(ivPic.getContext())
                .load(item.getEnvelopePic())
                .apply(new RequestOptions().centerCrop())
                .into(ivPic);
        helper.setText(R.id.tv_title,item.getTitle())
                .setText(R.id.tv_desc,item.getDesc())
                .setText(R.id.tv_time,item.getNiceDate())
                .setText(R.id.tv_author,item.getAuthor());
    }
}
