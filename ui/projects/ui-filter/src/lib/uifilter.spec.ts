import { FilterMetadata, SortMeta } from "primeng/api";
import { TableLazyLoadEvent } from "primeng/table";
import { UIFilter } from './uifilter';

describe('UIFilter', () => {
  it('should create an instance', () => {
    expect(new UIFilter(new class implements TableLazyLoadEvent {
      filters: { [p: string]: FilterMetadata | FilterMetadata[] | undefined };
      first: number;

      forceUpdate(p0) {
      }

      globalFilter: string | string[] | undefined | null;
      last: number;
      multiSortMeta: SortMeta[] | undefined | null;
      rows: number | undefined | null;
      sortField: string | string[] | null | undefined;
      sortOrder: number | undefined | null;
    })).toBeTruthy();
  });
});
