package com.x8.mt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 作者： allen
 * 时间：2018年5月5日
 * 作用：系统给外部提供的接口
 */
@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/externalInterface")
public class ExternalInterfaceController {

}
