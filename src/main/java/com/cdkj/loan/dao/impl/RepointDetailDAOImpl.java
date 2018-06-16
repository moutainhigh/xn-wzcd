package com.cdkj.loan.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cdkj.loan.dao.IRepointDetailDAO;
import com.cdkj.loan.dao.base.support.AMybatisTemplate;
import com.cdkj.loan.domain.RepointDetail;



//CHECK 。。。 
@Repository("repointDetailDAOImpl")
public class RepointDetailDAOImpl extends AMybatisTemplate implements IRepointDetailDAO {


	@Override
	public int insert(RepointDetail data) {
		return super.insert(NAMESPACE.concat("insert_repointDetail"), data);
	}


	@Override
	public int delete(RepointDetail data) {
		return super.delete(NAMESPACE.concat("delete_repointDetail"), data);
	}


	@Override
	public RepointDetail select(RepointDetail condition) {
		return super.select(NAMESPACE.concat("select_repointDetail"), condition,RepointDetail.class);
	}


	@Override
	public Long selectTotalCount(RepointDetail condition) {
		return super.selectTotalCount(NAMESPACE.concat("select_repointDetail_count"),condition);
	}


	@Override
	public List<RepointDetail> selectList(RepointDetail condition) {
		return super.selectList(NAMESPACE.concat("select_repointDetail"), condition,RepointDetail.class);
	}


	@Override
	public List<RepointDetail> selectList(RepointDetail condition, int start, int count) {
		return super.selectList(NAMESPACE.concat("select_repointDetail"), start, count,condition, RepointDetail.class);
	}


}