package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public class PartyServiceImpl implements PartyService {

	@Override
	public String computeReference(Sequence sequence) {
		 String prefix = sequence.getPrefix();
		 String suffix = sequence.getSuffix();
		 Integer padding = sequence.getPadding();
		 String previousNumberStr = sequence.getNextNumber();
		 String nextNumberstr;

		// splitting
		 previousNumberStr = sequence.getNextNumber().substring(prefix.length(), prefix.length() + padding);

		// increment
		String incremented = String.format("%0" + previousNumberStr.length() + "d", Integer.parseInt(previousNumberStr) + 1);

		// merging
		if(suffix == null) {
			nextNumberstr = prefix + incremented;
        }
        else {
        	nextNumberstr = prefix + incremented + suffix;
        }

		return nextNumberstr;
	}

}