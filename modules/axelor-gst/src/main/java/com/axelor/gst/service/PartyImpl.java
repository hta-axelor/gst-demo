package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public class PartyImpl implements PartyService {

	@Override
	public String computeReference(Sequence sequence) {
		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		Integer padding = sequence.getPadding();
		String nextNumberStr = sequence.getNextNumber();

		// splitting
		nextNumberStr = sequence.getNextNumber().substring(prefix.length(), nextNumberStr.length() - suffix.length());

		// increment
		String incremented = String.format("%0" + nextNumberStr.length() + "d", Integer.parseInt(nextNumberStr) + 1);

		// merging
		nextNumberStr = prefix + incremented + suffix;

		return nextNumberStr;
	}

}