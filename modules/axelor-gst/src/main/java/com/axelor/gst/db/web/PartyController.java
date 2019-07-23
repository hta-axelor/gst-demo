package com.axelor.gst.db.web;

import java.util.List;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.PartyService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
@Transactional
public class PartyController {

	@Inject
	private PartyService partyService;

	public void setReference(ActionRequest request, ActionResponse response) {
		SequenceRepository sequenceRepository = Beans.get(SequenceRepository.class);
		Sequence sequence = sequenceRepository.all().filter("self.metaModel.fullName = ?1", request.getModel()).fetchOne();
		if (sequence == null) {
            response.setError("No Sequence Found, Please enter the sequence");
		} else {
			response.setValue("reference", sequence.getNextNumber());
			String nextNumber = partyService.computeReference(sequence);
			sequence.setNextNumber(nextNumber);
			sequenceRepository.save(sequence);
		}
	}
}