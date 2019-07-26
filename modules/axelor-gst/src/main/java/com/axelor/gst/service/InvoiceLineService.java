package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.State;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public interface InvoiceLineService {
   public BigDecimal calculateGstRate(Product product);
   public String getItem(Product product);
   public BigDecimal calculateprice(Product product);
   public void calculateGstValues(ActionRequest request, ActionResponse response);
}