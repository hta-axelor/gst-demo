package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public interface PartyService {
   public void computeReference(ActionRequest request,ActionResponse response);
}