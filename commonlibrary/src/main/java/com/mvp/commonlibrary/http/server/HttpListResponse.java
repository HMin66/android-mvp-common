package com.mvp.commonlibrary.http.server;

import com.google.gson.annotations.SerializedName;

/**
 * http响应参数实体类
 * 通过Gson解析属性名称需要与服务器返回字段对应,或者使用注解@SerializedName
 * 备注:这里与服务器约定返回格式
 *
 */
public class HttpListResponse<T> {

    /**
     * 描述信息
     */
    @SerializedName("message")
    private String msg;

    /**
     * 状态码
     */
    @SerializedName("code")
    private int code;

    /**
     * 数据对象[成功返回对象,失败返回错误说明]
     */
    @SerializedName("data")
    private DataBean<T> result;

    /**
     * 是否成功(这里约定200)
     *
     * @return
     */
    public boolean isSuccess() {
        return code == ServerResponseCode.SUCCESS;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public DataBean<T> getResult() {
        return result;
    }

    public static class DataBean<T> {
        /**
         * current : 0
         * pages : 0
         * records : [{"content":"string","id":0,"pageNo":1,"pageSize":10,"state":0,"time":"2019-11-05T08:59:22.262Z","title":"string","type":0,"waybillId":0}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private T records;

        @Override
        public String toString() {
            return "DataBean{" +
                    "current=" + current +
                    ", pages=" + pages +
                    ", searchCount=" + searchCount +
                    ", size=" + size +
                    ", total=" + total +
                    ", records=" + records +
                    '}';
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public T getRecords() {
            return records;
        }

        public void setRecords(T records) {
            this.records = records;
        }
    }

    @Override
    public String toString() {
        return "MessageList{" +
                "code=" + code +
                ", data=" + result +
                ", message='" + msg + '\'' +
                '}';
    }
}
