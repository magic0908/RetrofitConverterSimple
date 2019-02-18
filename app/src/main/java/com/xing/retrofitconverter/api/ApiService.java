package com.xing.retrofitconverter.api;

import com.xing.retrofitconverter.ArticleBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Creation Time: 2019/2/13 13:32.
 * Author: King.
 * Description: api
 */
public interface ApiService {

    String BASE_URL = "http://www.wanandroid.com";

    /**
     * 获取首页文章列表
     * @param pageIndex pageIndex
     * @return Observable
     */
    @GET("/article/listproject/{pageIndex}/json")
    Observable<ArticleBean> getArticleList(@Path("pageIndex") int pageIndex);

    @GET("/article/listproject/{pageIndex}/json")
    Observable<BaseResponse<ArticleBean>> getArticleList1(@Path("pageIndex") int pageIndex);

}
