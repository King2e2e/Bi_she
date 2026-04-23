package org.example.common;

import org.example.common.enums.ResultCodeEnum;

public class Result {
    private String code;    // 状态码
    private String msg;     // 提示信息
    private Object data;   //Object它可以是一个String（字符串）、一个Integer（整数）、一个User（用户对象）    // 真正的数据
    private Result(Object data) {
        this.data = data;
    }
    public Result(){

    }
    public static Result success(){
        Result tResult = new Result();
        tResult.setCode(ResultCodeEnum.SUCCESS.code);
        tResult.setMsg(ResultCodeEnum.SUCCESS.msg);
        return tResult;
    }
    public static Result success(Object data){
        Result tResult = new Result(data);   // 调用私有构造方法，放入真实数据
        tResult.setCode(ResultCodeEnum.SUCCESS.code);
        tResult.setMsg(ResultCodeEnum.SUCCESS.msg);
        return tResult;
    }
    public static Result error(){           // 无参：返回一个通用的系统错误
        Result tResult = new Result();
        tResult.setCode(ResultCodeEnum.SUCCESS.code);
        tResult.setMsg(ResultCodeEnum.SUCCESS.msg);
        return tResult;
    }
    public static Result error(String code,String msg){         // 自定义错误码和消息
        Result tResult = new Result();
        tResult.setCode(code);
        tResult.setMsg(msg);
        return tResult;
    }
    public static Result error(ResultCodeEnum resultCodeEnum) {         // 从枚举中取错误信息
        Result tResult = new Result();
        tResult.setCode(resultCodeEnum.code);
        tResult.setMsg(resultCodeEnum.msg);
        return tResult;
    }
    public String getCode(){return code;}
    public String getMsg(){return msg;}
    public Object getData(){return data;}
    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }
    public void setData(Object data){
        this.data = data;
    }

}
