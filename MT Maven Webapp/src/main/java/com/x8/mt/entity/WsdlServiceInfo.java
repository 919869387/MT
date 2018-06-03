package com.x8.mt.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WsdlServiceInfo {

	@Value("#{configProperties['wsdlService.Switch']}")
	private String Switch;//是否打开wsdlService通知
	@Value("#{configProperties['wsdlService.Url']}")
	private String Url;//wsdlService地址
	@Value("#{configProperties['wsdlService.Tns']}")
	private String Tns;//wsdlService地址命名空间
	@Value("#{configProperties['wsdlService.Method']}")
	private String Method;//wsdlService请求的方法名
	
	public String getSwitch() {
		return Switch;
	}
	public void setSwitch(String switch1) {
		Switch = switch1;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getTns() {
		return Tns;
	}
	public void setTns(String tns) {
		Tns = tns;
	}
	public String getMethod() {
		return Method;
	}
	public void setMethod(String method) {
		Method = method;
	}
	
	
}
