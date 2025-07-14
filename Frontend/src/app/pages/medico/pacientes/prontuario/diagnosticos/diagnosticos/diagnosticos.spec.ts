import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Diagnosticos } from './diagnosticos';

describe('Diagnosticos', () => {
  let component: Diagnosticos;
  let fixture: ComponentFixture<Diagnosticos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Diagnosticos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Diagnosticos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
