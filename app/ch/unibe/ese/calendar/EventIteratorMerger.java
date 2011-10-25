package ch.unibe.ese.calendar;

import java.util.Iterator;

/**
 * Merges multiple Iterator<CalendarEntry> into one, assuming these iterators are 
 * chronological by start date this Iterator will be so too
 *
 */
public class EventIteratorMerger implements Iterator<CalendarEntry> {

	private Iterator<CalendarEntry>[] baseIterators;
	private CalendarEntry[] nextFromBase;
	private CalendarEntry next;
	private StartDateComparator startDateComparator = new StartDateComparator();
	
	
	public EventIteratorMerger(Iterator<CalendarEntry>... baseIterators) {
		if (baseIterators.length < 2) {
			throw new IllegalArgumentException("must merge at least 2 base iterator");
		}
		this.baseIterators = baseIterators;
		nextFromBase = new CalendarEntry[baseIterators.length];
		for (int i = 0; i < baseIterators.length; i++) {
			Iterator<CalendarEntry> baseIter = baseIterators[i];
			if (baseIter.hasNext()) {
				nextFromBase[i] = baseIter.next();
			} else {
				//just to be explicit
				nextFromBase[i] = null;
			}
		}
		prepareNext();
	}

	private void prepareNext() {
		next = null;
		int currentEarliest = 0;
		while (nextFromBase[currentEarliest] == null) {
			currentEarliest++;
			if (currentEarliest == nextFromBase.length) {
				return;
			}
		}
		for (int i = (currentEarliest+1); i < nextFromBase.length; i++) {
			if (nextFromBase[i] == null) {
				continue;
			}
			if (startDateComparator.compare(nextFromBase[i], nextFromBase[currentEarliest]) < 0) {
				currentEarliest = i;
			}
		}
		next = nextFromBase[currentEarliest];
		if (baseIterators[currentEarliest].hasNext()) {
			nextFromBase[currentEarliest] = baseIterators[currentEarliest].next();
		} else {
			nextFromBase[currentEarliest] = null;
		}
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public CalendarEntry next() {
		CalendarEntry result = next;
		prepareNext();
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("not implemented");	
	}

}
