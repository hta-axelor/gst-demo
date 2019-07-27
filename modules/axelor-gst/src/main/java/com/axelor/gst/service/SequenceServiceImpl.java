package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public class SequenceServiceImpl implements SequenceService {

	@Override
	public Sequence computeNextSequence(Sequence sequence) {
		String nextNumber = null;
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
        sequence.setNextNumber(nextNumber);
		return sequence;
	}
}