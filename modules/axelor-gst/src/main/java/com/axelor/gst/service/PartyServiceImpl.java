package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.repo.GstSequenceRepository;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;


public class PartyServiceImpl implements PartyService {

	@Override
	@Transactional
	public void computeReference(Sequence sequence) {
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
		nextNumberstr = prefix + incremented + (suffix == null ? "" : suffix);
		
		sequence.setNextNumber(nextNumberstr);
		
		GstSequenceRepository sequenceRepository = Beans.get(GstSequenceRepository.class);
		sequenceRepository.save(sequence);
	}
}