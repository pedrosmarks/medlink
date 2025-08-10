import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Requisicoes } from './requisicoes';

describe('Requisicoes', () => {
  let component: Requisicoes;
  let fixture: ComponentFixture<Requisicoes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Requisicoes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Requisicoes);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
