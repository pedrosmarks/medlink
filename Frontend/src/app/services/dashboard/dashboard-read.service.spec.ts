import { TestBed } from '@angular/core/testing';

import { DashboardReadService } from './dashboard-read.service';

describe('DashboardReadService', () => {
  let service: DashboardReadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DashboardReadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
