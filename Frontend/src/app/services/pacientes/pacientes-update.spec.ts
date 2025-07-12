import { TestBed } from '@angular/core/testing';

import { PacientesUpdateService } from './pacientes-update';

describe('PacientesUpdate', () => {
  let service: PacientesUpdateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PacientesUpdateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
