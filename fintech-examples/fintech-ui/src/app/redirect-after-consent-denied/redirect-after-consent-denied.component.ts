import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { Observable, timer } from "rxjs";
import { map, take } from "rxjs/operators";

@Component({
  selector: 'app-redirect-after-consent-denied',
  templateUrl: './redirect-after-consent-denied.component.html',
  styleUrls: ['./redirect-after-consent-denied.component.scss']
})
export class RedirectAfterConsentDeniedComponent implements OnInit {

  seconds = 5;
  private countDown$: Observable<number>;

  constructor(private router: Router) {
    this.countDown$ = timer(0,1000).pipe(
      take(this.seconds),
      map(() => this.seconds--)
    );
  }

  ngOnInit() {
    this.countDown$.subscribe(() => {
      if(this.seconds == 0)
        this.router.navigate(["/"]);
    })
  }

  toBankSearch(): void {
    this.router.navigate(["/"]);
  }

  toDashboard(): void {
    this.router.navigate(["/"]);
  }
}