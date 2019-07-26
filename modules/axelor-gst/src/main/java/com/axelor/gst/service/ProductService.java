package com.axelor.gst.service;

import java.util.List;
import java.util.Map;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Party;

public interface ProductService {
   public Map<String,Object> getInvoiceView(Company company,Party party,List<String> productIdList);
}