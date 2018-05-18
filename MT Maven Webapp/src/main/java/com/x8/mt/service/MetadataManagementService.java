package com.x8.mt.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.PageParam;
import com.x8.mt.common.TransformMetadata;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.dao.IMetaDataRelationDao;
import com.x8.mt.dao.IMetadataManagementDao;
import com.x8.mt.dao.IMetadataTankDao;
import com.x8.mt.dao.IMetamodel_datatypeDao;
import com.x8.mt.dao.IMetamodel_hierarchyDao;
import com.x8.mt.entity.MetaDataRelation;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.MetadataTank;
import com.x8.mt.entity.Metamodel_datatype;
import com.x8.mt.entity.Metamodel_hierarchy;
/**
 * 作者： allen
 * 时间：2018年3月15日
 */
@Service
public class MetadataManagementService {
	@Resource
	IMetadataManagementDao imetadataManagementDao;	
	@Resource
	IMetamodel_datatypeDao iMetamodel_datatypeDao;
	@Resource
	IMetamodel_hierarchyDao iMetamodel_hierarchyDao;
	@Resource
	IMetaDataRelationDao iMetaDataRelationDao;
	@Resource
	IMetadataTankDao iMetadataTankDao;
	@Resource
	IMetaDataDao iMetaDataDao;

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月16日
	 * 作用:导出元数据到excel
	 */
	public HSSFWorkbook exportMetadataToExcel(String metadataid) {
		Metadata metadata = imetadataManagementDao.getMetadata(metadataid);
		HSSFWorkbook wb = new HSSFWorkbook();
		if(metadata!=null){
			wb = new HSSFWorkbook(); 
			Metamodel_hierarchy Metamodel_hierarchy = imetadataManagementDao.getMetamodelByMetadataid(metadataid);
			Sheet sheet = wb.createSheet(Metamodel_hierarchy.getName());
			setExcelCellColor(wb,sheet);//设置sheet表头

			Row row = sheet.createRow(1);
			setExcelCellContent(row,null,metadata);

			exportSonMetadataToExcel(wb,metadata);
		}
		return wb;  
	}

