package com.axelor.gst.repo;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.SequenceService;
import com.google.inject.Inject;

public class GstSequenceRepository extends SequenceRepository{
	
	@Inject
	SequenceService sequenceService;

	@Override
	public Sequence save(Sequence entity) {
		if(entity.getNextNumber() == null) {
		   Sequence nextSequence = sequenceService.computeNextSequence(entity);
		   return super.save(nextSequence);
	    }
		return super.save(entity);
	}
    
}
