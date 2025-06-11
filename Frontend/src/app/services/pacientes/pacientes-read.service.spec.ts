import { TestBed } from '@angular/core/testing';

import { PacientesReadService } from './pacientes-read.service';

describe('PacientesReadService', () => {
  let service: PacientesReadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PacientesReadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
