package site.teamo.biu.wdrop.sdk.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/5
 */
public class BiuHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiuHttpClient.class);

    //默认请求超时时间和会话超时时间 单位：秒
    private static final int DEFAULT_REQUEST_TIMEOUT = 15;
    private static final int DEFAULT_SESSION_TIMEOUT = 60;


    private HttpClientContext context;
    private CloseableHttpClient httpclient;
    private int requestTimeOut = 15;
    private int sessionTimeOut = 60;

    public static BiuHttpClient getClient(){
        return new BiuHttpClient();
    }

    //指定超时时间和代理的构造器
    public BiuHttpClient(int sessionTimeOut, int requestTimeOut, HttpHost proxy) {
        //赋值请求和会话超时时间
        this.sessionTimeOut = sessionTimeOut;
        this.requestTimeOut = requestTimeOut;
        if (this.sessionTimeOut <= 0) {
            this.sessionTimeOut = DEFAULT_SESSION_TIMEOUT;
        }
        if (this.requestTimeOut <= 0) {
            this.requestTimeOut = DEFAULT_REQUEST_TIMEOUT;
        }
        //初始化Cookie
        initCookieStore();
        //构造请求和实例化http客户端
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectTimeout(this.requestTimeOut * 1000).setSocketTimeout(this.requestTimeOut * 1000);
        if (proxy != null) {
            requestConfigBuilder.setProxy(proxy);
        }
        httpclient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfigBuilder.build()).build();
    }

    //指定代理的构造器
    public BiuHttpClient(HttpHost proxy) {
        this(DEFAULT_SESSION_TIMEOUT, DEFAULT_REQUEST_TIMEOUT, proxy);
    }

    //使用默认参数的构造器
    public BiuHttpClient() {
        this(null);
    }

    //初始化CookieStore
    private void initCookieStore() {
        PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();
        Registry<CookieSpecProvider> cookieSpecReg = RegistryBuilder.<CookieSpecProvider>create()
                .register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider(publicSuffixMatcher))
                .register(CookieSpecs.STANDARD, new RFC6265CookieSpecProvider(publicSuffixMatcher)).build();
        CookieStore cookieStore = new BasicCookieStore();

        context = HttpClientContext.create();
        context.setCookieSpecRegistry(cookieSpecReg);
        context.setCookieStore(cookieStore);
    }


    /**
     * http get请求，指定Url，返回结果格式化类型
     *
     * @param url        请求Url
     * @param resultType 返回结果类型
     * @param <T>        返回结果泛型
     * @return 返回结果
     * @throws IOException
     */
    public <T> T get(String url, Class<T> resultType) throws IOException {
        return get(url, null, null, resultType);
    }

    /**
     * http get请求，指定Url，请求头，请求参数，返回结果格式化类型
     *
     * @param url        请求Url
     * @param headers    请求头
     * @param queryParam 请求参数
     * @param resultType 返回结果类型
     * @param <T>        返回结果类型
     * @return 返回结果
     * @throws IOException
     */
    public <T> T get(String url, Map<String, String> headers, Map<String, String> queryParam, Class<T> resultType) throws IOException {
        //获取请求结果
        String result = get(url, headers, queryParam);
        return result(result, resultType);
    }

    /**
     * http get请求，指定Url
     *
     * @param url 请求url
     * @return 返回结果
     * @throws IOException
     */
    public String get(String url) throws IOException {
        return get(url, null, null);
    }

    /**
     * http get请求，指定Url，请求头，请求参数
     *
     * @param url        请求Url
     * @param headers    请求头
     * @param queryParam 请求参数
     * @return 请求结果String类型
     * @throws IOException
     */
    public String get(String url, Map<String, String> headers, Map<String, String> queryParam) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        //将请求头和请求参数配置
        configureHeaderAndQueryParam(httpGet, headers, queryParam);
        return request(httpGet);
    }

    /**
     * http post请求
     *
     * @param url        请求url
     * @param body       请求体
     * @param resultType 返回结果类型
     * @param <T>        返回结果泛型
     * @return 返回结果
     * @throws IOException
     */
    public <T> T post(String url, String body, Class<T> resultType) throws IOException {
        return post(url, null, null, body, resultType);
    }

    /**
     * http post请求
     *
     * @param url        请求url
     * @param headers    请求头
     * @param queryParam 请求参数
     * @param body       请求体
     * @param resultType 返回结果类型
     * @param <T>        返回结果泛型
     * @return 返回结果
     * @throws IOException
     */
    public <T> T post(String url, Map<String, String> headers, Map<String, String> queryParam, String body, Class<T> resultType) throws IOException {
        String result = post(url, headers, queryParam, body);
        return result(result, resultType);
    }

    /**
     * http post请求
     *
     * @param url  请url
     * @param body 请体
     * @return 返回结果
     * @throws IOException
     */
    public String post(String url, String body) throws IOException {
        return post(url, null, null, body);
    }

    /**
     * http post请求
     *
     * @param url        请url
     * @param headers    请求头
     * @param queryParam 请求参数
     * @param body       请求体
     * @return 返回结果String 类型
     * @throws IOException
     */
    public String post(String url, Map<String, String> headers, Map<String, String> queryParam, String body) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        //设置请求头和请求参数到请求方法
        configureHeaderAndQueryParam(httpPost, headers, queryParam);
        if (StringUtils.isNotBlank(body)) {
            HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
        }
        return request(httpPost);
    }


    /**
     * 上传文件
     *
     * @param url        请求url
     * @param headers    请求头
     * @param queryParam 请求参数
     * @param filePaths  要上传的文件路径
     * @param resultType 结果类型
     * @param <T>        结果类型
     * @return
     * @throws IOException
     */
    public <T> T upload(String url, Map<String, String> headers, Map<String, String> queryParam, List<String> filePaths, Class<T> resultType) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        configureHeaderAndQueryParam(httpPost, headers, queryParam);
        if (filePaths != null) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (String path : filePaths) {
                File file = new File(path);
                builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName());
            }
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
        }
        String result = request(httpPost);
        return result(result, resultType);
    }


    /**
     * 下载文件
     *
     * @param url        请求url
     * @param headers    请求头
     * @param queryParam 请求参数
     * @param filePath   下载文件路基
     * @throws IOException
     */
    public void download(String url, Map<String, String> headers, Map<String, String> queryParam, String filePath)
            throws IOException {

        HttpPost httpPost = new HttpPost(url);
        configureHeaderAndQueryParam(httpPost, headers, queryParam);

        HttpEntity entity = null;
        try {
            HttpResponse response = httpclient.execute(httpPost, context);
            int statusCode = response.getStatusLine().getStatusCode() / 100;
            entity = response.getEntity();
            if (statusCode == 4 || statusCode == 5) {
                throw new IOException("");
            }
            BufferedInputStream bis = new BufferedInputStream(entity.getContent());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            StreamUtils.copy(bis, bos);
            bis.close();
            bos.close();
        } finally {
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        }
    }

    /**
     * 设置请求头和请求参数到请求方法中
     *
     * @param httpMethod 请求方法
     * @param headers    请求头
     * @param queryParam 请求参数
     */
    private void configureHeaderAndQueryParam(HttpRequestBase httpMethod, Map<String, String> headers, Map<String, String> queryParam) {
        //设置请求头
        if (headers != null) {
            headers.entrySet().forEach(x -> httpMethod.setHeader(x.getKey(), x.getValue()));
        }
        //将请求参数进行封装
        if (queryParam != null) {
            URIBuilder builder = new URIBuilder(httpMethod.getURI());

            queryParam.entrySet().forEach(x -> builder.addParameter(x.getKey(), x.getValue()));
            try {
                httpMethod.setURI(builder.build());
            } catch (URISyntaxException e) {
                LOGGER.warn("封装参数到HttpGet失败", e);
            }
        }
    }

    /**
     * 执行请求
     *
     * @param httpMethod http请求方法
     * @return 请求结果
     * @throws IOException
     */
    private String request(HttpRequestBase httpMethod) throws IOException {
        HttpEntity entity = null;
        try {
            HttpResponse response = httpclient.execute(httpMethod, context);
            int statusCode = response.getStatusLine().getStatusCode() / 100;
            entity = response.getEntity();
            String result = EntityUtils.toString(response.getEntity());
            if (statusCode == 4 || statusCode == 5) {
                throw new IOException(result);
            }
            return result;
        } finally {
            //释放entity资源
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        }
    }

    /**
     * 请求结果字符串转换成指定类型
     *
     * @param result     结果字符串
     * @param resultType 指定类型
     * @param <T>        指定类型
     * @return
     */
    private <T> T result(String result, Class<T> resultType) {
        if (String.class.equals(resultType)) {
            return (T) result;
        }
        if (StringUtils.isBlank(result)) {
            return null;
        }
        return JSON.parseObject(result, resultType);
    }
}
