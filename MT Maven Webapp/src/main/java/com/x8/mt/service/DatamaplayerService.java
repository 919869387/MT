package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IDatamaplayerDao;
import com.x8.mt.entity.Datamaplayer;

@Service
public class DatamaplayerService {
	@Resource
	IDatamaplayerDao iDatamaplayerDao;

	public List<Datamaplayer> getDatamaplayerList() {
		return iDatamaplayerDao.getDatamaplayerList();
	}
	
}
