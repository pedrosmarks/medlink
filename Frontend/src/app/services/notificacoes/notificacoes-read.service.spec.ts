import { TestBed } from '@angular/core/testing';

import { NotificacoesReadService } from './notificacoes-read.service';

describe('NotificacoesReadService', () => {
  let service: NotificacoesReadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificacoesReadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
