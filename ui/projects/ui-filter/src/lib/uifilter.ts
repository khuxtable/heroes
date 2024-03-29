import { FilterMetadata } from "primeng/api";
import { TableLazyLoadEvent } from 'primeng/table';
import { UIFilterData } from "./uifilter-data";
import { UIFilterSort } from "./uifilter-sort";

/**
 * Simplified data from the PrimeNG filtering.
 */
export class UIFilter {
	/**
	 * The index of the first record to be loaded.
	 */
	first?: number;

	/**
	 * The number of rows to load.
	 */
	rows?: number;

	/**
	 * The fields and orders to be used for sorting.
	 */
	sortFields?: UIFilterSort[];

	/**
	 * An object containing filter metadata for filtering the data.
	 * The keys represent the field names, and the values represent the corresponding filter metadata.
	 */
	filters?: {
		[s: string]: UIFilterData[];
	};

	/**
	 * Convert a PrimeNG lazy load event to a HeroFilter.
	 */
	constructor(event: TableLazyLoadEvent) {
		this.first = event.first;

		if (event.rows) {
			this.rows = event.rows;
		} else if (event.last && event.first) {
			this.rows = event.last - event.first;
		} else if (event.last) {
			this.rows = event.last;
		}

		if (event.multiSortMeta) {
			this.sortFields = [];
			for (var msm of event.multiSortMeta) {
				this.sortFields.push({field: msm.field, order: msm.order});
			}
		} else if (event.sortField && event.sortOrder) {
			const f = typeof event.sortField === 'string' ? event.sortField : event.sortField[0];

			this.sortFields = [];
			this.sortFields.push({field: f, order: event.sortOrder});
		}

		if (event.filters) {
			this.filters = {};
			for (var key in event.filters) {
				var filter = event.filters[key];
				if (filter) {
					var mdList: FilterMetadata[] = Array.isArray(filter) ? filter : [filter];
					var uiFilter = mdList
						.filter(md => md.value)
						.map(md => new UIFilterData(md));
					if (uiFilter.length > 0) {
						this.filters[key] = uiFilter;
					}
				}
			}
		}
	}
}