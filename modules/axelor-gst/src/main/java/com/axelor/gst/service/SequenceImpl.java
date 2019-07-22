package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public class SequenceImpl implements SequenceService {

	private String nextNumber;

	@Override
	public String computeNextSequence(Sequence sequence) {
		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		Integer padding = sequence.getPadding();
		if (prefix != null && suffix != null) {
			String paddedStr = String.format("%0" + padding + "d", 0);
			nextNumber = prefix + paddedStr + suffix;
		}
		return nextNumber;
	}
}