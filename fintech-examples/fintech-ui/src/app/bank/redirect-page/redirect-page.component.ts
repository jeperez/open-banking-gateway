import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RedirectStruct } from './redirect-struct';
import { StorageService } from '../../services/storage.service';
import {Consent, HeaderConfig} from '../../models/consts';
import {ConsentAuthorizationService} from '../services/consent-authorization.service';

@Component({
  selector: 'app-redirect-page',
  templateUrl: './redirect-page.component.html',
  styleUrls: ['./redirect-page.component.scss']
})
export class RedirectPageComponent implements OnInit {
  private bankId;
  public bankName;
  private location;
  private cancelPath;

  constructor(private authService:ConsentAuthorizationService, private router: Router,
              private route: ActivatedRoute, private storageService: StorageService) {}

  ngOnInit() {
    this.route.paramMap.subscribe(p => {
      const r: RedirectStruct = JSON.parse(p.get(HeaderConfig.HEADER_FIELD_LOCATION));
      this.location = decodeURIComponent(r.okUrl);
      this.cancelPath = decodeURIComponent(r.cancelUrl);
      console.log('LOCATION IS ', this.location);
    });
    // TODO this is no more the routing path approach
    this.bankName = this.storageService.getBankName();
    //  this.bankId = this.route.parent.parent.parent.snapshot.paramMap.get('bankid');
    //  console.log('redirect page for bankid', this.bankId);
  }

  cancel(): void {
    const authId = this.storageService.getAuthId();
    const redirectCode = this.storageService.getRedirectCode();
    console.log('call from consent NOT ok with auth ' + authId);
    this.authService.fromConsentOk(authId, Consent.NOT_OK, redirectCode);
  }

  proceed(): void {
    console.log('NOW GO TO:', this.location);
    window.location.href = this.location;
  }
}
