package com.x8.mt.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IDatamaplayerDao;

@Service
public class DatamaplayerService {
	@Resource
	IDatamaplayerDao iDatamaplayerDao;
	
}
