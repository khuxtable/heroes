import { TableLazyLoadEvent } from 'primeng/table';

import { Hero } from '@appModel/hero';


export class UIFilter {
    /**
     * The index of the first record to be loaded.
     */
    first?: number;

    /**
     * The index of the last record to be loaded.
     */
    last?: number;

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
        [s: string]: UIFilterMetadata[];
    };

	/** 
	 * Convert a PrimeNG lazy load event to a HeroFilter.
	 */
    constructor(event: TableLazyLoadEvent) {
        this.first = event.first;
        this.last = event.last;
 
        if (event.rows) {
            this.rows = event.rows;
        }
 
        if (event.multiSortMeta) {
            this.sortFields = [];
            for (var msm of event.multiSortMeta) {
                this.sortFields.push({ field: msm.field, order: msm.order });
            }
        } else if (event.sortField && event.sortOrder) {
            const f = typeof event.sortField === 'string' ? [event.sortField] : event.sortField;
            const o = typeof event.sortOrder === 'number' ? [event.sortOrder] : event.sortOrder;
 
            this.sortFields = [];
            for (var i = 0; i < f.length; i++) {
                // Need to handle non-matchingg lengths
                this.sortFields.push({ field: f[i], order: o[i] });
            }
        }
 
        if (event.filters) {
            var mf: {
                [s: string]: UIFilterMetadata[];
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
export interface UIFilterMetadata {
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
