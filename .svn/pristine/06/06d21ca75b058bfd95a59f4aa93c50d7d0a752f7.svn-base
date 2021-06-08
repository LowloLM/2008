package com.fh.shop.admin.aspect;

import com.fh.shop.admin.annotation.LogInfo;
import com.fh.shop.admin.biz.log.ILogService;
import com.fh.shop.admin.po.log.Log;
import com.fh.shop.admin.po.user.User;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.common.WebContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Resource(name = "logService")
    private ILogService logService;

    /***
     * AOP日志记录
     * @param pjp
     * @return
     * @throws Throwable
     */
    //横切逻辑【非核心】
    public Object doLog(ProceedingJoinPoint pjp) throws Throwable{
        // 类的全称
        String canonicalName = pjp.getTarget().getClass().getCanonicalName();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取方法名
        String methodName = signature.getName();
        //获取当前正在执行的方法
        Method method = signature.getMethod();
        //判断方法上是否有指定的注解
        String info = "";
        if(method.isAnnotationPresent(LogInfo.class)){
            //获取方法上的自定义注解
            LogInfo annotation = method.getAnnotation(LogInfo.class);
            //获取注解中属性的值
            info = annotation.info();
        }
        //向数据库插入日志信息
        HttpServletRequest request = WebContext.getRequest();
        StringBuilder paramInfo = new StringBuilder();
        //获取参数信息
        //name=zhangsan 或者 name=[zhangsan,lisi,wangwu]
        Map<String, String[]> parameterMap = request.getParameterMap();
        //循环遍历map【必须掌握的基本功】
        Iterator<Map.Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String[]> entry = iterator.next();
            String key = entry.getKey();
            String[] value = entry.getValue();
            // ;key=value;key=value;key=value1,value2,value3;
            paramInfo.append(";").append(key).append("=").append(StringUtils.join(value,","));
        }
        String reslut = "";
        if (paramInfo.length() > 0) {
            reslut = paramInfo.substring(1);
        }
        LOGGER.info("开始执行了"+canonicalName+"的"+methodName+"=========");
        //最重要【执行真正的业务】
        Object result = pjp.proceed();
        LOGGER.info("执行了"+canonicalName+"的指定"+methodName+"结束=========");
        //获取用户信息
        User user = (User) request.getSession().getAttribute(SystemConstant.CURR_USER);
        //如果用户为null，证明没有登录成功
        if (user == null) {
            return result;
        }
        Log log = new Log();
        log.setUserName(user.getLoginName());
        log.setRealName(user.getRealName());
        log.setInfo(info);
        log.setInsertTime(new Date());
        log.setParamInfo(reslut);
        //插入日志
        logService.addLog(log);
        return result;
    }

}
