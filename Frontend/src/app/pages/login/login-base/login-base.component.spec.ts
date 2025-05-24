import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginBaseComponent } from './login-base.component';

describe('LoginBaseComponent', () => {
  let component: LoginBaseComponent;
  let fixture: ComponentFixture<LoginBaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginBaseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginBaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
