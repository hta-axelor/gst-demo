package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;

public interface PartyService {
   public void computeReference(Sequence sequence);
}