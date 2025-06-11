import { TestBed } from '@angular/core/testing';

import { PerfilReadService } from './perfil-read.service';

describe('PerfilReadService', () => {
  let service: PerfilReadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PerfilReadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
