package com.axelor.gst.service;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public interface PartyService {
   public void computeReference(ActionRequest request,ActionResponse response);
}