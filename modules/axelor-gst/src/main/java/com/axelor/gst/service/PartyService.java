package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public interface PartyService {
   public String computeReference(Sequence sequence);
}