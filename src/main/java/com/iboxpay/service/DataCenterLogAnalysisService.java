package com.iboxpay.service;

import java.util.List;

import com.iboxpay.entity.ErrorInfo;

public interface DataCenterLogAnalysisService {

  public List<ErrorInfo> analyzeLogFromDataCenter(String dateStr) throws Exception;
}
