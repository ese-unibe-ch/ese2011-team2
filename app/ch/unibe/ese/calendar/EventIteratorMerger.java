package ch.unibe.ese.calendar;

import java.util.Iterator;

/**
 * Merges multiple Iterator<CalendarEvent> into one, assuming the se iterators are 
 * chronological by start date this Iterator will be so too
 *
 */
class EventIteratorMerger implements Iterator<CalendarEvent> {

	private Iterator<CalendarEvent>[] baseIterators;
	private CalendarEvent[] nextFromBase;
	private CalendarEvent next;
	private StartDateComparator startDateComparator = new StartDateComparator();
	
	
	EventIteratorMerger(Iterator<CalendarEvent>... baseIterators) {
		if (baseIterators.length < 2) {
			throw new IllegalArgumentException("must merge at least 2 base iterator");
		}
		this.baseIterators = baseIterators;
		nextFromBase = new CalendarEvent[baseIterators.length];
		for (int i = 0; i < baseIterators.length; i++) {
			Iterator<CalendarEvent> baseIter = baseIterators[i];
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
	public CalendarEvent next() {
		CalendarEvent result = next;
		prepareNext();
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("not implemented");	
	}

}
