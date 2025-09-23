import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ProntuarioService } from './prontuario.service';

describe('ProntuarioService', () => {
  let service: ProntuarioService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProntuarioService]
    });
    service = TestBed.inject(ProntuarioService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Cirurgias', () => {
    it('deve buscar cirurgias do paciente', () => {
      const mockData = { data: [{ id: 1, name: 'Cirurgia' }] };
      const pacienteId = '123';

      service.getCirurgiasPaciente(pacienteId).subscribe(cirurgias => {
        expect(cirurgias).toEqual(mockData.data);
      });

      const req = httpMock.expectOne(`http://localhost:8080/api/patients/${pacienteId}/surgeries`);
      expect(req.request.method).toBe('GET');
      req.flush(mockData);
    });

    it('deve adicionar cirurgia', () => {
      const mockCirurgia = { name: 'Nova Cirurgia' };
      const mockResponse = { id: 1, name: 'Nova Cirurgia' };
      const pacienteId = '123';

      service.adicionarCirurgia(pacienteId, mockCirurgia).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`http://localhost:8080/api/patients/${pacienteId}/surgeries`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockCirurgia);
      req.flush(mockResponse);
    });
  });

  // Outros testes podem ser adicionados para as demais funcionalidades
});