	private void setExcelCellColor(HSSFWorkbook wb,Sheet sheet){
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0); 
		cell= row.createCell(0);
		cell.setCellValue("元数据id");
		cell = row.createCell(1);  
		cell.setCellValue("元数据名称");
		cell = row.createCell(2);  
		cell.setCellValue("元数据业务说明");
		cell = row.createCell(3);  
		cell.setCellValue("元数据入库时间");
		cell = row.createCell(4);  
		cell.setCellValue("元数据修改时间");
		cell = row.createCell(5);  
		cell.setCellValue("元数据的版本号");
		cell = row.createCell(6);  
		cell.setCellValue("采集元数据的任务编号");
		cell = row.createCell(7);  
		cell.setCellValue("审核状态");
		cell = row.createCell(8);  
		cell.setCellValue("所属的元模型id");
		cell = row.createCell(9);  
		cell.setCellValue("元数据的私有属性信息");
		cell = row.createCell(10);  
		cell.setCellValue("父元数据名称");
	}

	private void setExcelCellContent(Row row,Metadata fatherMetadata,Metadata metadata){
		Cell cell = null;
		cell= row.createCell(0);
		cell.setCellValue(metadata.getID());
		cell = row.createCell(1);  
		cell.setCellValue(metadata.getNAME());
		cell = row.createCell(2);  
		cell.setCellValue(metadata.getDESCRIPTION());
		cell = row.createCell(3);  
		cell.setCellValue(metadata.getCREATETIME());
		cell = row.createCell(4);  
		cell.setCellValue(metadata.getUPDATETIME());
		cell = row.createCell(5);  
		cell.setCellValue(metadata.getVERSION());
		cell = row.createCell(6);  
		cell.setCellValue(metadata.getCOLLECTJOBID());
		cell = row.createCell(7);  
		cell.setCellValue(metadata.getCHECKSTATUS());
		cell = row.createCell(8);  
		cell.setCellValue(metadata.getMETAMODELID());
		cell = row.createCell(9);  
		cell.setCellValue(metadata.getATTRIBUTES());

		if(fatherMetadata!=null){
			cell = row.createCell(10);  
			cell.setCellValue(fatherMetadata.getNAME());
		}
	}

	private void exportSonMetadataToExcel(HSSFWorkbook wb,Metadata fatherMetadata){
		List<Metadata> metadataList = imetadataManagementDao.getSonMetadata(fatherMetadata.getID()+"");
		if(metadataList.size()==0){
			return;
		}else{
			Metamodel_hierarchy Metamodel_hierarchy = imetadataManagementDao.getMetamodelByMetadataid(metadataList.get(0).getID()+"");
			Sheet sheet = null;
			Row row = null;
			if(wb.getSheet(Metamodel_hierarchy.getName())==null){//此类型sheet不存在
				sheet = wb.createSheet(Metamodel_hierarchy.getName());
				setExcelCellColor(wb,sheet);//设置sheet表头

				for(int i=0;i<metadataList.size();i++){
					row = sheet.createRow(i+1);
					setExcelCellContent(row,fatherMetadata,metadataList.get(i));//设置单元格内容

					exportSonMetadataToExcel(wb,metadataList.get(i));
				}
			}else{//此类型sheet存在
				sheet = wb.getSheet(Metamodel_hierarchy.getName());
				int startRow = sheet.getLastRowNum();//得到已经写入数据的最后一行index

				for(int i=0;i<metadataList.size();i++){
					row = sheet.createRow(i+startRow+1);
					setExcelCellContent(row,fatherMetadata,metadataList.get(i));//设置单元格内容

					exportSonMetadataToExcel(wb,metadataList.get(i));
				}
			}
		}
	}


	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月19日
	 * 作用:查找元数据
	 */
	public PageParam searchMetadata(String key,int currPage,int pageSize) {
		
		PageParam pageParam = null;
		
		int rowCount=imetadataManagementDao.searchMetadataCount(key);//总记录数
		if(rowCount!=0){
			List<Map<String, Object>> searchMetadataList = new ArrayList<Map<String, Object>>();
			int startIndex = (currPage-1)*pageSize;
			
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("key", key);
			param.put("startIndex", startIndex);
			param.put("pageSize", pageSize);
			
			List<Metadata> metadataList = imetadataManagementDao.searchMetadataPage(param);
			Map<String, Object> map = null;
			for(Metadata metadata : metadataList){
				map = new HashMap<String, Object>();
				map.put("ID", metadata.getID());
				map.put("NAME", metadata.getNAME());
				map.put("DESCRIPTION", metadata.getDESCRIPTION());
				map.put("CREATETIME", metadata.getCREATETIME());
				map.put("UPDATETIME", metadata.getUPDATETIME());
				map.put("VERSION", metadata.getVERSION());
				map.put("COLLECTJOBID", metadata.getCOLLECTJOBID());
				map.put("CHECKSTATUS", metadata.getCHECKSTATUS());
				map.put("METAMODELID", metadata.getMETAMODELID());

				searchMetadataList.add(map);
			}
			
			pageParam = new PageParam();
			pageParam.setCurrPage(currPage);
			pageParam.setPageSize(pageSize);
			pageParam.setRowCount(rowCount);
			pageParam.setDate(searchMetadataList);
		}
		return pageParam;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年4月18日
	 * 作用:根据表元数据id，获取字段元数据
	 */
	public List<Map<String, Object>> getFieldMetadataList(String metadataId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataId", metadataId);
		map.put("medamodelId", GlobalMethodAndParams.FieldMetamodelId);

		return imetadataManagementDao.getMetadataList(map);
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年4月18日
	 * 作用:根据数据库元数据id，获取表元数据
	 */
	public List<Map<String, Object>> getTableMetadataList(String metadataId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataId", metadataId);
		map.put("medamodelId", GlobalMethodAndParams.TableMedamodelId_InDatabase);

		return imetadataManagementDao.getMetadataList(map);
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年4月18日
	 * 作用:获取所有数据库元数据获取所有数据库元数据
	 */
	public List<Map<String,Object>>getDatabaseMetadataList(){
		return imetadataManagementDao.getDatabaseMetadataList(GlobalMethodAndParams.DatabaseMetamodelId);
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月25日
	 * 作用:判断该元数据是否存在
	 */
	public boolean existMetadata(String metadataId) {
		if(iMetaDataDao.getMetadataById(Integer.parseInt(metadataId))!=null){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月25日
	 * 作用:删除元数据信息[因为数据库中是级联删除,删除主键，外键跟着删除。只需要删除metadata表即可]
	 *  	1.删除metadata_tank表中记录
	 *  	2.删除metadata_relation表中记录
	 *  	3.删除metadata表中记录
	 */
	@Transactional
	public boolean daleteMetadataInfo(String metadataId,List<Object> count) {

		List<String> sonMetadataIds = iMetaDataRelationDao.getSonMetadataID(metadataId);

		for(int i=0;i<sonMetadataIds.size();i++){
			String sonMetadataId = sonMetadataIds.get(i);

			//递归先删除儿子元数据
			daleteMetadataInfo(sonMetadataId,count);
		}

		if(imetadataManagementDao.daleteMetadata(metadataId)==1){
			count.add(1);
		}else{
			throw new RuntimeException("daleteMetadata Error");
		}

		return true;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月26日
	 * 作用:修改元数据私有信息
	 *  	1.将元数据在metadata表修改Attributes
	 *  	2.再修改metadata_tank表Attributes
	 */
	@Transactional
	public boolean updateMetadataInfoForPRIVATE(Map<String, Object> map) {

		//1.将元数据在metadata表修改
		String metaModelId = (map.get(GlobalMethodAndParams.Public_Metamodel_METAMODELID)).toString();

		List<String> attributesField = imetadataManagementDao.getAttributesField(metaModelId);
		JSONObject attributes = TransformMetadata.createAttributes(map, attributesField);

		Metadata metadata = new Metadata();
		metadata.setID(Integer.parseInt(map.get("ID").toString()));
		metadata.setATTRIBUTES(attributes.toString());

		if(!(imetadataManagementDao.updateMetadatAttributes(metadata)>0)){
			throw new RuntimeException("updateMetadatAttributes Error");
		}

		//2.加入metadata_tank表
		MetadataTank metadataTank = new MetadataTank();
		metadataTank.setATTRIBUTES(metadata.getATTRIBUTES());
		metadataTank.setID(Integer.parseInt(map.get("metadataTankid").toString()));

		if(!(iMetadataTankDao.updateMetaDataTankAttributes(metadataTank)>0)){
			throw new RuntimeException("updateMetaDataTankAttributes Error");
		}

		return true;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月24日
	 * 作用:修改元数据信息
	 *  	1.将元数据在metadata表修改
	 *  	2.修改后的记录加入metadata_tank表
	 */
	@Transactional
	public int updateMetadataInfoForCommon(Map<String, Object> map) {

		//1.将元数据在metadata表修改
		Metadata metadata = iMetaDataDao.getMetadataById(Integer.parseInt(map.get("ID").toString()));

		metadata.setNAME(map.get("NAME").toString());
		metadata.setDESCRIPTION(map.get("DESCRIPTION").toString());

		Date updateDataTime = new Date();
		metadata.setUPDATETIME(updateDataTime);

		metadata.setVERSION(metadata.getVERSION()+1);

		if(!(imetadataManagementDao.updateMetadata(metadata)>0)){
			throw new RuntimeException("updateMetadata Error");
		}

		//2.加入metadata_tank表
		MetadataTank metadataTank = new MetadataTank();
		metadataTank.setCHECKSTATUS(metadata.getCHECKSTATUS());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			metadataTank.setCREATETIME(sdf.parse(metadata.getCREATETIME()));
		} catch (ParseException e) {
			throw new RuntimeException("DATE Transform Error");
		}

		metadataTank.setDESCRIPTION(metadata.getDESCRIPTION());
		metadataTank.setKeyid(metadata.getID());
		metadataTank.setMETAMODELID(metadata.getMETAMODELID());
		metadataTank.setNAME(metadata.getNAME());
		metadataTank.setUPDATETIME(updateDataTime);
		metadataTank.setVERSION(metadata.getVERSION());
		metadataTank.setCOLLECTJOBID(metadata.getCOLLECTJOBID());

		if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
			throw new RuntimeException("insertMetaDataTank Error");
		}

		return metadataTank.getID();
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:insert元数据,插入实际元数据
	 *  	1.将元数据加入到metadata表
	 *  	2.加入metadata_relation表
	 *  	3.加入metadata_tank表
	 */
	@Transactional
	public int addMetadata(Map<String,Object> map){

		String parentMetadataIdStr = map.get("parentMetadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();
		Date dataTime = new Date();

		//1.将元数据加入到metadata表
		Metadata metadata = new Metadata();
		metadata.setMETAMODELID(Integer.parseInt(metamodelIdStr));
		metadata.setNAME(map.get("NAME").toString());
		metadata.setDESCRIPTION(map.get("DESCRIPTION").toString());
		metadata.setCHECKSTATUS("1");
		metadata.setCREATETIME(dataTime);
		metadata.setUPDATETIME(dataTime);
		metadata.setVERSION(1);

		map.remove("metamodelId");
		map.remove("NAME");
		map.remove("DESCRIPTION");
		map.remove("parentMetadataId");

		metadata.setATTRIBUTES(JSONObject.fromObject(map).toString());

		if(!(imetadataManagementDao.insertMetadata(metadata)>0)){
			throw new RuntimeException("insertMetadata Error");
		}

		//2.加入metadata_relation表
		int metadataId = metadata.getID();

		if(!parentMetadataIdStr.equals("0")){
			//parentMetadataId!=0,说明添加不是第一层元数据
			MetaDataRelation metaDataRelation = new MetaDataRelation();
			metaDataRelation.setMetaDataId(Integer.parseInt(parentMetadataIdStr));
			metaDataRelation.setRelateMetaDataId(metadataId);
			metaDataRelation.setType(GlobalMethodAndParams.COMPOSITION);

			if(!(iMetaDataRelationDao.insertMetaDataRelation(metaDataRelation)>0)){
				throw new RuntimeException("insertMetaDataRelation Error");
			}
		}

		//3.加入metadata_tank表
		MetadataTank metadataTank = new MetadataTank();
		metadataTank.setCHECKSTATUS(metadata.getCHECKSTATUS());
		metadataTank.setATTRIBUTES(metadata.getATTRIBUTES());
		metadataTank.setCREATETIME(dataTime);
		metadataTank.setDESCRIPTION(metadata.getDESCRIPTION());
		metadataTank.setKeyid(metadataId);
		metadataTank.setMETAMODELID(metadata.getMETAMODELID());
		metadataTank.setNAME(metadata.getNAME());
		metadataTank.setUPDATETIME(dataTime);
		metadataTank.setVERSION(metadata.getVERSION());
		metadataTank.setCOLLECTJOBID(0);

		if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
			throw new RuntimeException("insertMetaDataTank Error");
		}

		return metadataId;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到元数据对应的元模型信息,这个方法显示全部元模型的信息
	 * 
	 */
	public JSONArray getMetamodelInfoForAddMetadata(String metamodelid){

		JSONObject metamodelInfoMap = getMetamodelInfo(metamodelid);

		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_ATTRIBUTES);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_COLLECTJOBID);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_METAMODELID);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_CHECKSTATUS);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_VERSION);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_UPDATETIME);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_CREATETIME);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_ID);

		JSONArray metamodelInfos = new JSONArray();  
		Iterator iterator = metamodelInfoMap.keys();  
		while(iterator.hasNext()){
			JSONObject metamodelInfo = new JSONObject();
			String key = (String) iterator.next();  
			String describe = metamodelInfoMap.get(key).toString();
			metamodelInfo.put("key", key);
			metamodelInfo.put("describe", describe);
			metamodelInfos.add(metamodelInfo);
		}  
		return metamodelInfos;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到COMPOSITION类型的儿子元模型
	 * 
	 */
	public List<JSONObject> getCOMPOSITIONMetamodel(String metamodelId){
		List<Metamodel_hierarchy> metamodel_hierarchys = iMetamodel_hierarchyDao.getCOMPOSITIONMetamodel(metamodelId);
		if(metamodel_hierarchys.size()==0){
			return null;
		}else{
			List<JSONObject> includeMetaModel = new ArrayList<JSONObject>();

			for(int i=0;i<metamodel_hierarchys.size();i++){
				Metamodel_hierarchy metamodel_hierarchy = metamodel_hierarchys.get(i);
				JSONObject metamodel = new JSONObject();
				metamodel.put("modelid",metamodel_hierarchy.getId());
				metamodel.put("name",metamodel_hierarchy.getName());
				includeMetaModel.add(metamodel);
			}

			return includeMetaModel;
		}
	}


	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到元数据对应的元模型信息,这个方法显示全部元模型的信息
	 * 
	 */
	public JSONObject getMetamodelInfo(Object attribute_metamodelid){
		Map<Object,Object> metamodelidSelectMap = new HashMap<Object,Object>();
		metamodelidSelectMap.put(GlobalMethodAndParams.Public_metamodelid_Name, GlobalMethodAndParams.PublicMetamodelId);
		metamodelidSelectMap.put(GlobalMethodAndParams.Attribute_metamodelid_Name, attribute_metamodelid);
		List<Metamodel_datatype> metamodel_datatypes = iMetamodel_datatypeDao.getMetamodelDatatype_PublicAndAttributes(metamodelidSelectMap);

		JSONObject metamodelInfo = new JSONObject();
		for(int i=0;i<metamodel_datatypes.size();i++){
			Metamodel_datatype metamodel_datatype= metamodel_datatypes.get(i);
			metamodelInfo.put(metamodel_datatype.getName(), metamodel_datatype.getDesribe());
		}
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_ATTRIBUTES);
		return metamodelInfo;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月19日
	 * 作用:获取字段元数据,要将元数据的attributes属性展开
	 * 
	 */
	public List<Object> getFieldMetadata(int tableMetadataId) {
		List<Object> metadataList = new ArrayList<Object>();
		List<Metadata> fieldMetadatas= imetadataManagementDao.getFieldMetadata(tableMetadataId);
		for(int i = 0 ; i < fieldMetadatas.size() ; i++) {
			Metadata metadata= (Metadata)fieldMetadatas.get(i);
			Map<String, Object> metadataMap = TransformMetadata.transformMetadataToMap(metadata);
			metadataList.add(metadataMap);
		}
		return metadataList;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月19日
	 * 作用:根据元数据id,获取某一个元数据
	 */
	public Map<String, Object> getMetadata(String metadataId) {
		Metadata metadata= imetadataManagementDao.getMetadata(metadataId);
		Map<String, Object> metadataMap = TransformMetadata.transformMetadataToMap(metadata);
		return metadataMap;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:寻找可依赖的元数据
	 */
	public List<Map<String, Object>> getDependMetadata(String metadataidStr) {
		return imetadataManagementDao.getDependMetadata(metadataidStr);
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:添加元数据依赖
	 */
	public boolean addMetadataDepend(String metadataid,String relatedmetadataid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataid", metadataid);
		map.put("relatedmetadataid", relatedmetadataid);

		if(imetadataManagementDao.addMetadataDepend(map)>0){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:展示元数据依赖关系
	 */
	public List<Map<String, Object>> showMetadataDepend(String metadataidStr) {
		return imetadataManagementDao.showMetadataDepend(metadataidStr);
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:删除元数据依赖关系
	 */
	public boolean deleteMetadataDepend(String relationidStr) {
		if(imetadataManagementDao.deleteMetadataDepend(relationidStr)>0){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月25日
	 * 作用:得到历史版本元数据的公共属性
	 */
	public List<Map<String, Object>> getHistoryMetadataCommonInfo(String metadataidStr) {
		List<Map<String, Object>> historyMetadataMapList = new ArrayList<Map<String, Object>>();
		List<MetadataTank> historyMetadataList = imetadataManagementDao.getHistoryMetadataCommonInfo(metadataidStr);
		Map<String, Object> map = null;
		for(MetadataTank metadataTank : historyMetadataList){
			map = new HashMap<String, Object>();
			map.put("tankid", metadataTank.getID());
			map.put("ID", metadataTank.getKeyid());
			map.put("NAME", metadataTank.getNAME());
			map.put("DESCRIPTION", metadataTank.getDESCRIPTION());
			map.put("CREATETIME", metadataTank.getCREATETIME());
			map.put("UPDATETIME", metadataTank.getUPDATETIME());
			map.put("VERSION", metadataTank.getVERSION());
			map.put("COLLECTJOBID", metadataTank.getCOLLECTJOBID());
			map.put("CHECKSTATUS", metadataTank.getCHECKSTATUS());
			map.put("METAMODELID", metadataTank.getMETAMODELID());

			historyMetadataMapList.add(map);
		}
		return historyMetadataMapList;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月25日
	 * 作用:得到历史版本元数据的私有属性
	 */
	public JSONObject getHistoryMetadataPrivateInfo(String tankidStr) {
		String privateInfo = imetadataManagementDao.getHistoryMetadataPrivateInfo(tankidStr);
		return JSONObject.fromObject(privateInfo);
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月25日
	 * 作用:得到元模型私有属性
	 */
	public Map<String,Object> getMetamodelPrivateInfo(String metamodelidStr) {
		List<Metamodel_datatype> metamodel_datatypes = imetadataManagementDao.getMetamodelPrivateInfo(metamodelidStr);

		Map<String,Object> metamodelInfo = new HashMap();
		Metamodel_datatype metamodel_datatype = null;
		for(int i=0;i<metamodel_datatypes.size();i++){
			metamodel_datatype = metamodel_datatypes.get(i);
			metamodelInfo.put(metamodel_datatype.getName(), metamodel_datatype.getDesribe());
		}

		return metamodelInfo;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月26日
	 * 作用:添加表字段映射元数据
	 */
	@Transactional
	public boolean addTableFieldMappingMetadata(List<Map<String, Object>> list) {
		for(Map<String, Object> map:list){
			if(addMetadata(map)<=0){
				throw new RuntimeException("addTableFieldMappingMetadata Error");
			}
		}
		return true;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:请求第一层试图节点
	 */
	public List<Map<String, Object>> getViewNode(String viewid) {
		return TraverseBoolean(imetadataManagementDao.getViewNode(viewid));
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:请求元数据的第一层节点
	 */
	public List<Map<String, Object>> getMetadataFirstNode(String id) {
		return TraverseBoolean(imetadataManagementDao.getMetadataFirstNode(id));
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:请求元数据的其他层次节点
	 */
	public List<Map<String, Object>> getMetadataOtherNode(String metadataid) {
		return TraverseBoolean(imetadataManagementDao.getMetadataOtherNode(metadataid));
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:将字符串的boolean转换
	 */
	private List<Map<String, Object>> TraverseBoolean(List<Map<String, Object>> list){
		for(Map<String, Object> map : list){
			boolean temp = map.get("leaf").equals("true")?true:false;
			map.put("leaf", temp);
		}
		return list;
	}

}
