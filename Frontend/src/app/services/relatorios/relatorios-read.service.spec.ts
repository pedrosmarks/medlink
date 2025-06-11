import { TestBed } from '@angular/core/testing';

import { RelatoriosReadService } from './relatorios-read.service';

describe('RelatoriosReadService', () => {
  let service: RelatoriosReadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RelatoriosReadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
