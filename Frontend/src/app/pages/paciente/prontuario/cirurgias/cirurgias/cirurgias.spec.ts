import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Cirurgias } from './cirurgias';

describe('Cirurgias', () => {
  let component: Cirurgias;
  let fixture: ComponentFixture<Cirurgias>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Cirurgias]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Cirurgias);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
