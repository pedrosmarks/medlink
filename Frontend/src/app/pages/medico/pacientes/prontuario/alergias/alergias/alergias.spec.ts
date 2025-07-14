import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Alergias } from './alergias';

describe('Alergias', () => {
  let component: Alergias;
  let fixture: ComponentFixture<Alergias>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Alergias]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Alergias);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
