import { FilterMetadata } from "primeng/api";
import { UIFilterData } from './uifilter-data';

describe('UIFilterData', () => {
  it('should create an instance', () => {
    expect(new UIFilterData(new class implements FilterMetadata {
      matchMode: string;
      operator: string;
      value: any;
    })).toBeTruthy();
  });
});
