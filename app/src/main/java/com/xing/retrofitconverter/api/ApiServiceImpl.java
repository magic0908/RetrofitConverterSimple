package com.xing.retrofitconverter.api;

/**
 * Creation Time: 2018/8/30 9:56.
 * Author: King.
 * Description: ApiService接口类实现
 */
public class ApiServiceImpl {

    private ApiServiceImpl() {
        throw new RuntimeException("you can't init me");
    }
    public static ApiService getInstance() {
        return createApiService.apiService;
    }

    /**
     * Retrofit生成接口对象.
     */
    private static class createApiService {
        /**
         * Retrofit会根据传入的接口类.生成实例对象.
         */
        private static final ApiService apiService = RetrofitClient.getInstance().getApi(ApiService.class);
    }

}
