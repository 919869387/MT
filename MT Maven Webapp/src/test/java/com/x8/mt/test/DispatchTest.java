package com.x8.mt.test;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.entity.Dispatch;
import com.x8.mt.service.DispatchService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
/**
 * 作者： GodDispose
 * 时间：2018年4月11日
 * 作用：
 */
public class DispatchTest {
	@Autowired
	DispatchService dispatchService;


	@Test	
	public void queryAll(){
		List<Dispatch> lists = dispatchService.queryAll();
		for(Dispatch dis : lists){
			System.out.println(dis.getUser());
		}
	}
	
	@Test
	public void addDispatch(){
		Dispatch dispatch = new Dispatch();
		dispatch.setIntervalTime(1);
		dispatch.setLastTime("2017-03-24 22:25:24");
		dispatch.setTime("2017-03-24 22:25:24");
		dispatch.setUser("testes");
		dispatchService.addDispatch(dispatch);
		
	}
	
	@Test
	public void deleteDispatch(){
		dispatchService.deleteDispatch(1);
	}
	
	@Test
	public void updateDispatch(){
		Dispatch dispatch = new Dispatch();
		dispatch.setIntervalTime(1);
		dispatch.setLastTime("2017-03-24 22:25:24");
		dispatch.setTime("2017-03-24 22:25:24");
		dispatch.setUser("testes111");
		dispatchService.addDispatch(dispatch);
		dispatchService.updateDispatch(dispatch);
	}
	
	@Test
	public void queryByDispatchId(){
		Dispatch dis = dispatchService.queryByDispatchId(2);
		System.out.println(dis.getUser());
	}
}
