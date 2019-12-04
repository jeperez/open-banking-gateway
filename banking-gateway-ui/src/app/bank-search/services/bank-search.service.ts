import { Injectable } from '@angular/core';
import {Bank} from "../models/bank.model";
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BankSearchService {

  readonly API_PATH = '/to-be-defined';

  private static BANK_STUBS: Bank[] = [
    {id: '0', name: 'Sparkasse Nürnberg', bic: 'SSXXXX', bankCode: 373737},
    {id: '1', name: 'Sparkasse Ausburg', bic: 'SSXXXX', bankCode: 473737},
    {id: '2', name: 'Deutsche Bank', bic: 'SSXYXX', bankCode: 373307},
    {id: '3', name: 'Sparda Nürnberg', bic: 'SSXXXX', bankCode: 488737}
  ];

  constructor() { }

  getBanks() {
    return of(BankSearchService.BANK_STUBS);
  }
}
