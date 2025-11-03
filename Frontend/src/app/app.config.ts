import { ApplicationConfig, provideZoneChangeDetection, isDevMode } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { HttpRequest, HttpHandlerFn } from '@angular/common/http';

import { routes } from './app.routes';
import { provideServiceWorker } from '@angular/service-worker';

// Interceptor funcional para Angular 17+
function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const token = localStorage.getItem('authToken');
  
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }
  
  return next(req);
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), 
    provideServiceWorker('ngsw-worker.js', {
      enabled: !isDevMode(),
      registrationStrategy: 'registerWhenStable:30000'
    }),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor]))
  ]
};
