package ch.unibe.ese.calendar.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ch.unibe.ese.calendar.CalendarEvent;

public class EventIteratorUtils {
	
	

	/**
	 * Merges multiple Iterator<CalendarEvent> into one, assuming these iterators
	 * are chronological by start date this Iterator will be so too
	 */
	public static Iterator<CalendarEvent> merge(Iterator<CalendarEvent>... baseIterators) {
		return merge(Arrays.asList(baseIterators));
	}
	
	/**
	 * Merges multiple Iterator<CalendarEvent> into one, assuming these iterators
	 * are chronological by start date this Iterator will be so too
	 */
	public static Iterator<CalendarEvent> merge(List<Iterator<CalendarEvent>> iteratorList) {
		if (iteratorList.size() == 1) {
			return iteratorList.get(0);
		}
		return new EventIteratorMerger(iteratorList);
		
	}
	/**
	 * The filtered iterator only contains events ending after the specified endDate
	 * 
	 * @param base the iterator to be filtered
	 * @parem earliestEndDate the date after which events must end
	 * @return the filtered iterator
	 */
	public static Iterator<CalendarEvent> filterByEndDate(Iterator<CalendarEvent> base, final Date earliestEndDate) {
		return new FilteringIterator(base, new IteratorFilter() {

			@Override
			public boolean accept(CalendarEvent event) {
				return (event != null) && event.getEnd().after(earliestEndDate);
			}
			
		});
		
	}

	private static class EventIteratorMerger implements Iterator<CalendarEvent> {

		private List<Iterator<CalendarEvent>> baseIterators;
		private CalendarEvent[] nextFromBase;
		private CalendarEvent next;
		private StartDateComparator startDateComparator = new StartDateComparator();

		public EventIteratorMerger(List<Iterator<CalendarEvent>> baseIterators) {
			if (baseIterators.size() < 2) {
				throw new IllegalArgumentException(
						"must merge at least 2 base iterator");
			}
			this.baseIterators = baseIterators;
			nextFromBase = new CalendarEvent[baseIterators.size()];
			for (int i = 0; i < baseIterators.size(); i++) {
				Iterator<CalendarEvent> baseIter = baseIterators.get(i);
				if (baseIter.hasNext()) {
					nextFromBase[i] = baseIter.next();
				} else {
					// just to be explicit
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
			for (int i = (currentEarliest + 1); i < nextFromBase.length; i++) {
				if (nextFromBase[i] == null) {
					continue;
				}
				if (startDateComparator.compare(nextFromBase[i],
						nextFromBase[currentEarliest]) < 0) {
					currentEarliest = i;
				}
			}
			next = nextFromBase[currentEarliest];
			if (baseIterators.get(currentEarliest).hasNext()) {
				nextFromBase[currentEarliest] = baseIterators.get(currentEarliest)
						.next();
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
	
	private static interface IteratorFilter {
		boolean accept(CalendarEvent event);
	}

	private static class FilteringIterator implements Iterator<CalendarEvent> {

		private Iterator<CalendarEvent> base;
		private IteratorFilter iteratorFilter;
		private CalendarEvent next;
		private boolean hasMoreElements;
		
		public FilteringIterator(Iterator<CalendarEvent> base,
				IteratorFilter iteratorFilter) {
			this.base = base;
			this.iteratorFilter = iteratorFilter;
			prepareNext();
		}

		private void prepareNext() {
			hasMoreElements = false;
			next = null;
			while (base.hasNext()) {
				CalendarEvent tentativeNext = base.next();
				if (iteratorFilter.accept(tentativeNext)) {
					hasMoreElements = true;
					next = tentativeNext;
					return;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return hasMoreElements;
		}

		@Override
		public CalendarEvent next() {
			CalendarEvent result = next;
			prepareNext();
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}

	}
}
