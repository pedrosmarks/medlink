import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Vacinas } from './vacinas';

describe('Vacinas', () => {
  let component: Vacinas;
  let fixture: ComponentFixture<Vacinas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Vacinas]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Vacinas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
