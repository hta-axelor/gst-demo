package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;

public class PartyServiceImpl implements PartyService {
	
	@Override
	@Transactional
	public void computeReference(ActionRequest request,ActionResponse response) {
		SequenceRepository sequenceRepository = Beans.get(SequenceRepository.class);
		Sequence sequence = sequenceRepository.all().filter("self.metaModel.fullName = ?1", request.getModel())
				.fetchOne();
		if(sequence == null) {
			response.setError("No Sequence Found, Please enter the sequence");
			return;
		}
		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		Integer padding = sequence.getPadding();
		String previousNumberStr = sequence.getNextNumber();
		String nextNumberstr;

		// splitting
		previousNumberStr = sequence.getNextNumber().substring(prefix.length(), prefix.length() + padding);

		// increment
		String incremented = String.format("%0" + previousNumberStr.length() + "d",
				Integer.parseInt(previousNumberStr) + 1);

		// merging
		if (suffix == null) {
			nextNumberstr = prefix + incremented;
		} else {
			nextNumberstr = prefix + incremented + suffix;
		}
		response.setValue("reference", sequence.getNextNumber());
		sequence.setNextNumber(nextNumberstr);
		sequenceRepository.save(sequence);

	}
}