//package com.paper.order.util;
//
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.springframework.expression.ParseException;
//
//public class HttpH {
//
//    /**
//     * 发送http  get 请求
//     *
//     * @param uri
//     * @return
//     */
//    public static String sendGET(String uri) {
//
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        String content = null;
//        try {
//            // 创建httpget.
//            HttpGet httpget = new HttpGet(uri);
//            //System.out.println("executing request " + httpget.getURI());
//            // 执行get请求.
//            CloseableHttpResponse response = httpclient.execute(httpget);
//            HttpEntity entity = null;
//            try {
//                // 获取响应实体
//                entity = response.getEntity();
//                //System.out.println("--------------------------------------");
//                // 打印响应状态
//                System.out.println("响应状态----->" + response.getStatusLine());
//                if (entity != null) {
//                    // 打印响应内容长度
//                    //System.out.println("Response content length: " + entity.getContentLength());
//                    // 打印响应内容
//                    //System.out.println("Response content: " + EntityUtils.toString(entity));
//                    content = EntityUtils.toString(entity, "utf-8");
//                }
//                //System.out.println("------------------------------------");
//            } finally {
//                response.close();
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭连接,释放资源
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return content;
//    }
//
//
//    /**
//     * 发送http  post 请求
//     *
//     * @param uri
//     * @return
//     */
//    public String sendPOST(String uri, String[] names, String[] values) {
//        String content = null;
//        // 创建默认的httpClient实例.
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        // 创建httppost
//        HttpPost httppost = new HttpPost(uri);
//        // 创建参数队列
//        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
////        formparams.add(new BasicNameValuePair("mc_username", "admin"));
//        if (null == names || values == null || names.length != values.length)
//            return null;
//        for (int i = 0; i < names.length; i++) {
//            formparams.add(new BasicNameValuePair(names[i], values[i]));
//        }
//        UrlEncodedFormEntity uefEntity;
//        try {
//            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
//            httppost.setEntity(uefEntity);
//            System.out.println("executing request " + httppost.getURI());
//            CloseableHttpResponse response = httpclient.execute(httppost);
//            try {
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
////                    System.out.println("--------------------------------------");
////                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
////                    System.out.println("--------------------------------------");
//                    content = EntityUtils.toString(entity);
//                }
//            } finally {
//                response.close();
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭连接,释放资源
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return content;
//    }
//
//
//    public static void main(String[] args) {
//
//        HttpH httpH = new HttpH();
//        String uri = "http://agentapi.rayjump.com/agentapi/ad?appid=24600&apikey=581ee2e0c4fff0c9f7efb5c0cf35a85b&restype=json";
//        String content = httpH.sendGET(uri);
//        System.out.println(content);
//
//    }
//
//
//}
//
