package com.x8.mt.common;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.x8.mt.entity.SystemLog;
import com.x8.mt.service.SystemLogService;

@Aspect
@Component
public class SystemLogAspect {

	@Resource  
	private SystemLogService systemLogService;  

	private  static  final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);  

	//Controller层切点  
	@Pointcut("execution (* com.x8.mt.controller..*.*(..))")  
	public void controllerAspect() {  
	}  

	/*
	 * 全局日志对象，方便记录方法执行起始时间
	 */
	SystemLog log = null;

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月25日
	 * 作用:前置通知 用于拦截Controller层记录用户的操作 
	 */
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) {
		log = new SystemLog();
		log.setStartdatetime(new Date(System.currentTimeMillis()));

		try {
			Subject subject = SecurityUtils.getSubject();  
			Session session = subject.getSession();
			log.setSystemusername(session.getAttribute("username").toString());
		} catch (Exception e) {
		}
	}    

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月25日
	 * 作用:controller环绕通知,使用在方法aspect()上注册的切入点
	 */
	//@Around("controllerAspect()")
	public void around(JoinPoint joinPoint){}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月24日
	 * 作用:后置通知 用于拦截Controller层记录用户的操作 
	 */
	@After("controllerAspect()")  
	public void after(JoinPoint joinPoint) {  
		log.setEnddatetime(new Date(System.currentTimeMillis()));
		if(log.getSystemusername()==null){//为了记录登陆方法的用户名
			try {
				Subject subject = SecurityUtils.getSubject();  
				Session session = subject.getSession();
				log.setSystemusername(session.getAttribute("username").toString());
			} catch (Exception e) {
			}
		}
	} 

	//配置后置返回通知,使用在方法aspect()上注册的切入点
	@AfterReturning(value="controllerAspect()", returning="result")
	public void afterReturning(JoinPoint joinPoint,Object result){

		String operationType = "";
		String operationDesc = "";
		JSONObject resultJson = (JSONObject) result;
		try {
			String targetName = joinPoint.getTarget().getClass().getName();  
			String methodName = joinPoint.getSignature().getName();  
			Object[] arguments = joinPoint.getArgs();  
			Class targetClass = Class.forName(targetName);  
			Method[] methods = targetClass.getMethods();
			for (Method method : methods) {  
				if (method.getName().equals(methodName)) {  
					Class[] clazzs = method.getParameterTypes();  
					if (clazzs.length == arguments.length) {  
						operationType = method.getAnnotation(Log.class).operationType();
						operationDesc = method.getAnnotation(Log.class).operationDesc();
						break;  
					}  
				}  
			}
			log.setResult(resultJson.getString("result"));
			log.setOperationdesc(operationDesc+",记录数:"+resultJson.getString("count"));
		} catch (Throwable e) {
			log.setResult(GlobalMethodAndParams.systemlogResult_exception);
			log.setOperationdesc(operationDesc+",记录数:0");
		}finally{
			log.setId(UUID.randomUUID().toString());
			log.setOperationtype(operationType);
			//日志记录更新到数据库
			systemLogService.insert(log);  
		}
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月24日
	 * 作用:异常通知 用于拦截记录异常日志 
	 */
	@AfterThrowing(pointcut = "controllerAspect()", throwing="e")  
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {  

		String operationType = "";
		String operationDesc = "";
		try {
			String targetName = joinPoint.getTarget().getClass().getName();  
			String methodName = joinPoint.getSignature().getName();  
			Object[] arguments = joinPoint.getArgs();  
			Class targetClass = Class.forName(targetName);  
			Method[] methods = targetClass.getMethods();
			for (Method method : methods) {  
				if (method.getName().equals(methodName)) {  
					Class[] clazzs = method.getParameterTypes();  
					if (clazzs.length == arguments.length) {  
						operationType = method.getAnnotation(Log.class).operationType();
						operationDesc = method.getAnnotation(Log.class).operationDesc();
						break;  
					}  
				}  
			}
		}  catch (Exception ex) {
		}finally{
			log.setId(UUID.randomUUID().toString());
			log.setOperationtype(operationType);
			log.setResult(GlobalMethodAndParams.systemlogResult_exception);
			log.setOperationdesc(operationDesc+",记录数:0");

			systemLogService.insert(log);  
		}
	}  

}
