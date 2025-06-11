import { TestBed } from '@angular/core/testing';

import { MensagensReadService } from './mensagens-read.service';

describe('MensagensReadService', () => {
  let service: MensagensReadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MensagensReadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
