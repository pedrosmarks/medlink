import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsentimentoLgpdComponent } from './consentimento-lgpd.component';

describe('ConsentimentoLgpdComponent', () => {
  let component: ConsentimentoLgpdComponent;
  let fixture: ComponentFixture<ConsentimentoLgpdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsentimentoLgpdComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsentimentoLgpdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have all checkboxes unchecked by default', () => {
    expect(component.todosCheckboxesMarcados()).toBeFalsy();
  });

  it('should enable accept button when all checkboxes are checked', () => {
    component.checkboxes = {
      leuCompreendeu: true,
      autorizaTratamento: true,
      consentiuCompartilhamento: true,
      cienteDireitos: true,
      concordaPolitica: true
    };
    expect(component.todosCheckboxesMarcados()).toBeTruthy();
  });

  it('should clear all checkboxes', () => {
    component.checkboxes.leuCompreendeu = true;
    component.limparCheckboxes();
    expect(component.todosCheckboxesMarcados()).toBeFalsy();
  });
});