import {FinTechAuthorizationService} from '../../api';
import {Router} from '@angular/router';
import {Injectable} from '@angular/core';
import {Consent, HeaderConfig} from '../../models/consts';
import {StorageService} from '../../services/storage.service';

@Injectable({
  providedIn: 'root'
})
export class ConsentAuthorizationService {
  constructor(
    private router: Router,
    private authService: FinTechAuthorizationService,
    private storageService: StorageService
  ) {}

  fromConsentOk(authId: string, okOrNotOk: Consent, redirectCode: string) {
    console.log('pass auth id:' + authId + ' okOrNotOk ' + okOrNotOk + ' redirect code ' + redirectCode);
    this.authService.fromConsentGET(
      authId,
      okOrNotOk,
      redirectCode,
    '',
    '',
    'response'
  ).subscribe(resp => {
    console.log(resp);
      this.storageService.setXsrfToken(resp.headers.get(HeaderConfig.HEADER_FIELD_X_XSRF_TOKEN));
      this.router.navigate([resp.headers.get('Location')]);
      this.storageService.setRedirectActive(false);
      this.storageService.setRedirectCode(null);
    });
  }
}
