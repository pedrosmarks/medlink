import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Medico } from './medico';

describe('Medico', () => {
  let component: Medico;
  let fixture: ComponentFixture<Medico>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Medico]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Medico);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
