import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Prontuarios } from './prontuarios';

describe('Prontuarios', () => {
  let component: Prontuarios;
  let fixture: ComponentFixture<Prontuarios>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Prontuarios]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Prontuarios);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
