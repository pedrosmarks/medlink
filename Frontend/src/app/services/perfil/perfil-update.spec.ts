import { TestBed } from '@angular/core/testing';

import { PerfilUpdate } from './perfil-update';

describe('PerfilUpdate', () => {
  let service: PerfilUpdate;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PerfilUpdate);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
