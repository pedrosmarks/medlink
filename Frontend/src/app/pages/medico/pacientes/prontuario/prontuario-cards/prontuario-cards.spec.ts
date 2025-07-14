import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProntuarioCards } from './prontuario-cards';

describe('ProntuarioCards', () => {
  let component: ProntuarioCards;
  let fixture: ComponentFixture<ProntuarioCards>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProntuarioCards]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProntuarioCards);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
