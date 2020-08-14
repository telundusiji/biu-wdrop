package site.teamo.biu.wdrop.sdk.common.util;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 
 * @Title: BiuJSONResult.java
 * @Description: 自定义响应数据结构
 * 				本类可提供给 H5/ios/安卓/公众号/小程序 使用
 * 				前端接受此类数据（json object)后，可自行根据业务去实现相关功能
 * 				200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * @Copyright: Copyright (c) 2020
 * @version V1.0
 */
@Data
@Builder
@NoArgsConstructor
public class BiuJSONResult {

    // 响应业务状态
    private Integer code;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    


    public static BiuJSONResult build(Integer status, String msg, Object data) {
        return new BiuJSONResult(status, msg, data);
    }

    public static BiuJSONResult ok(Object data) {
        return new BiuJSONResult(data);
    }

    public static BiuJSONResult ok() {
        return new BiuJSONResult(null);
    }
    
    public static BiuJSONResult errorMsg(String msg) {
        return new BiuJSONResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }
    
    public static BiuJSONResult errorMap(Object data) {
        return new BiuJSONResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", data);
    }

    public static BiuJSONResult errorParameter(Object data){
        return new BiuJSONResult(HttpStatus.BAD_REQUEST.value(),"parameter error",data);
    }

    public BiuJSONResult(Integer status, String msg, Object data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    public BiuJSONResult(Object data) {
        this.code = HttpStatus.OK.value();
        this.msg = "OK";
        this.data = data;
    }

}
