import { TableLazyLoadEvent } from 'primeng/table';

import { Hero } from '@appModel/hero';


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
                this.sortFields.push({ field: msm.field, order: msm.order });
            }
        } else if (event.sortField && event.sortOrder) {
            const f = typeof event.sortField === 'string' ? event.sortField : event.sortField[0];

            this.sortFields = [];
            this.sortFields.push({ field: f, order: event.sortOrder});
        }
 
        if (event.filters) {
            var mf: {
                [s: string]: UIFilterData[];
            } = {};

            for (var key in event.filters) {
                var value = event.filters[key];
                if (value) {
                    mf[key] = !Array.isArray(value) ? [value] : value;
                }
            }
 
            this.filters = mf;
        }
    }
}

/**
 * Represents metadata for sorting.
 * @group Interface
 */
export interface UIFilterSort {
    field: string;
    order: number;
}

/**
 * Represents metadata for filtering a data set.
 * @group Interface
 */
export interface UIFilterData {
    /**
     * The value used for filtering.
     */
    value?: any;
    /**
     * The match mode for filtering.
     */
    matchMode?: string;
    /**
     * The operator for filtering.
     */
    operator?: string;
}

/**
 * Result contains the data and record count (for pagination)
 */
export interface UIFilterResult {
    heroes: Hero[];
    totalRecords: number;
}
