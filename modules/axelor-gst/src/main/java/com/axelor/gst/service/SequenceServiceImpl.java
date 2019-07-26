package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public class SequenceServiceImpl implements SequenceService {

	private String nextNumber;

	@Override
	public String computeNextSequence(Sequence sequence) {
		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		Integer padding = sequence.getPadding();
		String paddedStr = String.format("%0" + padding + "d", 0);
        if(suffix == null) {
        	nextNumber = prefix + paddedStr;
        }
        else {
        	nextNumber = prefix + paddedStr + suffix;
        }
		return nextNumber;
	}
}